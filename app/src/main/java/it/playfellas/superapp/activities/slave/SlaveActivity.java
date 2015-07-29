package it.playfellas.superapp.activities.slave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;

public class SlaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        ButterKnife.bind(this);

        SlaveFragment masterFragment = SlaveFragment.newInstance();


        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.slave_root_container, masterFragment, SlaveFragment.TAG)
                .commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }
}
