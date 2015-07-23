package it.playfellas.superapp.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.utils.Config;
import it.playfellas.superapp.utils.NineBus;
import java.io.IOException;
import java.util.UUID;

public class BTSlaveThread extends BTThread {

  private BluetoothServerSocket mmServerSocket;

  @Override public BluetoothSocket pair() throws IOException {
    String address = BluetoothAdapter.getDefaultAdapter().getAddress();
    mmServerSocket = BluetoothAdapter.getDefaultAdapter()
        .listenUsingRfcommWithServiceRecord(Config.NAME_SECURE,
            UUID.fromString(Config.MY_SALT_SECURE + address.replace(":", "")));
    NineBus.get().post(EventFactory.btListening(null));
    // Blocking call
    BluetoothSocket s = mmServerSocket.accept();
    mmServerSocket.close();
    mmServerSocket = null;
    NineBus.get().post(EventFactory.btConnected(s.getRemoteDevice()));
    return s;
  }
}
