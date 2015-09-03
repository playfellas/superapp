package it.playfellas.superapp.ui.slave.game3;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Class that represents a DialogFragment
 * <p></p>
 * Created by Stefano Cappa on 03/09/15.
 */
public class EndTurnDialogFragment extends DialogFragment {

    @Bind(R.id.dialogTurnTextView)
    TextView dialogTurnTextView;

    @Bind(R.id.dialogSlot1ImageView)
    ImageView dialogSlot1ImageView;
    @Bind(R.id.dialogSlot2ImageView)
    ImageView dialogSlot2ImageView;
    @Bind(R.id.dialogSlot3ImageView)
    ImageView dialogSlot3ImageView;
    @Bind(R.id.dialogSlot4ImageView)
    ImageView dialogSlot4ImageView;

    private static String titleString;
    private static String messageString;
    final private ImageView[] dialogSlotImageViews = new ImageView[InternalConfig.NO_FIXED_TILES];


    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static EndTurnDialogFragment newInstance(String title, String message) {
        titleString = title;
        messageString = message;
        return new EndTurnDialogFragment();
    }

    //call this method to init in this dialog
    private void init() {
        //init the slots stack in this dialog
        dialogSlotImageViews[0] = dialogSlot1ImageView;
        dialogSlotImageViews[1] = dialogSlot2ImageView;
        dialogSlotImageViews[2] = dialogSlot3ImageView;
        dialogSlotImageViews[3] = dialogSlot4ImageView;
    }

    /**
     * Default Fragment constructor.
     */
    public EndTurnDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slave_endturn_dialog, container, false);

        ButterKnife.bind(this, v);

        getDialog().setTitle(titleString);
        getDialog().setCanceledOnTouchOutside(false);

        //call this on this fragment, not on the dialog
        setCancelable(false);

        dialogTurnTextView.setText(messageString);

        return v;
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

    public void updateSlotsStack(Tile[] tiles) {
        this.init();
        Slave3Utils.updateSlotsTower(tiles, dialogSlotImageViews, this.getActivity().getResources());
    }
}