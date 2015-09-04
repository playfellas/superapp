package it.playfellas.superapp.ui.master.game1;

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
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.ui.master.GameFragment;

public class Game1Fragment extends GameFragment {
    public static final String TAG = Game1Fragment.class.getSimpleName();

    private static Config1 config;
    private static List<Bitmap> playerBitmaps;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @param config1 The config object
     * @return This Fragment instance.
     */
    public static Game1Fragment newInstance(Config1 config1, List<Bitmap> bitmaps) {
        Game1Fragment fragment = new Game1Fragment();
        config = config1;
        playerBitmaps = bitmaps;
        return fragment;
    }

    public Game1Fragment() {
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
        super.presenter = new Game1Presenter(this, config);

        super.imageViews.add(super.photo1ImageView);
        super.imageViews.add(super.photo2ImageView);
        super.imageViews.add(super.photo3ImageView);
        super.imageViews.add(super.photo4ImageView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (super.imageViews == null || playerBitmaps == null) {
            Log.e(TAG, "ImageView or playerBitmaps are null");
            return;
        }
        

        for (int i = 0; i < playerBitmaps.size(); i++) {
            if (super.imageViews.get(i) == null || playerBitmaps.get(i) == null) {
                Log.e(TAG, "ImageView.get(i) or playerPhoto.get(i) are null");
                continue;
            }
            super.imageViews.get(i).setImageBitmap(playerBitmaps.get(i));
        }
    }
}
