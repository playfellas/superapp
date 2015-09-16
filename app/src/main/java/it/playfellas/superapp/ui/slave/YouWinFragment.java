package it.playfellas.superapp.ui.slave;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.R;
import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 16/09/15.
 */
public class YouWinFragment extends Fragment {

    public static final String TAG = YouWinFragment.class.getSimpleName();
    private Handler h = new Handler();
    private Runnable transitionTask = new Runnable() {
        @Override
        public void run() {
            stopTrophy();
        }
    };

    public static YouWinFragment newInstance() {
        return new YouWinFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slave_win, container, false);
        TenBus.get().register(this);
        h.postDelayed(transitionTask, 3000);
        return v;
    }

    private void stopTrophy() {
        TenBus.get().unregister(this);
        startActivity(new Intent(this.getContext(), SlaveGameActivity.class));
    }

    @Subscribe
    public void onMasterEvent(NetEvent e) {
        // master said something.
        // Stop handler and exit.
        h.removeCallbacks(transitionTask);
    }
}
