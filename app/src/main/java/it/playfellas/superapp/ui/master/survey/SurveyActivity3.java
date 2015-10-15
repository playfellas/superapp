package it.playfellas.superapp.ui.master.survey;

import java.util.HashMap;
import java.util.Map;

import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config3;

public class SurveyActivity3 extends SurveyActivity {

    private Config3 config;

    @Override
    protected Map<Integer, Question> addQuestions() {
        config = (Config3) getIntent().getSerializableExtra(CONFIG);
        return new HashMap<>();
    }

    @Override
    protected void setSpecificContentView() {
        setContentView(R.layout.master_survey_activity3);
    }
}
