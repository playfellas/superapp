package it.playfellas.superapp.ui.master.game1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
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
        View rootView = inflater.inflate(R.layout.game1_fragment, container, false);

        //call this before the presenter.onTakeView
        super.photoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.master_central_img_example);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //Create the presenter
        super.presenter = new Game1Presenter(this, config);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO reimpl this, do not remove please
//        ImageView photo1ImageView = (ImageView)this.getView().findViewById(R.id.photo1ImageView);
//        if(playerBitmaps==null && playerBitmaps.get(0)==null) {
//            Log.d(TAG, "Null");
//            return;
//        }
//        if (photo1ImageView != null && playerBitmaps!=null && playerBitmaps.get(0)!=null) {
//            photo1ImageView.setImageBitmap(playerBitmaps.get(0));
//        } else {
//            Log.e(TAG, "this.getPhoto1ImageView()==null");
//        }
    }
}
