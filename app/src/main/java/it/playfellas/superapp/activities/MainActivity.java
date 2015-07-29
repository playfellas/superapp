package it.playfellas.superapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.activities.master.MasterActivity;
import it.playfellas.superapp.activities.slave.SlaveActivity;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.masterButton)
    Button masterButton;

    @Bind(R.id.slaveButton)
    Button slaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.masterButton)
    public void onClikMasterButton(View view) {
        Intent intent = new Intent(this, MasterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.slaveButton)
    public void onClikSlaveButton(View view) {
        Intent intent = new Intent(this, SlaveActivity.class);
        startActivity(intent);
    }
}
