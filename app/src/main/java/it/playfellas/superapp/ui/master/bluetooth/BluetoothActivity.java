package it.playfellas.superapp.ui.master.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.PreferenceKeys;
import it.playfellas.superapp.ui.master.MasterActivity;
import lombok.Getter;

public class BluetoothActivity extends ImmersiveAppCompatActivity implements
        BTPairedRecyclerViewAdapter.ItemClickListener,
        BTNewRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = BluetoothActivity.class.getSimpleName();

    private static final int UP = 0;
    private static final int RIGHT = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;

    @Bind(R.id.gameSelectorButton)
    Button gameSelectorButton;

    //BORDER BUTTONS
    @Bind(R.id.upButton)
    Button upButton;
    @Bind(R.id.rightButton)
    Button rightButton;
    @Bind(R.id.downButton)
    Button downButton;
    @Bind(R.id.leftButton)
    Button leftButton;

    @Bind(R.id.pairedDevicesRecyclerView)
    RecyclerView pairedDevicesRecyclerView;
    @Bind(R.id.newDevicesDevicesRecyclerView)
    RecyclerView newDevicesDevicesRecyclerView;
    @Bind(R.id.button_scan)
    Button scanButton;

    private BluetoothAdapter mBtAdapter;
    private BluetoothAdapter mBluetoothAdapter;

    @Getter
    private BTNewRecyclerViewAdapter newAdapter;
    @Getter
    private BTPairedRecyclerViewAdapter pairedAdapter;

    private List<BluetoothDevice> connectedDevices;
    private Button[] buttons = new Button[4];

    private SharedPreferences prefs;
    private String[] playersPrefs = {
            PreferenceKeys.APP_PLAYER1,
            PreferenceKeys.APP_PLAYER2,
            PreferenceKeys.APP_PLAYER3,
            PreferenceKeys.APP_PLAYER4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.activity_bluetooth);
        super.setKeepAwake();
        ButterKnife.bind(this);
        TenBus.get().register(this);

        connectedDevices = new ArrayList<>();

        buttons[0] = upButton;
        buttons[1] = rightButton;
        buttons[2] = downButton;
        buttons[3] = leftButton;

        // clear player preferences
        prefs = getSharedPreferences(getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        for (String playersPref : playersPrefs) {
            prefs.edit().remove(playersPref).apply();
        }

        //i created mBluetoothAdapter in MainActivity, but i need also here this object.
        //Indeed, i get the same instance with this static call.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //devicelist
        // Initialize the button to perform device discovery
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });


        //init recyclerviews and adapters
        newDevicesDevicesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        newDevicesDevicesRecyclerView.setHasFixedSize(true);
        newAdapter = new BTNewRecyclerViewAdapter(this);
        newDevicesDevicesRecyclerView.setAdapter(newAdapter);
        newDevicesDevicesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        pairedDevicesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pairedDevicesRecyclerView.setHasFixedSize(true);
        pairedAdapter = new BTPairedRecyclerViewAdapter(this);
        pairedDevicesRecyclerView.setAdapter(pairedAdapter);
        pairedDevicesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedAdapter.getPairedDevices().add(device);
            }
        }
    }

    private void saveDevice(BluetoothDevice device) {
        prefs.edit().putString(playersPrefs[this.connectedDevices.size() - 1], device.getAddress()).apply();
    }

    private void updateLeftButton() {
        // rotate the leftButton
        RotateAnimation leftAnim = new RotateAnimation(0, +90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        leftAnim.setFillAfter(true);
        leftAnim.setDuration(0);
        leftButton.startAnimation(leftAnim);
        leftButton.setTranslationY(550);
    }

    private void updateRightButton() {
        // rotate the rightButton
        RotateAnimation rightAnim = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rightAnim.setFillAfter(true);
        rightAnim.setDuration(0);
        rightButton.startAnimation(rightAnim);
        rightButton.setTranslationY(550);
    }

    private void updatedBorderButtonState(String deviceName) {
        buttons[this.connectedDevices.size() - 1].setEnabled(false);
        buttons[this.connectedDevices.size() - 1].setText(deviceName);

        switch (this.connectedDevices.size() - 1) {
            case RIGHT:
                this.updateRightButton();
                break;
            case LEFT:
                this.updateLeftButton();
                break;
            case UP:
            case DOWN:
            default:
        }

        buttons[this.connectedDevices.size() - 1].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    @OnClick(R.id.gameSelectorButton)
    public void selectGame() {
        startActivity(new Intent(this, MasterActivity.class));
    }

    @Override
    public void connectToPaired(BluetoothDevice device) {
        mBtAdapter.cancelDiscovery();
        connectDevice(device.getAddress());
    }


    /**
     * The BroadcastReceiver that listens for discovered devices
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                // When discovery finds a device
                case BluetoothDevice.ACTION_FOUND:
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // If it's already paired, skip it, because it's been listed already
                    //Indicates the remote device is bonded (paired).
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        int positionToAdd = newAdapter.getNewDiscoveredDevices().size();
                        newAdapter.getNewDiscoveredDevices().add(device);
                        newAdapter.notifyItemInserted(positionToAdd);
                    }
                    break;
                //When discovery completes
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d(TAG, "Discovery completed!");
                    break;
            }
        }
    };

    private void doDiscovery() {
        Log.d(TAG, "Discovery started!");

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    /**
     * Establish connection with other devices
     */
    private void connectDevice(String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            TenBus.get().attach(device);
        } catch (IOException e) {
            Toast.makeText(this, "Connect error", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onBTConnectedEvent(BTConnectedEvent event) {
        //remove from the paired devices
        int positionToRemove = pairedAdapter.getPairedDevices().indexOf(event.getDevice());
        pairedAdapter.getPairedDevices().remove(event.getDevice());
        pairedAdapter.notifyItemRemoved(positionToRemove);

        //add device to the connected lists
        this.connectedDevices.add(event.getDevice());

        //save into preferences
        this.saveDevice(event.getDevice());

        //set the correct border-button up/down/left or right.
        //not only the visibility but also the device name inside
        this.updatedBorderButtonState(event.getDevice().getName());
    }

    @Subscribe
    public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
        //add device to the connected lists
        this.connectedDevices.remove(event.getDevice());
    }
}
