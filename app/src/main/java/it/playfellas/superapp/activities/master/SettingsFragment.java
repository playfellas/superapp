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

    public Config config;

    public SharedPreferences.Editor editor;
    public SharedPreferences sharedPref;


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

    /**
     * Method to read preferences
     */
    public void readPreferences() {
        config.setDifficultyLevel(sharedPref.getInt("difficulty", 4));
        config.setTileDensity(sharedPref.getInt("density", 4));
        config.setMaxScore(sharedPref.getInt("consecutiveAnswer", 4));
        config.setNoStages(sharedPref.getInt("stageNumber", 4));
        config.setSpeedUp(sharedPref.getBoolean("increasingSpeed", false));
        difficultySpinner.setSelection(config.getDifficultyLevel());
        densitySeekBar.setProgress(config.getTileDensity());
        consecutiveAnswerSeekBar.setProgress(config.getMaxScore());
        stagesSeekBar.setProgress(config.getNoStages());
        increasingSpeedCheckBox.setChecked(config.isSpeedUp());
    }

    /**
     * Method to save preferences
     */
    public void savePreferences() {
        this.editor = sharedPref.edit();

        config.setDifficultyLevel(difficultySpinner.getSelectedItemPosition());
        config.setTileDensity(densitySeekBar.getProgress());
        config.setMaxScore(consecutiveAnswerSeekBar.getProgress());
        config.setNoStages(stagesSeekBar.getProgress());
        config.setSpeedUp(increasingSpeedCheckBox.isChecked());

        editor.putInt("difficulty", config.getDifficultyLevel());
        editor.putInt("density", config.getTileDensity());
        editor.putInt("consecutiveAnswer", config.getMaxScore());
        editor.putInt("stageNumber", config.getNoStages());
        editor.putBoolean("increasingSpeed", config.isSpeedUp());
    }
}
