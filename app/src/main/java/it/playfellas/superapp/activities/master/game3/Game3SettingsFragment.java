package it.playfellas.superapp.activities.master.game3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.SettingsFragment;
import it.playfellas.superapp.activities.master.StartGameListener;
import it.playfellas.superapp.logic.Config3;

public class Game3SettingsFragment extends SettingsFragment {

    public static final String TAG = Game3SettingsFragment.class.getSimpleName();

    @Bind(R.id.difficultySpinner)
    public Spinner difficultySpinner;

    @Bind(R.id.densitySeekBar)
    public SeekBar densitySeekBar;
    @Bind(R.id.consecutiveAnswerSeekBar)
    public SeekBar consecutiveAnswerSeekBar;
    @Bind(R.id.stagesSeekBar)
    public SeekBar stagesSeekBar;

    @Bind(R.id.increasingSpeeCheckBox)
    public CheckBox increasingSpeedCheckBox;

    @Bind(R.id.startButton)
    public Button startButton;

    private StartGameListener mListener;

    private SharedPreferences sharedPref;

    private Config3 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game3SettingsFragment newInstance() {
        return new Game3SettingsFragment();
    }

    public Game3SettingsFragment() {
        config = new Config3();
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

        super.sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_key_game3), Context.MODE_PRIVATE);

        this.readPreferences();
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

    @Override
    public void readPreferences() {
        super.config = new Config3();
        super.readPreferences();
    }

    @Override
    public void savePreferences() {
        super.savePreferences();
        super.editor.apply();
    }

    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        if (mListener != null) {
            mListener.startGame3();
            savePreferences();
        }
    }
}