package it.playfellas.superapp.ui.master.survey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Bind(R.id.surveyRecyclerView)
    RecyclerView surveyRecyclerView;
    private RecyclerView.Adapter surveyAdapter;
    private RecyclerView.LayoutManager surveyLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_survey_activity);
        ButterKnife.bind(this);

        initRecycletView();
    }

    private void initRecycletView() {
        surveyRecyclerView.setHasFixedSize(true);

        surveyLayoutManager = new LinearLayoutManager(this);
        surveyRecyclerView.setLayoutManager(surveyLayoutManager);

        surveyAdapter = new SurveyAdapter(prepareQuestions());
        surveyRecyclerView.setAdapter(surveyAdapter);
    }

    private List<Question> prepareQuestions() {
        config = (Config) getIntent().getSerializableExtra(CONFIG);
        List<Question> questions = new ArrayList<>();
        questions.add(new TextQuestion(EDUCATOR_ID, "Nome Educatore"));
        questions.add(new RadioQuestion(DIFFICULTY_LEVEL_ID, getString(R.string.config_difficulty_level).toLowerCase(), String.valueOf(config.getDifficultyLevel())));
        questions.add(new RadioQuestion(TILE_DENSITY_ID, getString(R.string.config_tile_density).toLowerCase(), String.valueOf(config.getTileDensity())));
        questions.add(new RadioQuestion(NO_STAGES_ID, getString(R.string.config_no_stages).toLowerCase(), String.valueOf(config.getNoStages())));
        questions.addAll(addQuestions());
        questions.add(new TextQuestion(EDUCATOR_ID, "Note"));
        return questions;
    }

    protected abstract List<Question> addQuestions();
}
