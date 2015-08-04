package it.playfellas.superapp.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.events.EventFactory;

/**
 * Created by affo on 28/07/15.
 */
class BTSlaveThread extends BTThread {

    private BluetoothServerSocket mmServerSocket;

    @Override
    public BluetoothSocket pair() throws IOException {
        String address = BluetoothAdapter.getDefaultAdapter().getAddress();
        mmServerSocket = BluetoothAdapter.getDefaultAdapter()
                .listenUsingRfcommWithServiceRecord(InternalConfig.BT_APP_NAME_SECURE,
                        UUID.fromString(InternalConfig.BT_MY_SALT_SECURE + address.replace(":", "")));
        TenBus.get().post(EventFactory.btListening(null));
        // Blocking call
        BluetoothSocket s = mmServerSocket.accept();
        mmServerSocket.close();
        mmServerSocket = null;
        TenBus.get().post(EventFactory.btConnected(s.getRemoteDevice()));
        return s;
    }
}
