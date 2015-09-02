package it.playfellas.superapp.events.game;

import android.bluetooth.BluetoothDevice;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.logic.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 02/09/15.
 */
public class YourTurnEvent extends NetEvent {
    @Getter
    String playerAddress;
    @Getter
    Tile[] stack;

    public YourTurnEvent(BluetoothDevice player, Tile[] stack) {
        this.stack = stack;
        this.playerAddress = player.getAddress();
    }
}
