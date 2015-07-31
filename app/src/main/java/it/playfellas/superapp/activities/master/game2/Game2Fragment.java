package it.playfellas.superapp.activities.master.game2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;

public class Game2Fragment extends Fragment {

    public static final String TAG = Game2Fragment.class.getSimpleName();

    private static Game2FragmentPresenter presenter;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game2Fragment newInstance() {
        return new Game2Fragment();
    }

    public Game2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game2_fragment, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);
        //Create the presenter
        if (presenter == null) presenter = new Game2FragmentPresenter();
        presenter.onTakeView(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
