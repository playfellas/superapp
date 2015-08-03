package it.playfellas.superapp.activities.master.game1;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.GameFragment;

public class Game1Fragment extends GameFragment {

    public static final String TAG = Game1Fragment.class.getSimpleName();

    private static Game1FragmentPresenter presenter;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game1Fragment newInstance() {
        return new Game1Fragment();
    }

    public Game1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game1_fragment, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //Create the presenter
        if (presenter == null) presenter = new Game1FragmentPresenter();
        presenter.onTakeView(this);

        super.photoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.master_central_img_example);
        super.initiCentralImage();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
