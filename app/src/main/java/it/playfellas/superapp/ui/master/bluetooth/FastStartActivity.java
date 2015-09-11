package it.playfellas.superapp.ui.master.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTConnectingEvent;
import it.playfellas.superapp.events.bt.BTErrorEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.PreferenceKeys;

/**
 * Created by affo on 10/09/15.
 */
public class FastStartActivity extends ImmersiveAppCompatActivity {
    private static final String TAG = FastStartActivity.class.getSimpleName();
    private String[] addresses = new String[InternalConfig.MAX_NO_PLAYERS];
    private HashMap<String, TextView> playersText = new HashMap<>();
    PairTask pairing;

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
        super.setKeepAwake();
        ButterKnife.bind(this);

        prefs = getSharedPreferences(getString(R.string.preference_key_app), Context.MODE_PRIVATE);

        addresses[0] = prefs.getString(PreferenceKeys.APP_PLAYER1, null);
        addresses[1] = prefs.getString(PreferenceKeys.APP_PLAYER2, null);
        addresses[2] = prefs.getString(PreferenceKeys.APP_PLAYER3, null);
        addresses[3] = prefs.getString(PreferenceKeys.APP_PLAYER4, null);

        playersText.put(addresses[0], player1Text);
        playersText.put(addresses[1], player2Text);
        playersText.put(addresses[2], player3Text);
        playersText.put(addresses[3], player4Text);

        pairing = new PairTask();
        pairing.execute(addresses);
    }

    private void updateText(String address, String content) {
        playersText.get(address).setText(content);
    }

    @OnClick(R.id.faststart_resetButton)
    public void onReset(View view) {
        prefs.edit().remove(PreferenceKeys.APP_MASTER).apply();
        pairing.cancel(true);
    }

    private class PairTask extends AsyncTask<String, Void, Void> {
        private BluetoothAdapter adapter;
        private boolean reset;
        private boolean error;
        private Semaphore s;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.reset = false;
            this.error = false;
            this.s = new Semaphore(0);
            this.adapter = BluetoothAdapter.getDefaultAdapter();
            TenBus.get().register(this);
        }

        @Override
        protected Void doInBackground(String... players) {
            for (String address : players) {
                if (address != null) {
                    BluetoothDevice device = adapter.getRemoteDevice(address);
                    pair(device, InternalConfig.MAX_BT_CONNECTION_RETRY);
                }

                synchronized (this) {
                    if (reset) {
                        return null;
                    }
                }
            }
            return null;
        }

        private void pair(BluetoothDevice device, int limit) {
            if (limit <= 0) {
                return;
            }

            try {
                TenBus.get().attach(device);
            } catch (IOException e) {
                TenBus.get().post(EventFactory.btError(device, "Error in connecting to " + device.getName()));
                return;
            }

            // waiting for connection result
            try {
                s.acquire();
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while paring.");
            }

            if (error) {
                // connection failed...
                // retry.
                boolean rstCopy; // avoid infinite locks with recursion
                synchronized (this) {
                    rstCopy = this.reset;
                }
                if (!rstCopy) {
                    pair(device, limit - 1);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            synchronized (this) {
                this.reset = true;
            }
        }

        @Subscribe
        public void onError(BTErrorEvent e) {
            updateText(e.getDevice().getAddress(), e.toString() + ". Retrying...");
            this.error = true;
            s.release();
        }

        @Subscribe
        public void onConnected(BTConnectedEvent e) {
            String address = e.getDevice().getAddress();
            String name = e.getDevice().getName();
            updateText(address, "Connected to " + name);
            this.error = false;
            s.release();
        }

        @Subscribe
        public void onConnecting(BTConnectingEvent e) {
            String address = e.getDevice().getAddress();
            String name = e.getDevice().getName();
            updateText(address, "Connecting to " + name + "...");
        }
    }

}
