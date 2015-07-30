package it.playfellas.superapp.activities.master;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;

public class MasterActivity extends AppCompatActivity {

    private static final String TAG = MasterActivity.class.getSimpleName();
    private static final String GAME_NUM_INTENTNAME = "game_num";

    @Bind(R.id.game1_button) public Button game1;
    @Bind(R.id.game2_button) public Button game2;
    @Bind(R.id.game3_button) public Button game3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.game1_button)
    public void onClikGame1(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_NUM_INTENTNAME, 1);
        startActivity(intent);
    }

    @OnClick(R.id.game2_button)
    public void onClikGame2(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_NUM_INTENTNAME, 2);
        startActivity(intent);
    }

    @OnClick(R.id.game3_button)
    public void onClikGame3(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_NUM_INTENTNAME, 3);
        startActivity(intent);
    }
}
