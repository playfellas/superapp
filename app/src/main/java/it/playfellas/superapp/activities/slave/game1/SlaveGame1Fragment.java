package it.playfellas.superapp.activities.slave.game1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.slave.StartSlaveGameListener;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.presenters.Conveyor;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame1Fragment extends Fragment {
    public static final String TAG = "SlaveGame1Fragment";

    private static Slave1Presenter presenter;
    private StartSlaveGameListener mListener;

    @Bind(R.id.photoImageView)
    public ImageView photoImageView;

    private static Bitmap photo;

    @Getter
    private Conveyor conveyorUp, conveyorDown;

    @Bind(R.id.downConveyor)
    public LinearLayout downConveyorLayout;

    @Bind(R.id.upConveyor)
    public LinearLayout upConveyorLayout;

    private static Config1 config;
    private static TileSelector db;

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    public static SlaveGame1Fragment newInstance(TileSelector ts, Config1 config1, Bitmap photoBitmap) {
        SlaveGame1Fragment fragment = new SlaveGame1Fragment();
        db = ts;
        config = config1;
//        photo = photoBitmap;
        return fragment;
    }

    public SlaveGame1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game1_fragment, container, false);

        ButterKnife.bind(this, root);

        conveyorUp = new Conveyor(upConveyorLayout, 100, Conveyor.LEFT);
        conveyorDown = new Conveyor(downConveyorLayout, 100, Conveyor.RIGHT);

        conveyorUp.start();
        conveyorDown.start();

//        photoImageView.setImageBitmap(photo);

        return root;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.startSlaveGame(TAG);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = new Slave1Presenter();
        }
        presenter.onTakeView(db, this, config);
        presenter.initController();
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
