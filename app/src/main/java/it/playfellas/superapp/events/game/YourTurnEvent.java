package it.playfellas.superapp.events.game;

import android.bluetooth.BluetoothDevice;

import it.playfellas.superapp.events.NetEvent;
import lombok.Getter;

/**
 * Created by affo on 02/09/15.
 */
public class YourTurnEvent extends NetEvent {
    @Getter
    String playerAddress;

    public YourTurnEvent(BluetoothDevice player) {
        this.playerAddress = player.getAddress();
    }
}
