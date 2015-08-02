package it.playfellas.superapp.activities.master;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

import butterknife.Bind;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;

/**
 * Created by Stefano Cappa on 31/07/15.
 */
public class SettingsFragment extends Fragment {

    private static final String DIFFICULTY_LEVEL = "difficultyLevel";
    private static final String TILE_DENSITY = "tileDensity";
    private static final String MAX_SCORE = "maxScore";
    private static final String NUM_STAGES = "noStages";
    private static final String SPEEDUP = "speedUp";

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

    protected Config config;

    protected SharedPreferences.Editor editor;
    protected SharedPreferences sharedPref;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.initDifficultySpinner();
    }

    private void initDifficultySpinner() {
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.difficultySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulty_string_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void initConfig (Config config) {
        if(this.config == null) {
            this.config = config;
        }
    }

    /**
     * Method to read preferences
     */
    protected void readPreferences(Config config) {

        this.initConfig(config);

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
    protected void savePreferences() {
        this.editor = sharedPref.edit();

        config.setDifficultyLevel(difficultySpinner.getSelectedItemPosition());
        config.setTileDensity(densitySeekBar.getProgress());
        config.setMaxScore(consecutiveAnswerSeekBar.getProgress());
        config.setNoStages(stagesSeekBar.getProgress());
        config.setSpeedUp(increasingSpeedCheckBox.isChecked());

        this.setEditor(config);
    }

    private void setEditor(Config config) {
        editor.putInt(DIFFICULTY_LEVEL, config.getDifficultyLevel());
        editor.putInt(TILE_DENSITY, config.getTileDensity());
        editor.putInt(MAX_SCORE, config.getMaxScore());
        editor.putInt(NUM_STAGES, config.getNoStages());
        editor.putBoolean(SPEEDUP, config.isSpeedUp());
    }

    private void updateGui(Config config) {
        difficultySpinner.setSelection(config.getDifficultyLevel());
        densitySeekBar.setProgress(config.getTileDensity());
        consecutiveAnswerSeekBar.setProgress(config.getMaxScore());
        stagesSeekBar.setProgress(config.getNoStages());
        increasingSpeedCheckBox.setChecked(config.isSpeedUp());
    }
}
