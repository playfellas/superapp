//package it.playfellas.superapp.activities;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import com.squareup.otto.Subscribe;
//import it.playfellas.superapp.R;
//import it.playfellas.superapp.events.BTConnectedEvent;
//import it.playfellas.superapp.events.BTDisconnectedEvent;
//import it.playfellas.superapp.events.InternalEvent;
//import it.playfellas.superapp.events.NetEvent;
//import it.playfellas.superapp.events.StringNetEvent;
//import it.playfellas.superapp.network.TenBus;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class BluetoothActivity extends AppCompatActivity {
//
//  private static final String TAG = "MainActivity";
//
//  // Intent request codes
//  private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
//  private static final int REQUEST_ENABLE_BT = 2;
//
//  private TenBus bus = TenBus.get();
//
//  @Bind(R.id.slaveButton) Button slaveButton;
//  @Bind(R.id.masterButton) Button masterButton;
//  @Bind(R.id.sendButton) Button sendButton;
//  @Bind(R.id.editText) EditText editText;
//  @Bind(R.id.textView) TextView textView;
//  @Bind(R.id.devicesListView) ListView devicesListView;
//  @Bind(R.id.activitiesListView) ListView activitiesListView;
//
//  private ArrayAdapter<String> devicesAdapter;
//  private ArrayAdapter<String> activitiesAdapter;
//
//  //Local Bluetooth adapter
//  private BluetoothAdapter mBluetoothAdapter = null;
//
//  private boolean master = false;
//
//  @Override protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//
//    ButterKnife.bind(this);
//    sendButton.setClickable(false);
//    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    bus.register(this);
//    checkBluetooth();
//    setupListViews();
//  }
//
//  @Override public void onStart() {
//    super.onStart();
//    // If BT is disabled, request that it be enabled.
//    // setupChat() will then be called during onActivityResult
//    if (!mBluetoothAdapter.isEnabled()) {
//      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//      // Otherwise, setup the chat session
//    }
//  }
//
//  @Override public void onDestroy() {
//    super.onDestroy();
//    bus.detach();
//  }
//
//  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    switch (requestCode) {
//      case REQUEST_CONNECT_DEVICE_SECURE:
//        // When DeviceListActivity returns with a device to connect
//        if (resultCode == Activity.RESULT_OK) {
//          connectDevice(data);
//        }
//        break;
//      case REQUEST_ENABLE_BT:
//        // When the request to enable Bluetooth returns
//        if (resultCode == Activity.RESULT_OK) {
//        } else {
//          // User did not enable Bluetooth or an error occurred
//          Log.d(TAG, "BT not enabled");
//          Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
//          finish();
//        }
//    }
//  }
//
//  //*******************************************************************
//  //************************EVENT WITH OTTO BUS************************
//  //*******************************************************************
//  private void addToActivities(String eventClass, String s) {
//    activitiesAdapter.add(
//        ("[" + eventClass + "] - " + new SimpleDateFormat("HH:mm:ss").format(new Date()))
//            + " - "
//            + s);
//    activitiesAdapter.notifyDataSetChanged();
//    activitiesListView.post(new Runnable() {
//      @Override public void run() {
//        activitiesListView.smoothScrollByOffset(activitiesListView.getAdapter().getCount());
//      }
//    });
//  }
//
//  @Subscribe public void onNetEvent(NetEvent event) {
//    addToActivities(event.getClass().getSimpleName(), event.toString());
//  }
//
//  @Subscribe public void onInternalEvent(InternalEvent event) {
//    addToActivities(event.getClass().getSimpleName(), event.toString());
//  }
//
//  @Subscribe public void onBTConnectedEvent(BTConnectedEvent event) {
//    devicesAdapter.add(event.getDevice().getName());
//    devicesAdapter.notifyDataSetChanged();
//  }
//
//  @Subscribe public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
//    devicesAdapter.remove(event.getDevice().getName());
//    devicesAdapter.notifyDataSetChanged();
//    if (!master) {
//      masterButton.setClickable(true);
//      slaveButton.setClickable(true);
//      sendButton.setClickable(false);
//    }
//  }
//  //********************************************************************
//
//  //*************************************************************************
//  //************************ON CLICK WITH BUTTERKNIFE************************
//  //*************************************************************************
//
//  @OnClick(R.id.masterButton) public void onMasterButtonClicked(View view) {
//    Intent serverIntent = new Intent(this, DeviceListActivity.class);
//    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
//    master = true;
//    textView.setText("MASTER connected to:");
//    slaveButton.setClickable(false);
//    sendButton.setClickable(true);
//  }
//
//  @OnClick(R.id.slaveButton) public void onSlaveButtonClicked(View view) {
//    ensureDiscoverable();
//    textView.setText("SLAVE  connected to:");
//    masterButton.setClickable(false);
//    slaveButton.setClickable(false);
//    sendButton.setClickable(true);
//    try {
//      bus.attach(null);
//    } catch (IOException e) {
//      Toast.makeText(this, "Listen error", Toast.LENGTH_SHORT).show();
//    }
//  }
//
//  @OnClick(R.id.sendButton) public void sendMessage(View view) {
//    StringNetEvent netEvent = new StringNetEvent(editText.getText().toString());
//    bus.post(netEvent);
//    editText.setText("");
//  }
//  //************************************************************************
//
//  /**
//   * Establish connection with other devices
//   *
//   * @param data An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
//   */
//  private void connectDevice(Intent data) {
//    // Get the device MAC address
//    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//    // Get the BluetoothDevice object
//    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//    // Attempt to connect to the device
//    try {
//      bus.attach(device);
//    } catch (IOException e) {
//      Toast.makeText(this, "Connect error", Toast.LENGTH_SHORT).show();
//    }
//  }
//
//  /**
//   * Makes this device discoverable.
//   */
//  private void ensureDiscoverable() {
//    if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//      Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//      discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//      startActivity(discoverableIntent);
//    }
//  }
//
//  private void checkBluetooth() {
//    // If the adapter is null, then Bluetooth is not supported
//    if (mBluetoothAdapter == null) {
//      Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
//      finish();
//    }
//  }
//
//  private void setupListViews() {
//    devicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//    activitiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//    devicesListView.setAdapter(devicesAdapter);
//    activitiesListView.setAdapter(activitiesAdapter);
//  }
//}
