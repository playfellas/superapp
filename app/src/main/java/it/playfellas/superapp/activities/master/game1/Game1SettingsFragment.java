package it.playfellas.superapp.activities.master.game1;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.SettingsFragment;
import it.playfellas.superapp.activities.master.StartGameListener;
import it.playfellas.superapp.logic.Config1;


public class Game1SettingsFragment extends SettingsFragment {

    public static final String TAG = Game1SettingsFragment.class.getSimpleName();

    @Bind(R.id.ruleGroup)
    public RadioGroup ruleRadioGroup;


    @Bind(R.id.invertGameSeekBar)
    public SeekBar invertGameSeekBar;


    @Bind(R.id.startButton)
    public Button startButton;

    private StartGameListener mListener;



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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        super.sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_key_game1), Context.MODE_PRIVATE);

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
        super.config = new Config1();

        super.readPreferences();


        ((Config1)super.config).setRule(super.sharedPref.getInt("rule", 0));
        ((Config1)super.config).setRuleChange(sharedPref.getInt("consecutiveAnswerChangeRule", 6));

        setRuleRadioGroup(((Config1)super.config).getRule());
        invertGameSeekBar.setProgress(((Config1)super.config).getRuleChange());
    }

    @Override
    public void savePreferences() {
        ((Config1)super.config).setRule(getCheckedRule());
        ((Config1)super.config).setRuleChange(invertGameSeekBar.getProgress());

        super.savePreferences();

        super.editor.putInt("rule", ((Config1)super.config).getRule());
        super.editor.putInt("consecutiveAnswerChangeRule", ((Config1)super.config).getRuleChange());

        super.editor.apply();
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


    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        if (mListener != null) {
            this.savePreferences();
            mListener.startGame1();
        }
    }
}