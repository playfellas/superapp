package it.playfellas.superapp.network;

import android.bluetooth.BluetoothDevice;

import java.io.IOException;

import it.playfellas.superapp.events.NetEvent;

/**
 * Created by affo on 28/07/15.
 */
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
