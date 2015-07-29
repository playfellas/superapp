package it.playfellas.superapp.events;

import android.bluetooth.BluetoothDevice;

import it.playfellas.superapp.logic.common.slave.TileDispenser;
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

    public static StartGameEvent startGame() {
        return new StartGameEvent();
    }

    public static EndGameEvent endGame() {
        return new EndGameEvent();
    }
}

