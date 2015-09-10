package it.playfellas.superapp.ui.master.bluetoothui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.MasterActivity;
import lombok.Getter;

public class BluetoothActivity extends ImmersiveAppCompatActivity implements
        BTConnectedRecyclerViewAdapter.ItemClickListener,
        BTPairedRecyclerViewAdapter.ItemClickListener,
        BTNewRecyclerViewAdapter.ItemClickListener{

    private static final String TAG = BluetoothActivity.class.getSimpleName();

    @Bind(R.id.gameSelectorButton)
    Button gameSelectorButton;

    @Bind(R.id.connectedDevicesRecyclerView)
    RecyclerView connectedDevicesRecyclerView;
    @Bind(R.id.pairedDevicesRecyclerView)
    RecyclerView pairedDevicesRecyclerView;
    @Bind(R.id.newDevicesDevicesRecyclerView)
    RecyclerView newDevicesDevicesRecyclerView;
    @Bind(R.id.button_scan)
    Button scanButton;

    private BluetoothAdapter mBtAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;

    @Getter
    private BTConnectedRecyclerViewAdapter connectedAdapter;
    @Getter
    private BTNewRecyclerViewAdapter newAdapter;
    @Getter
    private BTPairedRecyclerViewAdapter pairedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());

        setContentView(R.layout.activity_bluetooth);

        ButterKnife.bind(this);
        TenBus.get().register(this);

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
        //TODO create adapters for new and paired

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        connectedDevicesRecyclerView.setLayoutManager(mLayoutManager);
        // allows for optimizations if all item views are of the same size:
        connectedDevicesRecyclerView.setHasFixedSize(true);
        connectedAdapter = new BTConnectedRecyclerViewAdapter(this);
        connectedDevicesRecyclerView.setAdapter(connectedAdapter);
        connectedDevicesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        newDevicesDevicesRecyclerView.setLayoutManager(mLayoutManager2);
        // allows for optimizations if all item views are of the same size:
        newDevicesDevicesRecyclerView.setHasFixedSize(true);
        newAdapter = new BTNewRecyclerViewAdapter(this);
        newDevicesDevicesRecyclerView.setAdapter(newAdapter);
        newDevicesDevicesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(this);
        mLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        pairedDevicesRecyclerView.setLayoutManager(mLayoutManager3);
        // allows for optimizations if all item views are of the same size:
        pairedDevicesRecyclerView.setHasFixedSize(true);
        pairedAdapter = new BTPairedRecyclerViewAdapter(this);
        pairedDevicesRecyclerView.setAdapter(pairedAdapter);
        pairedDevicesRecyclerView.setItemAnimator(new DefaultItemAnimator());


        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
//        ArrayAdapter<String> pairedDevicesArrayAdapter =
//                new ArrayAdapter<>(this, R.layout.device_name);
//
//        // Find and set up the ListView for paired devices
//        pairedDevicesRecyclerView.setAdapter(pairedDevicesArrayAdapter);
//        pairedDevicesRecyclerView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
//        newDevicesDevicesRecyclerView.setAdapter(mNewDevicesArrayAdapter);
//        newDevicesDevicesRecyclerView.setOnItemClickListener(mDeviceClickListener);

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

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
//            titlePairedDevices.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedAdapter.getPairedDevices().add(device);
            }
        } else {
//            String noDevices = getResources().getText(R.string.none_paired).toString();
//            pairedDevicesArrayAdapter.add(noDevices);
            Log.d(TAG, "No paired devices");
        }


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

    @Subscribe
    public void onBTConnectedEvent(BTConnectedEvent event) {
        int positionBefore = connectedAdapter.getConnectedDevices().size();
        connectedAdapter.getConnectedDevices().add(event.getDevice());
        connectedAdapter.notifyItemInserted(positionBefore);
    }

    @Subscribe
    public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
        connectedAdapter.getConnectedDevices().remove(event.getDevice());
        connectedAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.gameSelectorButton)
    public void selectGame() {
        Intent intent = new Intent(this, MasterActivity.class);
        startActivity(intent);
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                //Indicates the remote device is bonded (paired).
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    pairedAdapter.notifyDataSetChanged();
//                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
//                if (mNewDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = getResources().getText(R.string.none_found).toString();
//                    mNewDevicesArrayAdapter.add(noDevices);
//                }
            }
        }
    };

//    /**
//     * The on-click listener for all devices in the ListViews
//     */
//    private AdapterView.OnItemClickListener mDeviceClickListener
//            = new AdapterView.OnItemClickListener() {
//        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
//            // Cancel discovery because it's costly and we're about to connect
//            mBtAdapter.cancelDiscovery();
//
//            // Get the device MAC address, which is the last 17 chars in the View
//            String info = ((TextView) v).getText().toString();
//            String address = info.substring(info.length() - 17);
//
//            connectDevice(address);
//        }
//    };


    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }


    @Override
    public void itemClicked(View view) {
        Log.d(TAG, "Item clicked");
    }

    @Override
    public void disconnectButtonClicked(BluetoothDevice deviceToDisconnect) {
        Log.d(TAG, "Disconnect button clicked");
        //TenBus.get()   TODO i need a method to disconnect a specific device from TenBus
    }

    @Override
    public void connectToPaired(BluetoothDevice device) {
        mBtAdapter.cancelDiscovery();
        connectDevice(device.getAddress());
        pairedAdapter.getPairedDevices().remove(device);
        pairedAdapter.notifyDataSetChanged();
    }
}
