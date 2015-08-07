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
import it.playfellas.superapp.events.game.StartGame1Color;
import it.playfellas.superapp.events.game.StartGame1Direction;
import it.playfellas.superapp.events.game.StartGame1Shape;
import it.playfellas.superapp.events.game.StartGame2Event;
import it.playfellas.superapp.events.game.StartGame3Event;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.ui.ScoreUpdateEvent;
import it.playfellas.superapp.events.ui.UIBeginStageEvent;
import it.playfellas.superapp.events.ui.UIEndStageEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.Config1Color;
import it.playfellas.superapp.logic.Config1Direction;
import it.playfellas.superapp.logic.Config1Shape;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.tiles.Tile;

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

    public static PhotoEvent sendPhotoByteArray(byte[] b) {
        return new PhotoEvent(b);
    }

    public static StartGame1Color startGame1Color(Config1Color conf) {
        return new StartGame1Color(conf);
    }

    public static StartGame1Direction startGame1Direction(Config1Direction conf) {
        return new StartGame1Direction(conf);
    }

    public static StartGame1Shape startGame1Shape(Config1Shape conf) {
        return new StartGame1Shape(conf);
    }

    public static StartGame2Event startGame2(Config2 conf) {
        return new StartGame2Event(conf);
    }

    public static StartGame3Event startGame3(Config3 conf) {
        return new StartGame3Event(conf);
    }

    public static EndGameEvent endGame() {
        return new EndGameEvent();
    }

    // UI

    public static UIBeginStageEvent uiBeginStage(int stage) {
        return new UIBeginStageEvent(stage);
    }

    public static UIEndStageEvent uiEndStage(int stage) {
        return new UIEndStageEvent(stage);
    }

    public static UIRWEvent uiRWEvent(boolean right) {
        return new UIRWEvent(right);
    }

    public static ScoreUpdateEvent scoreUpdate(int score) {
        return new ScoreUpdateEvent(score);
    }
}

