package it.playfellas.superapp.events;

import android.bluetooth.BluetoothAdapter;
import java.io.Serializable;

public abstract class NetEvent implements Serializable {
  protected final String myDeviceName = BluetoothAdapter.getDefaultAdapter().getName();
}
