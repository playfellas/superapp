package it.playfellas.superapp.ui.master;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.ImmersiveDialogFragment;

/**
 * Created by Stefano Cappa on 05/09/15.
 */
public class MasterTimerDialogFragment extends ImmersiveDialogFragment {

    @Bind(R.id.countDownTextView)
    TextView countDownTextView;

    public interface DialogTimerListener {
        void onCountdownFinished();
    }

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static MasterTimerDialogFragment newInstance() {
        return new MasterTimerDialogFragment();
    }

    /**
     * Default Fragment constructor.
     */
    public MasterTimerDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.master_timer_endstage_dialog, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, v);

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

        //set the immersive mode to hide the navigation bar
        super.setImmersive();

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setOnDismissListener(null);
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
