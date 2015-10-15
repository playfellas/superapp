package it.playfellas.superapp.ui.master.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.master.GameSurvey;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.ui.master.MasterActivity;

public abstract class SurveyActivity extends AppCompatActivity {

    public static final String CONFIG = "config";
    public static final String GAME_ID = "game_id";
    public static final String MASTER_CLASS = "master_class";

    public static final String EDUCATOR_ID = "educator";
    public static final String DIFFICULTY_LEVEL_ID = "difficultyLevel";
    public static final String TILE_DENSITY_ID = "tileDensity";
    public static final String NO_STAGES_ID = "noStages";
    public static final String NOTES_ID = "notes";

    private Config config;
    private MasterController master;
    private GameSurvey gameSurvey;
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
        initGameSurvey();
    }

    protected void initGameSurvey() {
        String gameId = getIntent().getStringExtra(GAME_ID);
        Class<? extends MasterController> masterClass = (Class<? extends MasterController>) getIntent().getSerializableExtra(MASTER_CLASS);
        gameSurvey = new GameSurvey(new Firebase(InternalConfig.FIREBASE_URL), gameId, masterClass);
    }


    @Override
    public void onBackPressed() {
        return;
    }

    private void initUi() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        Space topSpace = new Space(this);
        topSpace.setMinimumHeight(50);
        questionsLinearLayout.addView(topSpace);

        for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
            Question question = entry.getValue();
            int cardViewId = entry.getKey();
            CardView cardView = (CardView) layoutInflater.inflate(question.getLayout(), questionsLinearLayout, false);
            cardView.setId(cardViewId);
            questionsLinearLayout.addView(cardView);
            ((TextView) cardView.findViewById(R.id.questionTextView)).setText(question.getQuestionText());
            TextView actualTextView = (TextView) cardView.findViewById(R.id.actualValueTextView);
            // If actualValueTextView is null the question is a text one, so don't set the actual value text
            if (actualTextView != null) {
                actualTextView.setText(question.getActualValueText(this));
            }
        }
        Space bottomSpace = new Space(this);
        bottomSpace.setMinimumHeight(50);
        questionsLinearLayout.addView(bottomSpace);
    }

    private Map<Integer, Question> prepareQuestions() {
        config = (Config) getIntent().getSerializableExtra(CONFIG);
        questions = new LinkedHashMap<>();
        questions.put(View.generateViewId(), new TextQuestion(EDUCATOR_ID, "Nome Educatore"));
        questions.put(View.generateViewId(), new RadioQuestion(DIFFICULTY_LEVEL_ID, getString(R.string.config_difficulty_level).toLowerCase(), String.valueOf(config.getDifficultyLevel())));
        questions.put(View.generateViewId(), new RadioQuestion(TILE_DENSITY_ID, getString(R.string.config_tile_density).toLowerCase(), String.valueOf(config.getTileDensity())));
        questions.put(View.generateViewId(), new RadioQuestion(NO_STAGES_ID, getString(R.string.config_no_stages).toLowerCase(), String.valueOf(config.getNoStages())));
        questions.putAll(addQuestions());
        questions.put(View.generateViewId(), new TextQuestion(NOTES_ID, "Note"));
        return questions;
    }

    @OnClick(R.id.sendSurveyButton)
    public void onSendButtonClicked(View view) {
        for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
            Question question = entry.getValue();
            int cardViewId = entry.getKey();
            CardView cardView = (CardView) findViewById(cardViewId);
            // Check if actualValueTextView is present to determine if it is a test or radio question
            switch (question.getID()) {
                case EDUCATOR_ID:
                    String educatorName = ((EditText) cardView.findViewById(R.id.answerEditText)).getText().toString();
                    gameSurvey.setEducatorName(educatorName);
                    break;
                case NOTES_ID:
                    String notes = ((EditText) cardView.findViewById(R.id.answerEditText)).getText().toString();
                    gameSurvey.setAdditionalNotes(notes);
                    break;
                default:
                    RadioGroup radioGroup = ((RadioGroup) cardView.findViewById(R.id.answerRadioGroup));
                    int radioSelectedId = radioGroup.getCheckedRadioButtonId();
                    switch (radioSelectedId) {
                        case R.id.lowerRadioButton:
                            gameSurvey.answer(question.getID(), question.getLongName(), ((RadioQuestion) question).getItWas(), GameSurvey.Answers.LT);
                            break;
                        case R.id.equalRadioButton:
                            gameSurvey.answer(question.getID(), question.getLongName(), ((RadioQuestion) question).getItWas(), GameSurvey.Answers.EQ);
                            break;
                        case R.id.greaterRadioButton:
                            gameSurvey.answer(question.getID(), question.getLongName(), ((RadioQuestion) question).getItWas(), GameSurvey.Answers.GT);
                            break;
                    }
                    break;
            }
        }
        gameSurvey.save();
        Intent i = new Intent(this, MasterActivity.class);
        startActivity(i);
    }

    protected abstract Map<Integer, Question> addQuestions();
}
