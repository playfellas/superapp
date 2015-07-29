package it.playfellas.superapp.activities.master.game1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

/**
 * The MasterFragment
 */
public class Game1SettingsFragment extends Fragment {

    public static final String TAG = Game1SettingsFragment.class.getSimpleName();
    private  static final int GAMETYPE = 1;

    public interface StartListener {
        void startGame(int gameType);
    }

    @Bind(R.id.colorButton) public Button colorButton;
    @Bind(R.id.directionButton) public Button directionButton;
    @Bind(R.id.shapeButton) public Button shapeButton;

    @Bind(R.id.difficultySpinner) public Spinner difficultySpinner;

    @Bind(R.id.densitySeekBar) public SeekBar densitySeekBar;
    @Bind(R.id.consecutiveAnswerSeekBar) public SeekBar consecutiveAnswerSeekBar;
    @Bind(R.id.stageNumSeekBar) public SeekBar stageNumberSeekBar;
    @Bind(R.id.consecutiveAnswerChangeRuleSeekBar) public SeekBar consecutiveAnswerChangeRuleSeekBar;

    @Bind(R.id.increasingSpeeCheckBox) public CheckBox increasingSpeedCheckBox;

    @Bind(R.id.startButton) public Button startButton;

    private SharedPreferences sharedPref;

    private Config config;

    /**
     * Method to obtain a new Fragment's instance.
     * @return This Fragment instance.
     */
    public static Game1SettingsFragment newInstance() {
        return new Game1SettingsFragment();
    }

    public Game1SettingsFragment() {
        config = new Config();
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
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        readPreferences();
    }

    private void readPreferences() {
        config.setRule(sharedPref.getInt("rule", 1));
        config.setDifficulty(sharedPref.getInt("difficulty", 4));
        config.setDensity(sharedPref.getInt("density", 4));
        config.setConsecutiveAnswer(sharedPref.getInt("consecutiveAnswer", 4));
        config.setStageNumber(sharedPref.getInt("stageNumber", 4));
        config.setConsecutiveAnswerChangeRule(sharedPref.getInt("consecutiveAnswerChangeRule", 6));
        config.setIncreasingSpeed(sharedPref.getBoolean("increasingSpeed", false));

        //TODO set rule radio buttons group
        difficultySpinner.setSelection(config.getDifficulty());
        densitySeekBar.setProgress(config.getDensity());
        consecutiveAnswerSeekBar.setProgress(config.getConsecutiveAnswer());
        stageNumberSeekBar.setProgress(config.getStageNumber());
        consecutiveAnswerChangeRuleSeekBar.setProgress(config.getConsecutiveAnswerChangeRule());
        increasingSpeedCheckBox.setChecked(config.isIncreasingSpeed());
    }

    private void savePreferences() {
        config.setRule(1); //TODO
        config.setDifficulty(difficultySpinner.getSelectedItemPosition());
        config.setDensity(densitySeekBar.getProgress());
        config.setConsecutiveAnswer(consecutiveAnswerSeekBar.getProgress());
        config.setStageNumber(stageNumberSeekBar.getProgress());
        config.setConsecutiveAnswerChangeRule(consecutiveAnswerChangeRuleSeekBar.getProgress());
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

    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        ((StartListener)getActivity()).startGame(GAMETYPE);

        savePreferences();
    }
}
