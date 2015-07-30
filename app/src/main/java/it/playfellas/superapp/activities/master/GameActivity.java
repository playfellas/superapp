package it.playfellas.superapp.activities.master;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.game1.Game1Fragment;
import it.playfellas.superapp.activities.master.game1.Game1SettingsFragment;
import it.playfellas.superapp.activities.master.game2.Game2Fragment;
import it.playfellas.superapp.activities.master.game2.Game2SettingsFragment;
import it.playfellas.superapp.activities.master.game3.Game3Fragment;
import it.playfellas.superapp.activities.master.game3.Game3SettingsFragment;

public class GameActivity extends AppCompatActivity implements StartGameListener{

    private static final String TAG = GameActivity.class.getSimpleName();
    private static final String GAME_NUM_INTENTNAME = "game_num";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fragment);

        ButterKnife.bind(this);

        //start settings fragment, different for every game
        int gameType = this.getIntent().getIntExtra(GAME_NUM_INTENTNAME, 1);
        switch(gameType) {
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
    }

    @Override
    public void startGame1() {
        Log.d(TAG, "start game 1");
        this.changeFragment(Game1Fragment.newInstance(), Game1Fragment.TAG);
    }

    @Override
    public void startGame2() {
        Log.d(TAG, "start game 2");
        this.changeFragment(Game2Fragment.newInstance(), Game2Fragment.TAG);
    }

    @Override
    public void startGame3() {
        Log.d(TAG, "start game 3");
        this.changeFragment(Game3Fragment.newInstance(), Game3Fragment.TAG);
    }


    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.game_fragment_container, fragment, tag);
        executePendingTransactions(fragmentTransaction);
    }

    private void executePendingTransactions(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }
}
