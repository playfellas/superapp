package it.playfellas.superapp.activities.slave.game1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.slave.StartSlaveGameListener;
import it.playfellas.superapp.presenters.Conveyor;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame1Fragment extends Fragment {
    public static final String TAG = SlaveGame1Fragment.class.getSimpleName();

    private static Slave1Presenter presenter;
    private StartSlaveGameListener mListener;
    public Conveyor conveyorUp;
    public Conveyor conveyorDown;


    @Bind(R.id.downConveyor)
    public LinearLayout downConveyorLayout;

    @Bind(R.id.upConveyor)
    public LinearLayout upConveyorLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    public static SlaveGame1Fragment newInstance() {
        return new SlaveGame1Fragment();
    }

    public SlaveGame1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game1_fragment, container, false);

        ButterKnife.bind(this, root);

        Conveyor conveyorUp = new Conveyor(upConveyorLayout, 100, Conveyor.LEFT);
        Conveyor conveyorDown = new Conveyor(downConveyorLayout, 100, Conveyor.RIGHT);

        if(presenter == null) {
            presenter = new Slave1Presenter();
        }
        presenter.onTakeView(this);


        return root;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.startSlaveGame1();
        }
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
