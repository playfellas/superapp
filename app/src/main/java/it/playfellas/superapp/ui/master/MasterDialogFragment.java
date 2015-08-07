package it.playfellas.superapp.ui.master;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;

/**
 * Class that represents the DialogFragment to ask confirmation.
 * <p></p>
 * Created by Stefano Cappa on 07/08/15.
 */
public class MasterDialogFragment extends DialogFragment {

    @Bind(R.id.confirmButton)
    public Button confirmButton;
    @Bind(R.id.noButton)
    public Button noButton;
    @Bind(R.id.dialogStageLabel)
    public Button dialogStageTextView;

    private static String titleString;
    private static String messageString;
    private static boolean areYourSureBoolean;

    /**
     * {@link it.playfellas.superapp.ui.master.GameFragment} implements this interface.
     */
    public interface DialogConfirmListener {
        void yesButtonPressed();

        void noButtonPressed();

        void yesButtonAreYouSurePressed();

        void noButtonAreYouSurePressed();
    }

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static MasterDialogFragment newInstance(String title, String message, boolean areYouSure) {
        titleString = title;
        messageString = message;
        areYourSureBoolean = areYouSure;
        return new MasterDialogFragment();
    }

    /**
     * Default Fragment constructor.
     */
    public MasterDialogFragment() {
    }


    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setOnDismissListener(null);
        }
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.master_dialog, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, v);

        getDialog().setTitle(titleString);
        dialogStageTextView.setText(messageString);

        return v;
    }

    @OnClick(R.id.confirmButton)
    public void onConfirmButtonClick(View view) {
        if (!areYourSureBoolean) {
            ((DialogConfirmListener) getTargetFragment()).yesButtonPressed();
        } else {
            ((DialogConfirmListener) getTargetFragment()).yesButtonAreYouSurePressed();
        }
        this.dismiss();
    }

    @OnClick(R.id.noButton)
    public void onNoButtonClick(View view) {
        if (!areYourSureBoolean) {
            ((DialogConfirmListener) getTargetFragment()).noButtonPressed();
        } else {
            ((DialogConfirmListener) getTargetFragment()).noButtonAreYouSurePressed();
        }
        this.dismiss();
    }
}
