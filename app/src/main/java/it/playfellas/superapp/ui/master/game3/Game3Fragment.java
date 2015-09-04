package it.playfellas.superapp.ui.master.game3;

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
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.master.GameFragment;

public class Game3Fragment extends GameFragment {
    public static final String TAG = Game3Fragment.class.getSimpleName();

    static TileSelector tileSelector;
    private static Config3 config;
    private static List<Bitmap> playerBitmaps;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @param config3 The config object
     * @return This Fragment instance.
     */
    public static Game3Fragment newInstance(Config3 config3, List<Bitmap> bitmaps, TileSelector ts) {
        Game3Fragment fragment = new Game3Fragment();
        config = config3;
        playerBitmaps = bitmaps;
        tileSelector = ts;
        return fragment;
    }

    public Game3Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_master_fragment, container, false);

        //call this before the presenter.onTakeView
        super.photoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.master_central_img_example);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //Create the presenter
        super.presenter = new Game3Presenter(this, config);

        super.initPhotos();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (super.imageViews == null || playerBitmaps == null) {
            Log.e(TAG, "ImageView or playerBitmaps are null");
            return;
        }
        super.setPhotosInImageViews(playerBitmaps);
    }
}
