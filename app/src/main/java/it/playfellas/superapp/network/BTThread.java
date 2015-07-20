package it.playfellas.superapp.network;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.squareup.otto.Bus;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.utils.NineBus;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class BTThread extends Thread {

  private static final String TAG = "BTMasterThread";

  protected BluetoothSocket mmSocket = null;
  private ObjectInputStream mmIn = null;
  private ObjectOutputStream mmOut = null;
  private Bus bus = NineBus.get();
  private boolean active = true;

  public abstract BluetoothSocket pair() throws IOException;

  private synchronized boolean isActive() {
    return active;
  }

  private synchronized void deactivate(){
    this.active = false;
  }

  public void run() {
    try {
      mmSocket = pair();
      mmOut = new ObjectOutputStream(mmSocket.getOutputStream());
      mmOut.flush();
      mmIn = new ObjectInputStream(mmSocket.getInputStream());
    } catch (IOException e) {
      NineBus.get().post(EventFactory.btError(null, "Cannot pair devices"));
      cancel();
      return;
    }

    while (isActive()) {
      try {
        NetEvent netEvent = (NetEvent) mmIn.readObject();
        bus.post(netEvent);
      } catch (IOException e) {
        Log.e(TAG, "Disconnected while listening", e);
        NineBus.get().post(EventFactory.btError(null, "Disconnected while listening"));
        break;
      } catch (ClassNotFoundException e) {
        Log.e(TAG, "Cannot deserialize incoming event", e);
        NineBus.get().post(EventFactory.btError(null, "Cannot deserialize incoming event"));
        break;
      }
    }
    //TODO: check that the thread really terminates onDestroy
    cancel();
  }

  public void write(NetEvent netEvent) throws IOException {
    mmOut.writeObject(netEvent);
    mmOut.flush();
  }

  public void cancel() {
    try {
      if (mmSocket != null) {
        mmOut.close();
        mmIn.close();
        mmSocket.close();
      }
    } catch (IOException e) {
      Log.e(TAG, "Cannot close socket connection", e);
    } finally {
      NineBus.get().post(EventFactory.btDisconnected(mmSocket.getRemoteDevice()));
      mmSocket = null;
      mmIn = null;
      mmOut = null;
    }

    deactivate();
  }
}
