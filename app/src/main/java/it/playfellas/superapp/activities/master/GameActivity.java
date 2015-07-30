package it.playfellas.superapp.activities.master;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fragment);

        ButterKnife.bind(this);

        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();

        //start settings fragment, different for every game
        int gameType = this.getIntent().getIntExtra("game_num", 1);
        switch(gameType) {
            case 1:
                Game1SettingsFragment game1Fragment = Game1SettingsFragment.newInstance();
                fragmentTransaction.replace(R.id.game_fragment_container, game1Fragment, Game1SettingsFragment.TAG);
                break;
            case 2:
                Game2SettingsFragment game2Fragment = Game2SettingsFragment.newInstance();
                fragmentTransaction.replace(R.id.game_fragment_container, game2Fragment, Game2SettingsFragment.TAG);
                break;
            case 3:
                Game3SettingsFragment game3Fragment = Game3SettingsFragment.newInstance();
                fragmentTransaction.replace(R.id.game_fragment_container, game3Fragment, Game3SettingsFragment.TAG);
                break;
        }

        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void startGame1() {
        Log.d(TAG, "start game 1");

        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();

        Game1Fragment gameFragment = Game1Fragment.newInstance();
        fragmentTransaction.replace(R.id.game_fragment_container, gameFragment, Game1Fragment.TAG);

        this.executePendingTransactions(fragmentTransaction);
    }

    @Override
    public void startGame2() {
        Log.d(TAG, "start game 2");

        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();

        Game2Fragment gameFragment = Game2Fragment.newInstance();
        fragmentTransaction.replace(R.id.game_fragment_container, gameFragment, Game2Fragment.TAG);

        this.executePendingTransactions(fragmentTransaction);
    }

    @Override
    public void startGame3() {
        Log.d(TAG, "start game 3");

        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();

        Game3Fragment gameFragment = Game3Fragment.newInstance();
        fragmentTransaction.replace(R.id.game_fragment_container, gameFragment, Game3Fragment.TAG);

        this.executePendingTransactions(fragmentTransaction);
    }



    private void executePendingTransactions(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }
}
