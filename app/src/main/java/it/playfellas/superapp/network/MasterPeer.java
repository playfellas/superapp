package it.playfellas.superapp.network;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import it.playfellas.superapp.events.BTDisconnectedEvent;
import it.playfellas.superapp.events.NetEvent;

/**
 * Created by affo on 28/07/15.
 */
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
