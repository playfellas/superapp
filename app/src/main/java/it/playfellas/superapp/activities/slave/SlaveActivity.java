package it.playfellas.superapp.activities.slave;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
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
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.InternalEvent;
import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.events.game.StartGame1Event;
import it.playfellas.superapp.events.game.StartGame2Event;
import it.playfellas.superapp.events.game.StartGame3Event;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.DbAccess;
import it.playfellas.superapp.logic.db.DbFiller;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveActivity extends AppCompatActivity implements
        PhotoFragment.PhotoFragmentListener,
        StartSlaveGameListener {

    private static final String TAG = SlaveActivity.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter = null;

    private Bitmap photoBitmap;

    private DbAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        ButterKnife.bind(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TenBus.get().register(this);

        this.listen();

        this.changeFragment(PhotoFragment.newInstance(), PhotoFragment.TAG);

        this.db = new DbAccess(this);
        new DbFiller(this.db);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void selectWaitingFragment() {
        this.changeFragment(WaitingFragment.newInstance(), WaitingFragment.TAG);
    }

    @Override
    public void setPhotoBitmap(Bitmap photo) {
        this.photoBitmap = photo;
    }

    @Override
    public void sendPhotoEvent() {
        TenBus.get().post(EventFactory.sendPhoto(photoBitmap));
    }

    //*************************************************
    //ONLY FOR TESTING DURING DEVELOPMENT
    @Override
    public void selectSlaveGameFragment(int num) {
        Config config;
        switch (num) {
            default:
            case 1:
                config = new Config1();
                this.changeFragment(SlaveGame1Fragment.newInstance(this.db, (Config1) config, this.photoBitmap), SlaveGame1Fragment.TAG);
                break;
            case 2:
                config = new Config2();
                this.changeFragment(SlaveGame2Fragment.newInstance(this.db, (Config2) config, this.photoBitmap), SlaveGame2Fragment.TAG);
                break;
            case 3:
                config = new Config3();
                this.changeFragment(SlaveGame3Fragment.newInstance(this.db, (Config3) config, this.photoBitmap), SlaveGame3Fragment.TAG);
                break;
        }
    }
    //*************************************************


    private void checkBluetooth() {
        // If the adapter is null, then Bluetooth is not supported
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
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


    //TODO OTTO receives a NETEVENT to change the correct slave game fragment
    @Subscribe
    public void onBTStartGame1Event(StartGame1Event event) {
        Config1 config = event.getConf();
        this.changeFragment(SlaveGame1Fragment.newInstance(this.db, config, this.photoBitmap), SlaveGame1Fragment.TAG);
    }

    @Subscribe
    public void onBTStartGame2Event(StartGame2Event event) {
        Config2 config = event.getConf();
        this.changeFragment(SlaveGame2Fragment.newInstance(this.db, config, this.photoBitmap), SlaveGame2Fragment.TAG);
    }

    @Subscribe
    public void onBTStartGame3Event(StartGame3Event event) {
        Config3 config = event.getConf();
        this.changeFragment(SlaveGame3Fragment.newInstance(this.db, config, this.photoBitmap), SlaveGame3Fragment.TAG);
    }

    @Subscribe
    public void onNetEvent(NetEvent event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onInternalEvent(InternalEvent event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onBTConnectedEvent(BTConnectedEvent event) {
        Toast.makeText(this, event.getDevice().getName(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
        Toast.makeText(this, event.getDevice().getName(), Toast.LENGTH_SHORT).show();
    }


    /**
     * Method definied in {@link StartSlaveGameListener#startSlaveGame(String)} and
     * called in {@link SlaveGame1Fragment}, {@link SlaveGame2Fragment} and {@link SlaveGame3Fragment}.
     *
     * @param tagFragment
     */
    @Override
    public void startSlaveGame(String tagFragment) {
        //TODO implement this method if you want to add some behaviours when
        //TODO slavefragment startSlaveGame method in StartSlaveGameListener
        switch (tagFragment) {
            default:
            case SlaveGame1Fragment.TAG:

                break;
            case SlaveGame2Fragment.TAG:

                break;
            case SlaveGame3Fragment.TAG:

                break;
        }
    }
}
