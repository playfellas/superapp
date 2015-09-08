package it.playfellas.superapp.ui.master;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;

/**
 * Created by Stefano Cappa on 05/09/15.
 */
public class MasterTimerDialogFragment extends DialogFragment {

    @Bind(R.id.countDownTextView)
    TextView countDownTextView;

    private static String titleString;

    public interface DialogTimerListener {
        void onCountdownFinished();
    }

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static MasterTimerDialogFragment newInstance(String title) {
        titleString = title;
        return new MasterTimerDialogFragment();
    }

    /**
     * Default Fragment constructor.
     */
    public MasterTimerDialogFragment() {
    }


    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setOnDismissListener(null);
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.master_timer_endstage_dialog, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, v);

        getDialog().setTitle(titleString);
        getDialog().setCanceledOnTouchOutside(false);
        countDownTextView.setText("5");

        //call this on this fragment, not on the dialog
        setCancelable(false);

        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTextView.setText((millisUntilFinished / 1000) + "");
            }

            public void onFinish() {
                countDownTextView.setText("Avvio in corso...");
                //Go back to che caller fragment.
                ((DialogTimerListener) getTargetFragment()).onCountdownFinished();
                //and dismiss
                dismiss();
            }
        };
        countDownTimer.start();

        return v;
    }
}
