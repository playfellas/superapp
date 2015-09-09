package it.playfellas.superapp.ui.master;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by Stefano Cappa on 09/09/15.
 */
public class BTConnectedDevices {

    @Getter
    private List<BluetoothDevice> connectedDevices;

    private static BTConnectedDevices instance = new BTConnectedDevices();

    private BTConnectedDevices() {
        connectedDevices = new ArrayList<>();
    }

    /**
     * Metodo che permette di ottenere l'istanza della classe.
     *
     * @return istanza della classe.
     */
    public static BTConnectedDevices get() {
        return instance;
    }

}
