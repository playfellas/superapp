package it.playfellas.superapp.ui.master.survey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;

public abstract class SurveyActivity extends AppCompatActivity {

    public static final String CONFIG = "config";
    public static final String EDUCATOR_ID = "educator";
    public static final String DIFFICULTY_LEVEL_ID = "difficultyLevel";
    public static final String TILE_DENSITY_ID = "tileDensity";
    public static final String NO_STAGES_ID = "noStages";
    public static final String NOTES_ID = "notes";

    private Config config;
    private Map<Integer, Question> questions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSpecificContentView();
        prepareQuestions();
        initUi();
    }

    private void initUi() {
        for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
            ((TextView) findViewById(entry.getKey()).findViewById(R.id.questionTextView)).setText(entry.getValue().getQuestionText());
            TextView actualTextView = (TextView) findViewById(entry.getKey()).findViewById(R.id.actualValueTextView);
            // If actualValueTextView is null the question is a text one, so don't set the actual value text
            if (actualTextView != null) {
                actualTextView.setText(entry.getValue().getActualValueText(this));
            }
        }
    }

    private Map<Integer, Question> prepareQuestions() {
        config = (Config) getIntent().getSerializableExtra(CONFIG);
        questions = new HashMap<>();
        questions.put(R.id.educatorCard, new TextQuestion(EDUCATOR_ID, "Nome Educatore"));
        questions.put(R.id.difficultyLevelCard, new RadioQuestion(DIFFICULTY_LEVEL_ID, getString(R.string.config_difficulty_level).toLowerCase(), String.valueOf(config.getDifficultyLevel())));
        questions.put(R.id.tileDensityCard, new RadioQuestion(TILE_DENSITY_ID, getString(R.string.config_tile_density).toLowerCase(), String.valueOf(config.getTileDensity())));
        questions.put(R.id.noStagesCard, new RadioQuestion(NO_STAGES_ID, getString(R.string.config_no_stages).toLowerCase(), String.valueOf(config.getNoStages())));
        questions.putAll(addQuestions());
        questions.put(R.id.notesCard, new TextQuestion(NOTES_ID, "Note"));
        return questions;
    }

    protected abstract Map<Integer, Question> addQuestions();

    protected abstract void setSpecificContentView();
}
