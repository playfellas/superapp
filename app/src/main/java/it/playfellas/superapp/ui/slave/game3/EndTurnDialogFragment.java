package it.playfellas.superapp.ui.slave.game3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import it.playfellas.superapp.logic.tiles.TileSize;
import it.playfellas.superapp.ui.BitmapUtils;

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
    private TileSize[] tileSizes;
    final private ImageView[] dialogSlotsImageView = new ImageView[InternalConfig.NO_FIXED_TILES];


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

    //call this method to init in this dialog.
    //You shouldn't use this to show the fragment.
    //This should be used only one time.
    public void init(TileSize[] tileSizes) {
        //init the tower to complete
        dialogSlotsImageView[0] = dialogSlot1ImageView;
        dialogSlotsImageView[1] = dialogSlot2ImageView;
        dialogSlotsImageView[2] = dialogSlot3ImageView;
        dialogSlotsImageView[3] = dialogSlot4ImageView;

        this.tileSizes = tileSizes;
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

    public void updateSlotsTower(Tile[] tiles) {
        for (int i = 0; i < tiles.length && tiles[i] != null; i++) {
            dialogSlotsImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(getBitmapFromResId(tiles[i]), tileSizes[i].getMultiplier(), Color.TRANSPARENT));
        }
    }

    private Bitmap getBitmapFromResId(Tile t) {
        int resId = this.getActivity().getResources().getIdentifier(t.getName(), InternalConfig.DRAWABLE_RESOURCE, InternalConfig.PACKAGE_NAME);
        return BitmapFactory.decodeResource(this.getActivity().getResources(), resId);
    }
}