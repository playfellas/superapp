package it.playfellas.superapp.ui.master;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.master.game1.Game1Fragment;
import it.playfellas.superapp.ui.master.game1.Game1SettingsFragment;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.Config3;

public class GameActivity extends AppCompatActivity implements StartGameListener {

    private static final String TAG = GameActivity.class.getSimpleName();
    private static final String GAME_NUM_INTENTNAME = "game_num";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fragment);

        ButterKnife.bind(this);

        //start settings fragment, different for every game
        int gameType = this.getIntent().getIntExtra(GAME_NUM_INTENTNAME, 1);
        switch (gameType) {
            default:
            case 1:
                this.changeFragment(Game1SettingsFragment.newInstance(), Game1SettingsFragment.TAG);
                break;
            case 2:
//                this.changeFragment(Game2SettingsFragment.newInstance(), Game2SettingsFragment.TAG);
                break;
            case 3:
//                this.changeFragment(Game3SettingsFragment.newInstance(), Game3SettingsFragment.TAG);
                break;
        }
    }

    /**
     * Method in {@link StartGameListener#startGame1(Config1)}
     *
     * @param config The Config object
     */
    @Override
    public void startGame1(Config1 config) {
        Log.d(TAG, "start game 1");
        this.changeFragment(Game1Fragment.newInstance(config), Game1Fragment.TAG);
    }

    /**
     * Method in {@link StartGameListener#startGame2(Config2)}
     *
     * @param config The Config object
     */
    @Override
    public void startGame2(Config2 config) {
        Log.d(TAG, "start game 2");
//        this.changeFragment(Game2Fragment.newInstance(config), Game2Fragment.TAG);
    }

    /**
     * Method in {@link StartGameListener#startGame3(Config3)}
     *
     * @param config The Config object
     */
    @Override
    public void startGame3(Config3 config) {
        Log.d(TAG, "start game 3");
//        this.changeFragment(Game3Fragment.newInstance(config), Game3Fragment.TAG);
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
