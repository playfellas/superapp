package it.playfellas.superapp.ui.master.game2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.master.GameFragment;

public class Game2Fragment extends GameFragment {
    public static final String TAG = Game2Fragment.class.getSimpleName();

    static TileSelector tileSelector;
    private static Config2 config;
    private static List<Bitmap> playerBitmaps;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @param config2 The config object
     * @return This Fragment instance.
     */
    public static Game2Fragment newInstance(Config2 config2, TileSelector ts) {
        Game2Fragment fragment = new Game2Fragment();
        config = config2;
        tileSelector = ts;
        return fragment;
    }

    public Game2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_master_fragment, container, false);

        //call this before the presenter.onTakeView
        super.photoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable._master_central_img);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //Create the presenter
        super.presenter = new Game2Presenter(this, config);

        super.initPhotos();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (super.photoimageViews == null || playerBitmaps == null) {
            Log.e(TAG, "ImageView or playerBitmaps are null");
            return;
        }
    }
}
