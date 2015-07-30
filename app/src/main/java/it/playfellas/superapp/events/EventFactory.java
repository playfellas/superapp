package it.playfellas.superapp.events;

import android.bluetooth.BluetoothDevice;

import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTConnectingEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.events.bt.BTErrorEvent;
import it.playfellas.superapp.events.bt.BTListeningEvent;
import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.RWEvent;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.common.Config;
import it.playfellas.superapp.logic.common.slave.SlaveController;
import it.playfellas.superapp.logic.common.tiles.Tile;

public class EventFactory {
    public static StringNetEvent stringEvent(String body) {
        return new StringNetEvent(body);
    }

    public static BTDisconnectedEvent btDisconnected(BluetoothDevice device) {
        return new BTDisconnectedEvent(device);
    }

    public static BTConnectedEvent btConnected(BluetoothDevice device) {
        return new BTConnectedEvent(device);
    }

    public static BTListeningEvent btListening(BluetoothDevice device) {
        return new BTListeningEvent(device);
    }

    public static BTConnectingEvent btConnecting(BluetoothDevice device) {
        return new BTConnectingEvent(device);
    }

    public static BTErrorEvent btError(BluetoothDevice device, String msg) {
        return new BTErrorEvent(device, msg);
    }

    public static NewTileEvent newTile(Tile t) {
        return new NewTileEvent(t);
    }

    public static ClickedTileEvent clickedTile(Tile t) {
        return new ClickedTileEvent(t);
    }

    public static ToggleGameModeEvent gameChange() {
        return new ToggleGameModeEvent();
    }

    public static RWEvent rw(boolean right) {
        return new RWEvent(right);
    }

    public static RTTUpdateEvent rttUpdate(float rtt) {
        return new RTTUpdateEvent(rtt);
    }

    public static BeginStageEvent beginStage() {
        return new BeginStageEvent();
    }

    public static EndStageEvent endStage() {
        return new EndStageEvent();
    }

    public static StartGameEvent startGame(Class<SlaveController> sc, Config conf) {
        return new StartGameEvent(sc, conf);
    }

    public static EndGameEvent endGame() {
        return new EndGameEvent();
    }
}

