package it.playfellas.superapp.ui.slave.game3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.ui.slave.Conveyor;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame3Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame3Fragment.class.getSimpleName();


    @Bind(R.id.downConveyor)
    RelativeLayout downConveyorLayout;
    @Bind(R.id.photoImageView)
    ImageView photoImageView;

    @Bind(R.id.stackButtonImageView)
    ImageView stackButtonImageView;
    @Bind(R.id.slot1ImageView)
    ImageView slot1ImageView;
    @Bind(R.id.slot2ImageView)
    ImageView slot2ImageView;
    @Bind(R.id.slot3ImageView)
    ImageView slot3ImageView;
    @Bind(R.id.slot4ImageView)
    ImageView slot4ImageView;

    @Bind(R.id.complete1ImageView)
    ImageView complete1ImageView;
    @Bind(R.id.complete2ImageView)
    ImageView complete2ImageView;
    @Bind(R.id.complete3ImageView)
    ImageView complete3ImageView;
    @Bind(R.id.complete4ImageView)
    ImageView complete4ImageView;

    private static Bitmap photo;

    @Getter
    private Conveyor conveyorUp;
    @Getter
    private Conveyor conveyorDown;

    protected static Config3 config;
    protected static TileSelector db;
    private Slave3Presenter slave3Presenter;
    final private ImageView[] slotImageViews = new ImageView[InternalConfig.NO_FIXED_TILES];
    private ImageView[] completeImageViews = new ImageView[InternalConfig.NO_FIXED_TILES];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game3_fragment, container, false);

        ButterKnife.bind(this, root);

        Log.d(TAG, "Creating Converyors...");

        conveyorDown = new Conveyor(downConveyorLayout, 100, Conveyor.RIGHT);
        conveyorDown.start();

        photoImageView.setImageBitmap(photo);

        //init the tower to complete
        slotImageViews[0] = slot1ImageView;
        slotImageViews[1] = slot2ImageView;
        slotImageViews[2] = slot3ImageView;
        slotImageViews[3] = slot4ImageView;

        //init the complete tower
        completeImageViews[0] = complete1ImageView;
        completeImageViews[1] = complete2ImageView;
        completeImageViews[2] = complete3ImageView;
        completeImageViews[3] = complete4ImageView;

        return root;
    }

    @Override
    public void onDestroyView() {
        if (conveyorDown != null) {
            conveyorDown.clear();
            conveyorDown.stop();
        }

        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame3Fragment newInstance(TileSelector ts, Config3 config3, Bitmap photoBitmap) {
        SlaveGame3Fragment fragment = new SlaveGame3Fragment();
        db = ts;
        config = config3;
        photo = photoBitmap;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.pausePresenter();
        this.slave3Presenter = new Slave3Presenter(db, this, config);
        this.slave3Presenter.startTileDisposer();

        stackButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Stack clicked");
                //send event to remove an image from the slots tower
                slave3Presenter.stackClicked();
            }
        });

    }

    @Override
    public void pausePresenter() {
        if (this.slave3Presenter != null) {
            this.slave3Presenter.pause();
        }
    }

    @Override
    public void restartPresenter() {
        if (this.slave3Presenter != null) {
            this.slave3Presenter.restart();
        }
    }


    public void showEndTurnDialog() {
        EndTurnDialogFragment endTurnDialogFragment = this.findEndTurnDialog();
        if (endTurnDialogFragment == null) {
            endTurnDialogFragment = EndTurnDialogFragment.newInstance("title", "message");
            //TODO add the 3 in InternalConfig
            endTurnDialogFragment.setTargetFragment(this, 3);

            //TODO add the endTurnDialogFragment in InternalConfig
            endTurnDialogFragment.show(getFragmentManager(), "endTurnDialogFragment");
            getFragmentManager().executePendingTransactions();
        }
    }

    public void hideEndTurnDialog() {
        EndTurnDialogFragment endTurnDialogFragment = this.findEndTurnDialog();
        if (endTurnDialogFragment != null) {
            endTurnDialogFragment.dismiss();
            getFragmentManager().executePendingTransactions();
        }
    }

    public void updateDialogSlotsStack(Tile[] stack) {
        EndTurnDialogFragment endTurnDialogFragment = EndTurnDialogFragment.newInstance("title", "message");
        endTurnDialogFragment.setTargetFragment(this, 3);

        endTurnDialogFragment.show(getFragmentManager(), "endTurnDialogFragment");
        getFragmentManager().executePendingTransactions();

        endTurnDialogFragment.updateSlotsStack(stack);
    }

    public void updateSlotsStack(Tile[] stack) {
        Slave3Utils.updateSlotsTower(stack, slotImageViews, this.getActivity().getResources());
    }

    public void updateCompleteStack(Tile[] stack) {
        Slave3Utils.updateCompleteTower(stack, completeImageViews, this.getActivity().getResources());
    }

    private EndTurnDialogFragment findEndTurnDialog() {
        return (EndTurnDialogFragment) getFragmentManager().findFragmentByTag("endTurnDialogFragment");
    }
}

