package it.playfellas.superapp.activities.slave.game3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.slave.StartSlaveGameListener;
import it.playfellas.superapp.logic.Config3;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame3Fragment extends Fragment {
    public static final String TAG = "SlaveGame3Fragment";

    @Bind(R.id.photoImageView)
    public ImageView photoImageView;

    private static Bitmap photo;

    private static Slave3Presenter presenter;
    private StartSlaveGameListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    public static SlaveGame3Fragment newInstance(Config3 config, Bitmap photoBitmap) {
        SlaveGame3Fragment fragment = new SlaveGame3Fragment();

        photo = photoBitmap;

        if(presenter == null) {
            presenter = new Slave3Presenter();
        }
        presenter.onTakeView(fragment, config);

        return fragment;
    }

    public SlaveGame3Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game3_fragment, container, false);

        ButterKnife.bind(this, root);

        photoImageView.setImageBitmap(photo);

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
