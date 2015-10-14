package it.playfellas.superapp.ui.master.survey;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.logic.Config2;

public class SurveyActivity2 extends SurveyActivity {

    private Config2 config;

    @Override
    protected List<Question> addQuestions() {
        config = (Config2) getIntent().getSerializableExtra(CONFIG);
        return new ArrayList<>();
    }
}
