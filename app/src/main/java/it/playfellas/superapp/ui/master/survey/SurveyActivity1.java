package it.playfellas.superapp.ui.master.survey;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;

public class SurveyActivity1 extends SurveyActivity {

    public static final String MAX_SCORE_ID = "maxScore";
    public static final String RULE_CHANGE_ID = "ruleChange";

    private Config1 config;


    @Override
    protected Map<Integer, Question> addQuestions() {
        config = (Config1) getIntent().getSerializableExtra(CONFIG);
        Map<Integer, Question> questions = new HashMap<>();
        questions.put(View.generateViewId(), new RadioQuestion(
                MAX_SCORE_ID,
                getString(R.string.config_max_score),
                String.valueOf((config).getMaxScore())
        ));
        questions.put(View.generateViewId(), new RadioQuestion(
                RULE_CHANGE_ID,
                getString(R.string.config_rule_change),
                String.valueOf((config).getRuleChange())
        ));
        return questions;
    }
}
