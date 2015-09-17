package it.playfellas.superapp.ui.slave;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.GameDialogFragment;

/**
 * Class that represents a DialogFragment
 * <p></p>
 * Created by Stefano Cappa on 07/08/15.
 */
public class EndStageDialogFragment extends GameDialogFragment {

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

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar.getIndeterminateDrawable().setColorFilter(
                this.getActivity().getResources().getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);
    }
}