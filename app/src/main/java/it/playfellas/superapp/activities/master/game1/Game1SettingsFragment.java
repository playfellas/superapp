package it.playfellas.superapp.activities.master.game1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.SettingsFragment;
import it.playfellas.superapp.activities.master.StartGameListener;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.Config1;


public class Game1SettingsFragment extends SettingsFragment {

    public static final String TAG = "Game1SettingsFragment";

    private static final String RULE = "rule";
    private static final String RULE_CHANGE = "ruleChange";

    @Bind(R.id.ruleGroup)
    public RadioGroup ruleRadioGroup;


    @Bind(R.id.ruleChangeSeekBar)
    public SeekBar invertGameSeekBar;


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
    protected int getPreferencesId() {
        return R.string.preference_key_game1;
    }

    @Override
    public void onStartGame(StartGameListener l) {
        l.startGame1(config);
    }

    @Override
    protected Config newConfig() {
        config = new Config1();

        //update the config object in the super class with other parameters
        config.setRule(super.sharedPref.getInt(RULE, 0));
        config.setRuleChange(super.sharedPref.getInt(RULE_CHANGE, 6));

        return config;
    }

    @Override
    protected void showPreferences() {
        //update specific gui elements for this Fragment using parameter in superclass Config object
        setRuleRadioGroup(config.getRule());
        invertGameSeekBar.setProgress(config.getRuleChange());
    }

    /**
     * This method set the check RadioButton in a RadioGroup by index
     *
     * @param index The index of the element, from 0 to size - 1 of the RadioGroup
     */
    private void setRuleRadioGroup(int index) {
        ((RadioButton) ruleRadioGroup.getChildAt(index)).setChecked(true);
    }

    @Override
    protected Config setPreferences(SharedPreferences.Editor editor) {
        //update the config object in the super class with other parameters
        config.setRule(getCheckedRule());
        config.setRuleChange(invertGameSeekBar.getProgress());

        //update specific settings elements before save
        editor.putInt(RULE, config.getRule());
        editor.putInt(RULE_CHANGE, config.getRuleChange());
        return config;
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
}