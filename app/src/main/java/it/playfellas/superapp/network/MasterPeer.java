package it.playfellas.superapp.network;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import com.squareup.otto.Subscribe;
import it.playfellas.superapp.events.BTDisconnectedEvent;
import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.utils.NineBus;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MasterPeer {

  private Map<String, BTMasterThread> threadMap;
  private static MasterPeer master;
  private static final String TAG = MasterPeer.class.getSimpleName();

  private MasterPeer() {
    super();
    threadMap = new HashMap<>();
    NineBus.get().register(this);
  }

  public synchronized static MasterPeer get() {
    if (master == null) {
      master = new MasterPeer();
    }
    return master;
  }

  public void connectDevice(BluetoothDevice device) throws IOException {
    if (threadMap.containsKey(device.getAddress())) {
      Log.w(TAG, "Already connected to " + device.getName());
      return;
    }
    BTMasterThread btMasterThread = new BTMasterThread(device);
    btMasterThread.start();
    threadMap.put(device.getAddress(), btMasterThread);
  }

  @Subscribe public void onDeviceDisconnected(BTDisconnectedEvent event) {
    BTMasterThread removed = threadMap.remove(event.getDevice().getAddress());
    if (removed == null) {
      Log.w(TAG, "Non existing thread removed: " + event.getDevice().getName());
    }
  }

  public void close() {
    for (Map.Entry<String, BTMasterThread> btMasterThread : threadMap.entrySet()) {
      btMasterThread.getValue().cancel();
    }
  }

  public void sendMessage(NetEvent netEvent) throws IOException {
    for (Map.Entry<String, BTMasterThread> btMasterThread : threadMap.entrySet()) {
      btMasterThread.getValue().write(netEvent);
    }
  }
}
