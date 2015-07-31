package it.playfellas.superapp.activities.slave.game2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.slave.StartSlaveGameListener;
import it.playfellas.superapp.activities.slave.game1.Slave1Presenter;
import it.playfellas.superapp.logic.Config2;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame2Fragment extends Fragment {
    public static final String TAG = SlaveGame2Fragment.class.getSimpleName();

    private static Slave2Presenter presenter;
    private StartSlaveGameListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    public static SlaveGame2Fragment newInstance(Config2 config) {
        SlaveGame2Fragment fragment = new SlaveGame2Fragment();

        if(presenter == null) {
            presenter = new Slave2Presenter();
        }
        presenter.onTakeView(fragment, config);

        return fragment;
    }
    public SlaveGame2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game2_fragment, container, false);
        return root;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.startSlaveGame2();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (StartSlaveGameListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + StartSlaveGameListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}