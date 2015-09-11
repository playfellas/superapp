package it.playfellas.superapp.ui.master.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.FastStartPreferences;
import it.playfellas.superapp.ui.master.MasterActivity;

/**
 * Created by affo on 10/09/15.
 */
public class FastStartActivity extends ImmersiveAppCompatActivity {
    private static final String TAG = FastStartActivity.class.getSimpleName();
    private HashMap<String, TextView> playersText = new HashMap<>();
    private PairTask pairing;

    @Bind(R.id.faststart_exitButton)
    Button exitButton;
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

        String[] addresses = FastStartPreferences.getPlayers(this);

        int nullCount = 0;
        for (String a : addresses) {
            if (a == null) {
                nullCount++;
            }
        }
        if (nullCount == addresses.length) {
            // every address is set to null...
            // enter addresses please...
            startActivity(new Intent(this, BluetoothActivity.class));
            return;
        }

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

    private void unExit() {
        exitButton.setEnabled(false);
    }

    private void onEndPairing(boolean error) {
        // a small pause
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted while sleeping.");
        }

        if (error) {
            startActivity(new Intent(this, BluetoothActivity.class));
        } else {
            startActivity(new Intent(this, MasterActivity.class));
        }
    }

    @OnClick(R.id.faststart_exitButton)
    public void onExit(View view) {
        pairing.cancel(true);
        onEndPairing(true);
    }

    private class PairTask extends AsyncTask<String, Void, Void> {
        private BluetoothAdapter adapter;
        private boolean error;
        private Semaphore s;

        private void updateUI(final String address, final String content) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateText(address, content);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.error = false;
            this.s = new Semaphore(0);
            this.adapter = BluetoothAdapter.getDefaultAdapter();
            TenBus.get().register(this);
        }

        @Override
        protected Void doInBackground(String... players) {
            // a small pause
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while sleeping.");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    unExit();
                }
            });

            for (String address : players) {
                if (address != null) {
                    BluetoothDevice device = adapter.getRemoteDevice(address);
                    pair(device, InternalConfig.MAX_BT_CONNECTION_RETRY);
                }
            }
            return null;
        }

        private void pair(BluetoothDevice device, int limit) {
            if (limit <= 0 || isCancelled()) {
                return;
            }

            String address = device.getAddress();
            String name = device.getName();
            updateUI(address,
                    "Connessione a " + name + ": tentativo " + (InternalConfig.MAX_BT_CONNECTION_RETRY - limit + 1)
                            + " di " + InternalConfig.MAX_BT_CONNECTION_RETRY);
            // a small pause
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while sleeping.");
            }

            TenBus.get().attach(device);


            // waiting for connection result
            try {
                s.acquire();
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while paring.");
                return;
            }

            if (error) {
                // connection failed...
                // retry.
                pair(device, limit - 1);
            } else {
                updateUI(address, "Connesso a " + name);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onEndPairing(error);
                }
            });
        }

        @Subscribe
        public void onError(BTDisconnectedEvent e) {
            this.error = true;
            s.release();
        }

        @Subscribe
        public void onConnected(BTConnectedEvent e) {
            this.error = false;
            s.release();
        }
    }

}
