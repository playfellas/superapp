package it.playfellas.superapp.ui.slave;

import android.app.Dialog;
import android.os.Bundle;
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

/**
 * Class that represents a DialogFragment
 * <p></p>
 * Created by Stefano Cappa on 07/08/15.
 */
public class EndStageDialogFragment extends DialogFragment {

    @Bind(R.id.endStageWaitingTextView)
    TextView endStageWaitingTextView;

    private static String messageString;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static EndStageDialogFragment newInstance(String message) {
        messageString = message;
        return new EndStageDialogFragment();
    }

    /**
     * Default Fragment constructor.
     */
    public EndStageDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slave_endstage_dialog, container, false);

        ButterKnife.bind(this, v);

        //call this on this fragment, not on the dialog
        setCancelable(false);

        endStageWaitingTextView.setText(messageString);

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            this.getDialog().getWindow().setLayout(width, height);
        }
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