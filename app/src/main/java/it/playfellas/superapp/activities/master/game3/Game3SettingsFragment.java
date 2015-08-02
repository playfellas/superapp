package it.playfellas.superapp.activities.master.game3;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.SettingsFragment;
import it.playfellas.superapp.activities.master.StartGameListener;
import it.playfellas.superapp.logic.Config3;


public class Game3SettingsFragment extends SettingsFragment {

    public static final String TAG = "Game3SettingsFragment";

    @Bind(R.id.startButton)
    public Button startButton;

    private StartGameListener mListener;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game3SettingsFragment newInstance() {
        return new Game3SettingsFragment();
    }

    public Game3SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game3_settings_fragment, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init sharedPref in superclass
        super.sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_key_game3), Context.MODE_PRIVATE);

        //get preferences
        this.getPreferences();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (StartGameListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + StartGameListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getPreferences() {
        //read preferences of commons attributes calling the super class.
        //i'm passing the concrete implementation Config3 because i must cast to Config3 next
        super.readPreferences(new Config3());
    }

    private void setPreferences() {
        //save preferences of commons attributes calling the super class.
        super.savePreferences();

        //save all preferences, common, and specific defined here
        super.editor.apply();
    }

    /**
     * This method is in the callback interface {@link StartGameListener} and is implemented in
     * {@link it.playfellas.superapp.activities.master.GameActivity#startGame(String)}
     * @param view
     */
    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        if (mListener != null) {
            this.setPreferences();
            mListener.startGame(TAG);
        }
    }
}