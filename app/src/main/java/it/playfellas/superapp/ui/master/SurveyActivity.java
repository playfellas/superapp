package it.playfellas.superapp.ui.master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;

public class SurveyActivity extends AppCompatActivity {

    public static final String CONFIG = "config";

    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_survey_activity);
        config = (Config) getIntent().getSerializableExtra(CONFIG);
    }
}
