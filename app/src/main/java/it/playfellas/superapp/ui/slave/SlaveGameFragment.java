package it.playfellas.superapp.ui.slave;

import android.support.v4.app.Fragment;
import android.widget.Toast;

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
        boolean areYouSureDialog = false;
        EndStageDialogFragment endStageDialogFragment = (EndStageDialogFragment) getFragmentManager()
                .findFragmentByTag("endStageDialogFragment");

        if (endStageDialogFragment == null) {
            endStageDialogFragment = EndStageDialogFragment.newInstance("title", "message", areYouSureDialog);
            endStageDialogFragment.setTargetFragment(this, 1);

            endStageDialogFragment.show(getFragmentManager(), "endStageDialogFragment");
            getFragmentManager().executePendingTransactions();
        }
    }

    public void hideWaitingDialog() {
        EndStageDialogFragment endStageDialogFragment = (EndStageDialogFragment) getFragmentManager()
                .findFragmentByTag("endStageDialogFragment");
        if (endStageDialogFragment != null) {
            endStageDialogFragment.dismiss();
        }
    }
}
