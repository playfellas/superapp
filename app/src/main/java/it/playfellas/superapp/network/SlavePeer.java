package it.playfellas.superapp.network;

import it.playfellas.superapp.events.NetEvent;
import java.io.IOException;

public class SlavePeer {

  private BTSlaveThread btSlaveThread;
  private static SlavePeer slave;

  private SlavePeer() {
    super();
  }

  public synchronized static SlavePeer get() {
    if (slave == null) {
      slave = new SlavePeer();
    }
    return slave;
  }

  public void listenForConnection() throws IOException {
    btSlaveThread = new BTSlaveThread();
    btSlaveThread.start();
  }

  public void close() {
    btSlaveThread.cancel();
  }

  public void sendMessage(NetEvent netEvent) throws IOException {
    btSlaveThread.write(netEvent);
  }
}
