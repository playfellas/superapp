package it.playfellas.superapp.ui.slave.game2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;

/**
 * Created by Stefano Cappa on 12/08/15.
 */
public class SlaveGame2ColorFragment extends SlaveGame2Fragment {
    public static final String TAG = "SlaveGame2ColorFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame2Fragment newInstance(TileSelector ts, Config2 config2, Bitmap photoBitmap) {
        SlaveGame2Fragment fragment = SlaveGame2Fragment.newInstance(ts, config2, photoBitmap);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new Slave2Presenter(db, this, config);
        presenter.initController();
    }
}
