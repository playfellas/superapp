package it.playfellas.superapp.activities.master.game1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.StartGameListener;


public class Game1SettingsFragment extends Fragment {

    public static final String TAG = Game1SettingsFragment.class.getSimpleName();

    @Bind(R.id.ruleGroup)
    public RadioGroup ruleRadioGroup;

    @Bind(R.id.difficultySpinner)
    public Spinner difficultySpinner;

    @Bind(R.id.densitySeekBar)
    public SeekBar densitySeekBar;
    @Bind(R.id.consecutiveAnswerSeekBar)
    public SeekBar consecutiveAnswerSeekBar;
    @Bind(R.id.stagesSeekBar)
    public SeekBar stagesSeekBar;
    @Bind(R.id.invertGameSeekBar)
    public SeekBar invertGameSeekBar;

    @Bind(R.id.increasingSpeeCheckBox)
    public CheckBox increasingSpeedCheckBox;

    @Bind(R.id.startButton)
    public Button startButton;

    private SharedPreferences sharedPref;

    private Config1 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game1SettingsFragment newInstance() {
        return new Game1SettingsFragment();
    }

    public Game1SettingsFragment() {
        config = new Config1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game1_settings_fragment, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_key_game1), Context.MODE_PRIVATE);

        this.initDifficultySpinner();

        readPreferences();
    }

    /**
     * Method to read preferences
     */
    private void readPreferences() {
        config.setRule(sharedPref.getInt("rule", 0));
        config.setDifficulty(sharedPref.getInt("difficulty", 4));
        config.setDensity(sharedPref.getInt("density", 4));
        config.setConsecutiveAnswer(sharedPref.getInt("consecutiveAnswer", 4));
        config.setStageNumber(sharedPref.getInt("stageNumber", 4));
        config.setConsecutiveAnswerChangeRule(sharedPref.getInt("consecutiveAnswerChangeRule", 6));
        config.setIncreasingSpeed(sharedPref.getBoolean("increasingSpeed", false));

        setRuleRadioGroup(config.getRule());
        difficultySpinner.setSelection(config.getDifficulty());
        densitySeekBar.setProgress(config.getDensity());
        consecutiveAnswerSeekBar.setProgress(config.getConsecutiveAnswer());
        stagesSeekBar.setProgress(config.getStageNumber());
        invertGameSeekBar.setProgress(config.getConsecutiveAnswerChangeRule());
        increasingSpeedCheckBox.setChecked(config.isIncreasingSpeed());
    }

    /**
     * Method to save preferences
     */
    private void savePreferences() {
        config.setRule(getCheckedRule());
        config.setDifficulty(difficultySpinner.getSelectedItemPosition());
        config.setDensity(densitySeekBar.getProgress());
        config.setConsecutiveAnswer(consecutiveAnswerSeekBar.getProgress());
        config.setStageNumber(stagesSeekBar.getProgress());
        config.setConsecutiveAnswerChangeRule(invertGameSeekBar.getProgress());
        config.setIncreasingSpeed(increasingSpeedCheckBox.isChecked());

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("rule", config.getRule());
        editor.putInt("difficulty", config.getDifficulty());
        editor.putInt("density", config.getDensity());
        editor.putInt("consecutiveAnswer", config.getConsecutiveAnswer());
        editor.putInt("stageNumber", config.getStageNumber());
        editor.putInt("consecutiveAnswerChangeRule", config.getConsecutiveAnswerChangeRule());
        editor.putBoolean("increasingSpeed", config.isIncreasingSpeed());
        editor.apply();
    }

    /**
     * This method return the index of a RadioButton in a RadioGroup.
     *
     * @return an integer from 0 to size - 1  of the RadioGroup
     */
    private int getCheckedRule() {
        int radioButtonID = ruleRadioGroup.getCheckedRadioButtonId();
        View radioButton = ruleRadioGroup.findViewById(radioButtonID);
        return ruleRadioGroup.indexOfChild(radioButton);
    }

    /**
     * This method set the check RadioButton in a RadioGroup by index
     *
     * @param index The index of the element, from 0 to size - 1 of the RadioGroup
     */
    private void setRuleRadioGroup(int index) {
        ((RadioButton) ruleRadioGroup.getChildAt(index)).setChecked(true);
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

    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        ((StartGameListener) getActivity()).startGame1();
        savePreferences();
    }
}