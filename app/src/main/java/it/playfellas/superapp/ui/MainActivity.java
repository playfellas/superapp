package it.playfellas.superapp.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.master.bluetoothui.BluetoothActivity;
import it.playfellas.superapp.ui.master.bluetoothui.FastStartActivity;
import it.playfellas.superapp.ui.slave.SlaveActivity;

public class MainActivity extends ImmersiveAppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter mBluetoothAdapter = null;
    private SharedPreferences prefs;

    @Bind(R.id.masterButton)
    Button masterButton;

    @Bind(R.id.slaveButton)
    Button slaveButton;

    @Override
    public void onStart() {
        super.onStart();
        // If BT is disabled, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        checkBluetooth();

        prefs = getSharedPreferences(getString(R.string.preference_key_app), Context.MODE_PRIVATE);

        if (prefs.contains(PreferenceKeys.APP_MASTER)) {
            if (prefs.getBoolean(PreferenceKeys.APP_MASTER, false)) {
                // I was a master
                startActivity(new Intent(this, FastStartActivity.class));
            }
        }
        // nothing was set before, go on as nothing has happened
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "BlueTooth enabled -> everything is ok!");
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BlueTooth not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void checkBluetooth() {
        // If the adapter is null, then Bluetooth is not supported
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponibile", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @OnClick(R.id.masterButton)
    public void onClikMasterButton(View view) {
        prefs.edit().putBoolean(PreferenceKeys.APP_MASTER, true).apply();
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.slaveButton)
    public void onClikSlaveButton(View view) {
        prefs.edit().putBoolean(PreferenceKeys.APP_MASTER, false).apply();
        Intent intent = new Intent(this, SlaveActivity.class);
        startActivity(intent);
    }
}
