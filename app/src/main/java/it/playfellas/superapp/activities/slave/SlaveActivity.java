package it.playfellas.superapp.activities.slave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        ButterKnife.bind(this);

        PhotoFragment photoFragment = PhotoFragment.newInstance();

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.slave_root_container, photoFragment, PhotoFragment.TAG)
                .commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }
}
