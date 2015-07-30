package it.playfellas.superapp.activities.slave;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.slave.game1.SlaveGame1Fragment;
import it.playfellas.superapp.activities.slave.game2.SlaveGame2Fragment;
import it.playfellas.superapp.activities.slave.game3.SlaveGame3Fragment;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveActivity extends AppCompatActivity implements
        PhotoFragment.PhotoFragmentListener,
        StartSlaveGameListener {

    private SlavePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        ButterKnife.bind(this);

        this.changeFragment(PhotoFragment.newInstance(), PhotoFragment.TAG);
    }

    @Override
    public void selectWaitingFragment() {
        this.changeFragment(WaitingFragment.newInstance(), WaitingFragment.TAG);
    }


    //TODO OTTO receives a NETEVENT to change the correct slave game fragment
    public void startSalveGame() {
        //TODO get from EVENT
        int game_num = 1;

        switch (game_num) {
            default:
            case 1:
                this.changeFragment(SlaveGame1Fragment.newInstance(), SlaveGame1Fragment.TAG);
                break;
            case 2:
                this.changeFragment(SlaveGame2Fragment.newInstance(), SlaveGame2Fragment.TAG);
                break;
            case 3:
                this.changeFragment(SlaveGame3Fragment.newInstance(), SlaveGame3Fragment.TAG);
                break;
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
}
