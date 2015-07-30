package it.playfellas.superapp.activities.master;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.otto.Subscribe;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.DeviceListActivity;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import java.io.IOException;

public class BluetoothActivity extends AppCompatActivity {

  private static final String TAG = BluetoothActivity.class.getSimpleName();
  private static final int REQUEST_CONNECT_DEVICE = 1;
  private static final int REQUEST_ENABLE_BT = 2;

  @Bind(R.id.addButton) Button addButton;
  @Bind(R.id.startButton) Button startButton;
  @Bind(R.id.devicesListView) ListView devicesListView;

  private ArrayAdapter<String> devicesAdapter;

  private BluetoothAdapter mBluetoothAdapter = null;

  private int playersCount = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bluetooth);
    ButterKnife.bind(this);
    TenBus.get().register(this);
    checkBluetooth();
    setUI();
  }

  @Override public void onStart() {
    super.onStart();
    // If BT is disabled, request that it be enabled.
    if (!mBluetoothAdapter.isEnabled()) {
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_CONNECT_DEVICE:
        // When DeviceListActivity returns with a device to connect
        if (resultCode == Activity.RESULT_OK) {
          connectDevice(data);
        }
        break;
      case REQUEST_ENABLE_BT:
        // When the request to enable Bluetooth returns
        if (resultCode == Activity.RESULT_OK) {
        } else {
          // User did not enable Bluetooth or an error occurred
          Log.d(TAG, "BT not enabled");
          Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
          finish();
        }
    }
  }

  private void setUI(){
    devicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
    devicesListView.setAdapter(devicesAdapter);
    startButton.setClickable(false);
  }

  @Subscribe public void onBTConnectedEvent(BTConnectedEvent event) {
    devicesAdapter.add(event.getDevice().getName());
    devicesAdapter.notifyDataSetChanged();
    playersCount++;
    if(playersCount > 0){
      startButton.setClickable(true);
    }
    if(playersCount >= 4){
      addButton.setClickable(false);
    }
  }

  @Subscribe public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
    devicesAdapter.remove(event.getDevice().getName());
    devicesAdapter.notifyDataSetChanged();
    playersCount--;
    if(playersCount == 0){
      startButton.setClickable(false);
    }
    if(playersCount < 4){
      addButton.setClickable(true);
    }
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

  private void checkBluetooth() {
    // If the adapter is null, then Bluetooth is not supported
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter == null) {
      Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @OnClick(R.id.addButton) public void addPlayer(){
    Intent i = new Intent(this, DeviceListActivity.class);
    startActivityForResult(i, REQUEST_CONNECT_DEVICE);
  }
  @OnClick(R.id.startButton) public void startGame(){
    Intent intent = new Intent(this, MasterActivity.class);
    startActivity(intent);
  }

}
