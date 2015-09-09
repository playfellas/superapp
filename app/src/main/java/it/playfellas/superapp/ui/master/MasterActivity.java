package it.playfellas.superapp.ui.master;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.network.TenBus;

public class MasterActivity extends ImmersiveAppCompatActivity {

    private static final String TAG = MasterActivity.class.getSimpleName();
    private static final String GAME_NUM_INTENTNAME = "game_num";

    @Bind(R.id.game1_button)
    Button game1;
    @Bind(R.id.game2_button)
    Button game2;
    @Bind(R.id.game3_button)
    Button game3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.activity_master);
        ButterKnife.bind(this);
        TenBus.get().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.game1_button)
    public void onClikGame1(View view) {
        startActivity(getGameActivityIntent(1));
    }

    @OnClick(R.id.game2_button)
    public void onClikGame2(View view) {
        startActivity(getGameActivityIntent(2));
    }

    @OnClick(R.id.game3_button)
    public void onClikGame3(View view) {
        startActivity(getGameActivityIntent(3));
    }

    private Intent getGameActivityIntent(int game) {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putInt(GAME_NUM_INTENTNAME, game);
        intent.putExtra("masterActivity", b);
        return intent;
    }
}
