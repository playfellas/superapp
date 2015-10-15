package it.playfellas.superapp.ui.master.survey;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;

public abstract class SurveyActivity extends ImmersiveAppCompatActivity {

    public static final String CONFIG = "config";
    public static final String EDUCATOR_ID = "educator";
    public static final String DIFFICULTY_LEVEL_ID = "difficultyLevel";
    public static final String TILE_DENSITY_ID = "tileDensity";
    public static final String NO_STAGES_ID = "noStages";
    public static final String NOTES_ID = "notes";

    private Config config;
    private Map<Integer, Question> questions;

    @Bind(R.id.questionsLinearLayout)
    LinearLayout questionsLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_survey_activity);
        ButterKnife.bind(this);
        prepareQuestions();
        initUi();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void initUi() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
            Question question = entry.getValue();
            int cardViewId = entry.getKey();
            CardView cardView = (CardView) layoutInflater.inflate(question.getLayout(), null, false);
            cardView.setId(cardViewId);
            questionsLinearLayout.addView(cardView);
            ((TextView) cardView.findViewById(R.id.questionTextView)).setText(question.getQuestionText());
            TextView actualTextView = (TextView) cardView.findViewById(R.id.actualValueTextView);
            // If actualValueTextView is null the question is a text one, so don't set the actual value text
            if (actualTextView != null) {
                actualTextView.setText(question.getActualValueText(this));
            }
        }
    }

    private Map<Integer, Question> prepareQuestions() {
        config = (Config) getIntent().getSerializableExtra(CONFIG);
        questions = new HashMap<>();
        questions.put(View.generateViewId(), new TextQuestion(EDUCATOR_ID, "Nome Educatore"));
        questions.put(View.generateViewId(), new RadioQuestion(DIFFICULTY_LEVEL_ID, getString(R.string.config_difficulty_level).toLowerCase(), String.valueOf(config.getDifficultyLevel())));
        questions.put(View.generateViewId(), new RadioQuestion(TILE_DENSITY_ID, getString(R.string.config_tile_density).toLowerCase(), String.valueOf(config.getTileDensity())));
        questions.put(View.generateViewId(), new RadioQuestion(NO_STAGES_ID, getString(R.string.config_no_stages).toLowerCase(), String.valueOf(config.getNoStages())));
        questions.putAll(addQuestions());
        questions.put(View.generateViewId(), new TextQuestion(NOTES_ID, "Note"));
        return questions;
    }

    protected abstract Map<Integer, Question> addQuestions();
}
