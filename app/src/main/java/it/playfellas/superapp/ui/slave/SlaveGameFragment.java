package it.playfellas.superapp.ui.slave;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.events.ui.UIRWEvent;

/**
 * Created by Stefano Cappa on 07/08/15.
 */
public abstract class SlaveGameFragment extends Fragment {

    public void onRightOrWrong(UIRWEvent e) {
        if (e.isRight()) {
            Toast.makeText(this.getActivity(), "Right", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void notifyMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public abstract void pausePresenter();

    public abstract void restartPresenter();

    public void showWaitingDialog() {
        EndStageDialogFragment endStageDialogFragment = getEndStageDiagFragment();
        if (endStageDialogFragment == null) {
            endStageDialogFragment = EndStageDialogFragment.newInstance("title", "message", false);
            endStageDialogFragment.setTargetFragment(this, InternalConfig.ENDSTAGE_DIAG_ID);
            endStageDialogFragment.show(getFragmentManager(), InternalConfig.ENDSTAGE_DIAG_TAG);
            getFragmentManager().executePendingTransactions();
        }
    }

    public void hideWaitingDialog() {
        EndStageDialogFragment endStageDialogFragment = getEndStageDiagFragment();
        if (endStageDialogFragment != null) {
            endStageDialogFragment.dismiss();
        }
    }

    private EndStageDialogFragment getEndStageDiagFragment() {
        return (EndStageDialogFragment) getFragmentManager().findFragmentByTag(InternalConfig.ENDSTAGE_DIAG_TAG);
    }
}
