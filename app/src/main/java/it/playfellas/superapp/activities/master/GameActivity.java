package it.playfellas.superapp.activities.master;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.game1.Game1Fragment;
import it.playfellas.superapp.activities.master.game1.Game1SettingsFragment;

public class GameActivity extends AppCompatActivity implements Game1SettingsFragment.StartListener{

    private static final String TAG = GameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fragment);

        ButterKnife.bind(this);

        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();

        int gameType = this.getIntent().getIntExtra("game_num", 1);

        switch(gameType) {
            case 1:
                //start settings fragment, different for every game
                Game1SettingsFragment gameFragment = Game1SettingsFragment.newInstance();
                fragmentTransaction.replace(R.id.game_fragment_container, gameFragment, Game1SettingsFragment.TAG);
                break;
            case 2:
                break;
            case 3:
                break;
        }

        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }


    @Override
    public void startGame(int gameType) {
        //start the Fragment with the game, replacing the settings fragment
         Log.d(TAG, "start game");

        FragmentTransaction fragmentTransaction =  this.getSupportFragmentManager().beginTransaction();
        
        //start settings fragment, different for every game
        switch(gameType) {
            case 1:
                Game1Fragment gameFragment = Game1Fragment.newInstance();
                fragmentTransaction.replace(R.id.game_fragment_container, gameFragment, Game1Fragment.TAG);
                break;
            case 2:
                break;
            case 3:
                break;
        }

        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();

    }
}
