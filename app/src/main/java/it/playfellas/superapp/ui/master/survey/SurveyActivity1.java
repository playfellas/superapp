package it.playfellas.superapp.ui.master.survey;

import java.util.Arrays;
import java.util.List;

import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;

public class SurveyActivity1 extends SurveyActivity {

    public static final String RULE_CHANGE_ID = "ruleChange";
    public static final String MAX_SCORE_ID = "maxScore";

    private Config1 config;

    @Override
    protected List<Question> addQuestions() {
        config = (Config1) getIntent().getSerializableExtra(CONFIG);
        return Arrays.<Question>asList(
                new RadioQuestion(
                        MAX_SCORE_ID,
                        getString(R.string.config_max_score),
                        String.valueOf((config).getMaxScore())
                ),
                new RadioQuestion(
                        RULE_CHANGE_ID,
                        getString(R.string.config_rule_change),
                        String.valueOf((config).getRuleChange())
                )
        );
    }
}
