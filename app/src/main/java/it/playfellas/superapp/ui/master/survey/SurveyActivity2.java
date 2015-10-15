package it.playfellas.superapp.ui.master.survey;

import java.util.HashMap;
import java.util.Map;

import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config2;

public class SurveyActivity2 extends SurveyActivity {

    private Config2 config;

    @Override
    protected Map<Integer, Question> addQuestions() {
        config = (Config2) getIntent().getSerializableExtra(CONFIG);
        return new HashMap<>();
    }

    @Override
    protected void setSpecificContentView() {
        setContentView(R.layout.master_survey_activity2);
    }
}
