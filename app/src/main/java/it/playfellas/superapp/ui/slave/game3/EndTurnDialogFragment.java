package it.playfellas.superapp.ui.slave.game3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.ui.ImmersiveDialogFragment;

/**
 * Class that represents a DialogFragment
 * <p></p>
 * Created by Stefano Cappa on 03/09/15.
 */
public class EndTurnDialogFragment extends ImmersiveDialogFragment {

    @Bind(R.id.dialogSlot1ImageView)
    ImageView dialogSlot1ImageView;
    @Bind(R.id.dialogSlot2ImageView)
    ImageView dialogSlot2ImageView;
    @Bind(R.id.dialogSlot3ImageView)
    ImageView dialogSlot3ImageView;
    @Bind(R.id.dialogSlot4ImageView)
    ImageView dialogSlot4ImageView;

    @Bind(R.id.dialogComplete1ImageView)
    ImageView dialogComplete1ImageView;
    @Bind(R.id.dialogComplete2ImageView)
    ImageView dialogComplete2ImageView;
    @Bind(R.id.dialogComplete3ImageView)
    ImageView dialogComplete3ImageView;
    @Bind(R.id.dialogComplete4ImageView)
    ImageView dialogComplete4ImageView;

    final private ImageView[] dialogSlotImageViews = new ImageView[InternalConfig.NO_FIXED_TILES];
    final private ImageView[] dialogCompleteImageViews = new ImageView[InternalConfig.NO_FIXED_TILES];

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static EndTurnDialogFragment newInstance() {
        return new EndTurnDialogFragment();
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

        //call this on this fragment, not on the dialog
        setCancelable(false);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void updateSlotsStack(Tile[] tiles) {
        //init the slots stack in this dialog
        dialogSlotImageViews[0] = dialogSlot1ImageView;
        dialogSlotImageViews[1] = dialogSlot2ImageView;
        dialogSlotImageViews[2] = dialogSlot3ImageView;
        dialogSlotImageViews[3] = dialogSlot4ImageView;

        Slave3Utils.updateImageViewsTower(tiles, dialogSlotImageViews, this.getActivity().getResources());
    }

    public void updateCompleteStack(Tile[] tiles) {
        //init the complete stack in this dialog
        dialogCompleteImageViews[0] = dialogComplete1ImageView;
        dialogCompleteImageViews[1] = dialogComplete2ImageView;
        dialogCompleteImageViews[2] = dialogComplete3ImageView;
        dialogCompleteImageViews[3] = dialogComplete4ImageView;

        Slave3Utils.updateImageViewsTower(tiles, dialogCompleteImageViews, this.getActivity().getResources());
    }
}