package it.playfellas.superapp.ui.slave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;

/**
 * This fragment has an indeterminate progress bar.
 * This class hasn't any methods to change the fragment.
 */
public class WaitingFragment extends Fragment {

    public static final String TAG = WaitingFragment.class.getSimpleName();

    @Bind(R.id.waitingTextView)
    public TextView waitingTextView;

    private static String message = null;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static WaitingFragment newInstance(String msg) {
        message = msg;
        return new WaitingFragment();
    }

    public WaitingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slave_waiting, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //if the message received in newInstance is different than null i use this parameter to
        //set the textView. Otherwise i don't set anything, because this fragment will display the original
        //default message added in the slave_waiting.xml file.
        if(message!=null) {
            waitingTextView.setText(message);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
