package it.playfellas.superapp.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.utils.Config;

/**
 * Created by affo on 28/07/15.
 */
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
