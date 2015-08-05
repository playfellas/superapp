package it.playfellas.superapp.activities.master;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;

/**
 * Created by Stefano Cappa on 31/07/15.
 */
public abstract class SettingsFragment extends Fragment {

    private static final String DIFFICULTY_LEVEL = "difficultyLevel";
    private static final String TILE_DENSITY = "tileDensity";
    private static final String MAX_SCORE = "maxScore";
    private static final String NUM_STAGES = "noStages";
    private static final String SPEEDUP = "speedUp";

    @Bind(R.id.difficultyLevelSpinner)
    public Spinner difficultyLevelSpinner;
    @Bind(R.id.tileDensitySeekBar)
    public SeekBar tileDensitySeekBar;
    @Bind(R.id.noStagesSeekBar)
    public SeekBar noStagesSeekBar;
    @Bind(R.id.maxScoreSeekBar)
    public SeekBar maxScoreSeekBar;
    @Bind(R.id.speedUpCheckBox)
    public CheckBox speedUpCheckBox;

    @Bind(R.id.startButton)
    public Button startButton;

    protected SharedPreferences.Editor editor;
    protected SharedPreferences sharedPref;

    private StartGameListener mListener;
    private Config config;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init sharedPref in superclass
        sharedPref = getActivity().getSharedPreferences(
                getString(getPreferencesId()), Context.MODE_PRIVATE);

        this.initDifficultySpinner();

        //get preferences
        this.readPreferences();
    }

    private void initDifficultySpinner() {
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.difficultyLevelSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulty_string_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    /**
     * Method to read preferences
     */
    private void readPreferences() {
        config = newConfig();
        config.setDifficultyLevel(sharedPref.getInt(DIFFICULTY_LEVEL, 4));
        config.setTileDensity(sharedPref.getInt(TILE_DENSITY, 4));
        config.setMaxScore(sharedPref.getInt(MAX_SCORE, 4));
        config.setNoStages(sharedPref.getInt(NUM_STAGES, 4));
        config.setSpeedUp(sharedPref.getBoolean(SPEEDUP, false));
        this.updateGui(config);
    }

    /**
     * Method to save preferences. You must call readPreferences before this method.
     */
    private void savePreferences() {
        if (config == null) {
            return;
        }
        this.editor = sharedPref.edit();

        config = setPreferences(editor);

        config.setDifficultyLevel(difficultyLevelSpinner.getSelectedItemPosition());
        config.setTileDensity(tileDensitySeekBar.getProgress());
        config.setMaxScore(noStagesSeekBar.getProgress());
        config.setNoStages(maxScoreSeekBar.getProgress());
        config.setSpeedUp(speedUpCheckBox.isChecked());

        editor.putInt(DIFFICULTY_LEVEL, config.getDifficultyLevel());
        editor.putInt(TILE_DENSITY, config.getTileDensity());
        editor.putInt(MAX_SCORE, config.getMaxScore());
        editor.putInt(NUM_STAGES, config.getNoStages());
        editor.putBoolean(SPEEDUP, config.isSpeedUp());

        //save all preferences, common, and specific defined here
        editor.apply();
    }

    private void updateGui(Config config) {
        difficultyLevelSpinner.setSelection(config.getDifficultyLevel());
        tileDensitySeekBar.setProgress(config.getTileDensity());
        noStagesSeekBar.setProgress(config.getMaxScore());
        maxScoreSeekBar.setProgress(config.getNoStages());
        speedUpCheckBox.setChecked(config.isSpeedUp());
        showPreferences();
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

    /**
     * This method is in the callback interface {@link StartGameListener} and is implemented in
     * {@link it.playfellas.superapp.activities.master.GameActivity}
     *
     * @param view
     */
    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        if (mListener != null) {
            this.savePreferences();
            onStartGame(mListener);
        }
    }

    public abstract void onStartGame(StartGameListener l);

    protected abstract Config newConfig();

    protected abstract void showPreferences();

    protected abstract Config setPreferences(SharedPreferences.Editor editor);

    protected abstract int getPreferencesId();
}
