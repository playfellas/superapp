package it.playfellas.superapp.ui.master.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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


    @Bind(R.id.titleTextView)
    TextView titleTextView;

    @Bind(R.id.cardview1)
    CardView cardview1;
    @Bind(R.id.nameTextView1)
    TextView nameTextView1;
    @Bind(R.id.addressTextView1)
    TextView addressTextView1;
    @Bind(R.id.progressBar1)
    ProgressBar progressBar1;
    @Bind(R.id.countdownTextView1)
    TextView countDownTextView1;

    @Bind(R.id.cardview2)
    CardView cardview2;
    @Bind(R.id.nameTextView2)
    TextView nameTextView2;
    @Bind(R.id.addressTextView2)
    TextView addressTextView2;
    @Bind(R.id.progressBar2)
    ProgressBar progressBar2;
    @Bind(R.id.countdownTextView2)
    TextView countDownTextView2;

    @Bind(R.id.cardview3)
    CardView cardview3;
    @Bind(R.id.nameTextView3)
    TextView nameTextView3;
    @Bind(R.id.addressTextView3)
    TextView addressTextView3;
    @Bind(R.id.progressBar3)
    ProgressBar progressBar3;
    @Bind(R.id.countdownTextView3)
    TextView countDownTextView3;

    @Bind(R.id.cardview4)
    CardView cardview4;
    @Bind(R.id.nameTextView4)
    TextView nameTextView4;
    @Bind(R.id.addressTextView4)
    TextView addressTextView4;
    @Bind(R.id.progressBar4)
    ProgressBar progressBar4;
    @Bind(R.id.countdownTextView4)
    TextView countDownTextView4;


    @Bind(R.id.stopConnectionButton)
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

    @OnClick(R.id.stopConnectionButton)
    public void onStopConnection(View view) {
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
