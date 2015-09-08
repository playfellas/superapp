package it.playfellas.superapp.ui.master;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.DeviceListActivity;

public class BluetoothActivity extends ImmersiveAppCompatActivity {

    private static final String TAG = BluetoothActivity.class.getSimpleName();
    private static final int REQUEST_CONNECT_DEVICE = 1;

    @Bind(R.id.addButton)
    Button addButton;
    @Bind(R.id.gameSelectorButton)
    Button gameSelectorButton;
    @Bind(R.id.devicesListView)
    ListView devicesListView;

    private ArrayAdapter<String> devicesAdapter;

    private BluetoothAdapter mBluetoothAdapter = null;

    private int playersCount = 0;

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

        setUI();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONNECT_DEVICE) {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void setUI() {
        devicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        devicesListView.setAdapter(devicesAdapter);
    }

    @Subscribe
    public void onBTConnectedEvent(BTConnectedEvent event) {
        devicesAdapter.add(event.getDevice().getName());
        devicesAdapter.notifyDataSetChanged();
        playersCount++;
    }

    @Subscribe
    public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
        devicesAdapter.remove(event.getDevice().getName());
        devicesAdapter.notifyDataSetChanged();
        playersCount--;
    }

    /**
     * Establish connection with other devices
     *
     * @param data An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     */
    private void connectDevice(Intent data) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            TenBus.get().attach(device);
        } catch (IOException e) {
            Toast.makeText(this, "Connect error", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.addButton)
    public void addPlayer() {
        Intent i = new Intent(this, DeviceListActivity.class);
        startActivityForResult(i, REQUEST_CONNECT_DEVICE);
    }

    @OnClick(R.id.gameSelectorButton)
    public void selectGame() {
        Intent intent = new Intent(this, MasterActivity.class);
        startActivity(intent);
    }

}
