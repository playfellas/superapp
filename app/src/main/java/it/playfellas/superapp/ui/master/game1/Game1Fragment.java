package it.playfellas.superapp.ui.master.game1;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.ui.master.GameFragment;

public class Game1Fragment extends GameFragment {
    public static final String TAG = Game1Fragment.class.getSimpleName();

    private static Config1 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @param config1 The config object
     * @return This Fragment instance.
     */
    public static Game1Fragment newInstance(Config1 config1) {
        Game1Fragment fragment = new Game1Fragment();
        config = config1;
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
}
