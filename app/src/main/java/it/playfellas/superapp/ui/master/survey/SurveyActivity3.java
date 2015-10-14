package it.playfellas.superapp.ui.master.survey;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.logic.Config3;

public class SurveyActivity3 extends SurveyActivity {

    private Config3 config;

    @Override
    protected List<Question> addQuestions() {
        config = (Config3) getIntent().getSerializableExtra(CONFIG);
        return new ArrayList<>();
    }
}
