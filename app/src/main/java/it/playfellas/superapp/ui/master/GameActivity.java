package it.playfellas.superapp.ui.master;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.DbAccess;
import it.playfellas.superapp.logic.db.DbException;
import it.playfellas.superapp.logic.db.DbFiller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.BitmapUtils;
import it.playfellas.superapp.ui.master.game1.Game1Fragment;
import it.playfellas.superapp.ui.master.game1.Game1SettingsFragment;
import it.playfellas.superapp.ui.master.game2.Game2Fragment;
import it.playfellas.superapp.ui.master.game2.Game2SettingsFragment;
import it.playfellas.superapp.ui.master.game3.Game3Fragment;
import it.playfellas.superapp.ui.master.game3.Game3SettingsFragment;

public class GameActivity extends ImmersiveAppCompatActivity implements StartGameListener {

    private static final String TAG = GameActivity.class.getSimpleName();
    private static final String GAME_NUM_INTENTNAME = "game_num";
    private List<Bitmap> playerImages = new ArrayList<>();
    private DbAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.game_fragment);

        ButterKnife.bind(this);

        Bundle b = this.getIntent().getExtras().getBundle("masterActivity");

        if (b == null) {
            Log.e(TAG, "Bundle is null");
            finish();
            return;
        }

        for (int i = 0; i < TenBus.get().noDevices(); i++) {
            byte[] photoArray = b.getByteArray("photo" + (i + 1));
            if (photoArray != null) {
                playerImages.add(BitmapUtils.scaleBitmap(BitmapUtils.fromByteArraytoBitmap(photoArray),
                        l));
            }
        }

        this.db = new DbAccess(this);

        //Fill the db
        try {
            (new DbFiller(this.db)).fill();
        } catch (DbException e) {
            Log.e(TAG, "DbException", e);
            finish();
        }

        //start settings fragment, different for every game
        int gameType = b.getInt(GAME_NUM_INTENTNAME, 1);
        switch (gameType) {
            default:
            case 1:
                this.changeFragment(Game1SettingsFragment.newInstance(), Game1SettingsFragment.TAG);
                break;
            case 2:
                this.changeFragment(Game2SettingsFragment.newInstance(), Game2SettingsFragment.TAG);
                break;
            case 3:
                this.changeFragment(Game3SettingsFragment.newInstance(), Game3SettingsFragment.TAG);
                break;
        }

        b.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * Method in {@link StartGameListener#startGame1(Config1)}
     *
     * @param config The Config object
     */
    @Override
    public void startGame1(Config1 config) {
        Log.d(TAG, "start game 1 with " + playerImages.size() + " photos");
        this.changeFragment(Game1Fragment.newInstance(config, playerImages), Game1Fragment.TAG);
    }

    /**
     * Method in {@link StartGameListener#startGame2(Config2)}
     *
     * @param config The Config object
     */
    @Override
    public void startGame2(Config2 config) {
        Log.d(TAG, "start game 2");
        this.changeFragment(Game2Fragment.newInstance(config, playerImages, this.db), Game2Fragment.TAG);
    }

    /**
     * Method in {@link StartGameListener#startGame3(Config3)}
     *
     * @param config The Config object
     */
    @Override
    public void startGame3(Config3 config) {
        Log.d(TAG, "start game 3");
        this.changeFragment(Game3Fragment.newInstance(config, playerImages, this.db), Game3Fragment.TAG);
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.game_fragment_container, fragment, tag);
        executePendingTransactions(fragmentTransaction);
    }

    private void executePendingTransactions(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }
}
