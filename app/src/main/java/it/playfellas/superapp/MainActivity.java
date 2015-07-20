package it.playfellas.superapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import it.playfellas.superapp.services.BLService;

public class MainActivity extends AppCompatActivity {

  // Intent request codes
  private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
  private static final int REQUEST_ENABLE_BT = 2;

  /**
   * Local Bluetooth adapter
   */
  private BluetoothAdapter mBluetoothAdapter = null;

  private BLService mBLService;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    checkBluetooth();
  }

  @Override
  public void onStart() {
    super.onStart();
    // If BT is not on, request that it be enabled.
    // setupChat() will then be called during onActivityResult
    if (!mBluetoothAdapter.isEnabled()) {
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
      // Otherwise, setup the chat session
    } else if (mBLService == null) {
      mBLService = new BLService(this);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mBLService != null) {
      mBLService.stop();
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    // Performing this check in onResume() covers the case in which BT was
    // not enabled during onStart(), so we were paused to enable it...
    // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
    if (mBLService != null) {
      // Only if the state is STATE_NONE, do we know that we haven't started already
      if (mBLService.getState() == BLService.STATE_NONE) {
        // Start the Bluetooth chat services
        mBLService.start();
      }
    }
  }

  /**
   * Makes this device discoverable.
   */
  private void ensureDiscoverable() {
    if (mBluetoothAdapter.getScanMode() !=
        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
      Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
      discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
      startActivity(discoverableIntent);
    }
  }

  private void checkBluetooth(){
    // If the adapter is null, then Bluetooth is not supported
    if (mBluetoothAdapter == null) {
      Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
      finish();
    }
  }
}
