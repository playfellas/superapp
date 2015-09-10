package it.playfellas.superapp.ui.master.bluetoothui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.MainActivity;
import it.playfellas.superapp.ui.PreferenceKeys;
import it.playfellas.superapp.ui.master.MasterActivity;

/**
 * Created by affo on 10/09/15.
 */
public class FastStartActivity extends ImmersiveAppCompatActivity {
    private String[] addresses = new String[InternalConfig.MAX_NO_PLAYERS];
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private boolean reset = false;
    private TextView[] playersText = new TextView[InternalConfig.MAX_NO_PLAYERS];

    private SharedPreferences prefs;

    @Bind(R.id.faststart_resetButton)
    Button resetButton;
    @Bind(R.id.faststart_player1)
    TextView player1Text;
    @Bind(R.id.faststart_player2)
    TextView player2Text;
    @Bind(R.id.faststart_player3)
    TextView player3Text;
    @Bind(R.id.faststart_player4)
    TextView player4Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.faststart_activity);
        ButterKnife.bind(this);

        prefs = getSharedPreferences(getString(R.string.preference_key_app), Context.MODE_PRIVATE);

        addresses[0] = prefs.getString(PreferenceKeys.APP_PLAYER1, null);
        addresses[1] = prefs.getString(PreferenceKeys.APP_PLAYER2, null);
        addresses[2] = prefs.getString(PreferenceKeys.APP_PLAYER3, null);
        addresses[3] = prefs.getString(PreferenceKeys.APP_PLAYER4, null);

        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] != null) {
                BluetoothDevice device = adapter.getRemoteDevice(addresses[i]);
                pair(device, InternalConfig.MAX_BT_CONNECTION_RETRY);
                playersText[i].setText("Connesso a " + device.getName());
            }

            synchronized (this) {
                if (reset) {
                    startActivity(new Intent(this, MainActivity.class));
                    return;
                }
            }
        }

        startActivity(new Intent(this, MasterActivity.class));
    }

    private void pair(BluetoothDevice device, int limit) {
        if (limit <= 0) {
            return;
        }

        try {
            TenBus.get().attach(device);
        } catch (IOException e) {
            // connection failed...
            // retry.
            String toast = "Tentativo di connessione numero " + limit + " con " + device.getName() + " fallito. Riprovo...";
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
            boolean rstCopy; // avoid infinite locks with recursion
            synchronized (this) {
                rstCopy = reset;
            }
            if (!rstCopy) {
                pair(device, limit - 1);
            }
        }
    }

    @OnClick(R.id.faststart_resetButton)
    public synchronized void onReset(View view) {
        prefs.edit().remove(PreferenceKeys.APP_MASTER).apply();
        this.reset = true;
    }

}
