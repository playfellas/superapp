package it.playfellas.superapp.network;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.events.NetEvent;

/**
 * Created by affo on 28/07/15.
 */
class MasterPeer extends Peer {

    private Map<String, BTMasterThread> threadMap;
    private static final String TAG = MasterPeer.class.getSimpleName();
    private int iterationStep;

    public MasterPeer() {
        super();
        threadMap = new LinkedHashMap<>(); // preserve insert order
        iterationStep = 0;
        TenBus.get().register(this);
    }

    @Subscribe
    public synchronized void onDeviceDisconnected(BTDisconnectedEvent event) {
        BTMasterThread removed = threadMap.remove(event.getDevice().getAddress());
        if (removed == null) {
            Log.w(TAG, "Non existing thread removed: " + event.getDevice().getName());
        }

        int size = threadMap.size();
        if (iterationStep >= size) {
            iterationStep = size - 1;
        }
    }

    @Override
    public synchronized void obtainConnection(BluetoothDevice device) throws IOException {
        if (threadMap.containsKey(device.getAddress())) {
            Log.w(TAG, "Already connected to " + device.getName());
            return;
        }
        BTMasterThread btMasterThread = new BTMasterThread(device);
        btMasterThread.start();
        threadMap.put(device.getAddress(), btMasterThread);
    }

    @Override
    public synchronized void close() {
        for (Map.Entry<String, BTMasterThread> btMasterThread : threadMap.entrySet()) {
            btMasterThread.getValue().deactivate();
        }
    }

    @Override
    public synchronized void sendMessage(NetEvent netEvent) throws IOException {
        for (Map.Entry<String, BTMasterThread> btMasterThread : threadMap.entrySet()) {
            btMasterThread.getValue().write(netEvent);
        }
    }

    @Override
    public synchronized int noDevices() {
        return threadMap.size();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public synchronized BluetoothDevice next() {
        BluetoothDevice dev = (new ArrayList<>(threadMap.values())).get(iterationStep).getDevice();
        iterationStep++;
        int size = threadMap.size();
        if (iterationStep >= size) {
            iterationStep = 0; // circularity
        }
        return dev;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
