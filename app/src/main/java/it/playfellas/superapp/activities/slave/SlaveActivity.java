package it.playfellas.superapp.activities.slave;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.slave.game1.SlaveGame1Fragment;
import it.playfellas.superapp.activities.slave.game2.SlaveGame2Fragment;
import it.playfellas.superapp.activities.slave.game3.SlaveGame3Fragment;
import it.playfellas.superapp.events.InternalEvent;
import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.events.game.StartGame1Event;
import it.playfellas.superapp.events.game.StartGame2Event;
import it.playfellas.superapp.events.game.StartGame3Event;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveActivity extends AppCompatActivity implements
        PhotoFragment.PhotoFragmentListener,
        StartSlaveGameListener {

    private static final String TAG = SlaveActivity.class.getSimpleName();

    //Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        ButterKnife.bind(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TenBus.get().register(this);

        this.listen();

        this.changeFragment(PhotoFragment.newInstance(), PhotoFragment.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        TenBus.get().detach();
    }

    @Override
    public void selectWaitingFragment() {
        this.changeFragment(WaitingFragment.newInstance(), WaitingFragment.TAG);
    }

    private void listen() {
        ensureDiscoverable();
        try {
            TenBus.get().attach(null);
        } catch (IOException e) {
            Toast.makeText(this, "Listen error", Toast.LENGTH_SHORT).show();
        }
    }


    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.slave_root_container, fragment, tag);
        executePendingTransactions(fragmentTransaction);
    }

    private void executePendingTransactions(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    //TODO this methods can be useful in the future
    @Override
    public void startSlaveGame1() {
        //TODO method called from slavegamefragment1
    }

    @Override
    public void startSlaveGame2() {
        //TODO method called from slavegamefragment2
    }

    @Override
    public void startSlaveGame3() {
        //TODO method called from slavegamefragment3
    }


    //TODO OTTO receives a NETEVENT to change the correct slave game fragment
    @Subscribe public void onBTStartGame1Event(StartGame1Event event) {
        Config1 config = event.getConf();
        this.changeFragment(SlaveGame1Fragment.newInstance(config), SlaveGame1Fragment.TAG);
    }

    @Subscribe public void onBTStartGame2Event(StartGame2Event event) {
        Config2 config = event.getConf();
        this.changeFragment(SlaveGame2Fragment.newInstance(config), SlaveGame2Fragment.TAG);
    }

    @Subscribe public void onBTStartGame3Event(StartGame3Event event) {
        Config3 config = event.getConf();
        this.changeFragment(SlaveGame3Fragment.newInstance(config), SlaveGame3Fragment.TAG);
    }

    @Subscribe
    public void onNetEvent(NetEvent event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe public void onInternalEvent(InternalEvent event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe public void onBTConnectedEvent(BTConnectedEvent event) {
        Toast.makeText(this, event.getDevice().getName(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
        Toast.makeText(this, event.getDevice().getName(), Toast.LENGTH_SHORT).show();
    }

}
