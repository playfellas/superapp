package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame1DirectionFragment extends SlaveGame1Fragment {
    public static final String TAG = "SlaveGame1ColorFragment";

    private static TileDirection td;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame1Fragment newInstance(TileSelector ts, Config1 config1, TileDirection tileDirection, Bitmap photoBitmap) {
        SlaveGame1Fragment.init(ts, config1, photoBitmap);
        SlaveGame1DirectionFragment fragment = new SlaveGame1DirectionFragment();;
        td = tileDirection;
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new Slave1Presenter(db, this, config);
        presenter.initControllerDirection(td);
    }
}
