package it.playfellas.superapp.activities.slave;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.playfellas.superapp.R;

/**
 * The SlaveFragment
 */
public class SlaveFragment extends Fragment {

    public static final String TAG = SlaveFragment.class.getSimpleName();

    /**
     * Method to obtain a new Fragment's instance.
     * @return This Fragment instance.
     */
    public static SlaveFragment newInstance() {
        return new SlaveFragment();
    }

    public SlaveFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slave, container, false);

        //TODO USE PHOTO ACTIVITY HERE
    }
}
