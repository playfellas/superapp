package it.playfellas.superapp.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import it.playfellas.superapp.events.BTDisconnectedEvent;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.InternalEvent;
import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.utils.Config;

/**
 * Wraps Otto Bus to extend it with the ability to post on the main thread
 */
public class TenBus {
    private static final String TAG = TenBus.class.getSimpleName();
    private final Handler mainThread = new Handler(Looper.getMainLooper());
    private static TenBus bus;
    private Bus ottoBus;
    private Peer peer;

    private TenBus(ThreadEnforcer t) {
        ottoBus = new Bus(t);
        peer = null;
    }

    public synchronized static TenBus get() {
        if (bus == null) {
            bus = new TenBus(ThreadEnforcer.ANY);
        }
        return bus;
    }

    public void attach(BluetoothDevice device) throws IOException {
        if (peer == null) {
            // need to instantiate a new peer
            if (device == null) {
                peer = new SlavePeer();
            } else {
                peer = new MasterPeer();
            }
        }
        peer.obtainConnection(device);
    }

    public void register(final Object subscriber) {
        ottoBus.register(subscriber);
    }

    public void detach() {
        if (peer == null) {
            Log.e(TAG, "Cannot detach if not attached!");
            return;
        }

        peer.close();
        peer = null;
    }

    private void logEvent(final Object event) {
        if (Config.DEBUG) {
            Log.d(event.getClass().getSimpleName(), event.toString());
        }
    }

    void postInternal(final Object event) {
        logEvent(event);

        if (Looper.myLooper() == Looper.getMainLooper()) {
            ottoBus.post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    ottoBus.post(event);
                }
            });
        }
    }

    private void postNet(final NetEvent event) throws IOException {
        logEvent(event);

        if (peer == null) {
            Log.e(TAG, "Cannot send NetEvent if no device is attached!");
            throw new IOException();
        }
        peer.sendMessage(event);
    }

    public void post(final InternalEvent e) {
        postInternal(e);
    }

    public void post(final NetEvent e) {
        try {
            postNet(e);
        } catch (IOException ex) {
            String msg = "IO error on posting event " + e.toString();
            Log.e(TAG, msg);
            postInternal(EventFactory.btError(null, msg));
        }
    }
}

abstract class Peer {
    /**
     * @param device can be `null` in case of slave
     * @throws IOException
     */
    public abstract void obtainConnection(BluetoothDevice device) throws IOException;

    public abstract void close();

    public abstract void sendMessage(NetEvent netEvent) throws IOException;
}

class MasterPeer extends Peer {

    private Map<String, BTMasterThread> threadMap;
    private static final String TAG = MasterPeer.class.getSimpleName();

    public MasterPeer() {
        super();
        threadMap = new HashMap<>();
        TenBus.get().register(this);
    }

    @Subscribe
    public void onDeviceDisconnected(BTDisconnectedEvent event) {
        BTMasterThread removed = threadMap.remove(event.getDevice().getAddress());
        if (removed == null) {
            Log.w(TAG, "Non existing thread removed: " + event.getDevice().getName());
        }
    }

    @Override
    public void obtainConnection(BluetoothDevice device) throws IOException {
        if (threadMap.containsKey(device.getAddress())) {
            Log.w(TAG, "Already connected to " + device.getName());
            return;
        }
        BTMasterThread btMasterThread = new BTMasterThread(device);
        btMasterThread.start();
        threadMap.put(device.getAddress(), btMasterThread);
    }

    @Override
    public void close() {
        for (Map.Entry<String, BTMasterThread> btMasterThread : threadMap.entrySet()) {
            btMasterThread.getValue().deactivate();
        }
    }

    @Override
    public void sendMessage(NetEvent netEvent) throws IOException {
        for (Map.Entry<String, BTMasterThread> btMasterThread : threadMap.entrySet()) {
            btMasterThread.getValue().write(netEvent);
        }
    }
}

class SlavePeer extends Peer {

    private BTSlaveThread btSlaveThread;

    public SlavePeer() {
        super();
    }

    /**
     * In this case device is not used. It can be passed as `null`.
     *
     * @param device can be `null` in case of slave
     * @throws IOException
     */
    @Override
    public void obtainConnection(BluetoothDevice device) throws IOException {
        btSlaveThread = new BTSlaveThread();
        btSlaveThread.start();
    }

    @Override
    public void close() {
        if (btSlaveThread != null) {
            btSlaveThread.deactivate();
        }
    }

    @Override
    public void sendMessage(NetEvent netEvent) throws IOException {
        btSlaveThread.write(netEvent);
    }
}


abstract class BTThread extends Thread {

    private static final String TAG = "BTThread";

    protected BluetoothSocket mmSocket = null;
    private ObjectInputStream mmIn = null;
    private ObjectOutputStream mmOut = null;
    private TenBus bus = TenBus.get();
    private boolean active = true;

    public abstract BluetoothSocket pair() throws IOException;

    private synchronized boolean isActive() {
        return active;
    }

    public synchronized void deactivate() {
        this.active = false;
        try {
            // Closing the input stream to break the while and stop the thread
            mmIn.close();
        } catch (IOException | NullPointerException e) {
            Log.e(TAG, "Cannot close input stream on deactivate()", e);
        }
    }

    public void run() {
        try {
            mmSocket = pair();
            mmOut = new ObjectOutputStream(mmSocket.getOutputStream());
            mmOut.flush();
            mmIn = new ObjectInputStream(mmSocket.getInputStream());
        } catch (IOException e) {
            TenBus.get().post(EventFactory.btError(null, "Cannot pair devices"));
            cancel();
            return;
        }
        // Setting the thread name
        setName(TAG + ":" + mmSocket.getRemoteDevice().getName());

        while (isActive()) {
            try {
                NetEvent netEvent = (NetEvent) mmIn.readObject();
                // NOTE: the only case in which a NetEvent is posted
                // as internal. If not, an infinite loop would start!
                bus.postInternal(netEvent);
            } catch (IOException e) {
                Log.e(TAG, "Disconnected while listening", e);
                TenBus.get().post(EventFactory.btError(null, "Disconnected while listening"));
                break;
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Cannot deserialize incoming event", e);
                TenBus.get().post(EventFactory.btError(null, "Cannot deserialize incoming event"));
                break;
            }
        }
        cancel();
    }

    public void write(NetEvent netEvent) throws IOException {
        mmOut.writeObject(netEvent);
        mmOut.flush();
    }

    private void cancel() {
        try {
            if (mmSocket.isConnected()) {
                mmOut.close();
                mmSocket.close();
                mmIn.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Cannot close socket connection", e);
        } finally {
            TenBus.get().post(EventFactory.btDisconnected(mmSocket.getRemoteDevice()));
            mmSocket = null;
            mmIn = null;
            mmOut = null;
        }
    }
}

class BTMasterThread extends BTThread {
    private static final String TAG = BTMasterThread.class.getSimpleName();

    public BTMasterThread(BluetoothDevice device) throws IOException {
        mmSocket = device.createRfcommSocketToServiceRecord(
                UUID.fromString(Config.MY_SALT_SECURE + device.getAddress().replace(":", "")));
    }

    @Override
    public BluetoothSocket pair() throws IOException {
        Log.i(TAG, "Pairing devices");
        // Always cancel discovery because it will slow down a connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        // Make a connection to the BluetoothSocket:
        // This is a blocking call and will only return on a
        // successful connection or an exception
        TenBus.get().post(EventFactory.btConnecting(mmSocket.getRemoteDevice()));
        mmSocket.connect();
        TenBus.get().post(EventFactory.btConnected(mmSocket.getRemoteDevice()));
        return mmSocket;
    }
}

class BTSlaveThread extends BTThread {

    private BluetoothServerSocket mmServerSocket;

    @Override
    public BluetoothSocket pair() throws IOException {
        String address = BluetoothAdapter.getDefaultAdapter().getAddress();
        mmServerSocket = BluetoothAdapter.getDefaultAdapter()
                .listenUsingRfcommWithServiceRecord(Config.NAME_SECURE,
                        UUID.fromString(Config.MY_SALT_SECURE + address.replace(":", "")));
        TenBus.get().post(EventFactory.btListening(null));
        // Blocking call
        BluetoothSocket s = mmServerSocket.accept();
        mmServerSocket.close();
        mmServerSocket = null;
        TenBus.get().post(EventFactory.btConnected(s.getRemoteDevice()));
        return s;
    }
}