package it.playfellas.superapp.ui.slave;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.ImmersiveDialogFragment;

/**
 * Class that represents a DialogFragment
 * <p></p>
 * Created by Stefano Cappa on 07/08/15.
 */
public class EndStageDialogFragment extends ImmersiveDialogFragment {

    @Bind(R.id.progressBarEndStageDiagFrag)
    ProgressBar progressBar;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static EndStageDialogFragment newInstance() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar.getIndeterminateDrawable().setColorFilter(
                this.getActivity().getResources().getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);
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