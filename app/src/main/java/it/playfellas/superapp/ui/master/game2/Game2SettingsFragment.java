package it.playfellas.superapp.ui.master.game2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.SettingsFragment;
import it.playfellas.superapp.ui.master.StartGameListener;


public class Game2SettingsFragment extends SettingsFragment {

    public static final String TAG = "Game2SettingsFragment";

    private static final String GAME_MODE = "gameMode";

    @Bind(R.id.ruleGroup)
    RadioGroup ruleRadioGroup;

    private Config2 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game2SettingsFragment newInstance() {
        return new Game2SettingsFragment();
    }

    public Game2SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master_game2_settings_fragment, container, false);

        ButterKnife.bind(this, rootView);

        super.scorePerStageSeekBar.setEnabled(false);

        return rootView;
    }

    @Override
    protected int getPreferencesId() {
        return R.string.preference_key_game2;
    }

    @Override
    public void onStartGame(StartGameListener l) {
        l.startGame2(config);
    }

    @Override
    protected Config newConfig() {
        config = new Config2();

        //update the config object in the super class with other parameters
        config.setGameMode(super.sharedPref.getInt(GAME_MODE, 0));
        config.setMaxScore(InternalConfig.NO_FIXED_TILES * TenBus.get().noDevices());

        return config;
    }

    @Override
    protected void showPreferences() {
        //update specific gui elements for this Fragment using parameter in superclass Config object
        setRuleRadioGroup(config.getGameMode());
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
        config.setGameMode(getCheckedRule());
        config.setMaxScore(InternalConfig.NO_FIXED_TILES * TenBus.get().noDevices());
        //update specific settings elements before save
        editor.putInt(GAME_MODE, config.getGameMode());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}