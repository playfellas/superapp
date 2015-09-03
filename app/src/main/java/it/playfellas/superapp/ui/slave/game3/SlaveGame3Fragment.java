package it.playfellas.superapp.ui.slave.game3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.ui.BitmapUtils;
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

    @Bind(R.id.relativeLayoutSlots)
    RelativeLayout slotsLayout;
    @Bind(R.id.stackButton)
    Button stackButton;
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
    final private ImageView[] slotsImageView = new ImageView[InternalConfig.NO_FIXED_TILES];
    private ImageView[] completeImageView = new ImageView[InternalConfig.NO_FIXED_TILES];

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
        slotsImageView[0] = slot1ImageView;
        slotsImageView[1] = slot2ImageView;
        slotsImageView[2] = slot3ImageView;
        slotsImageView[3] = slot4ImageView;

        //init the complete tower
        completeImageView[0] = complete1ImageView;
        completeImageView[1] = complete2ImageView;
        completeImageView[2] = complete3ImageView;
        completeImageView[3] = complete4ImageView;

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

        BitmapDrawable bdrawable = new BitmapDrawable(this.getActivity().getResources(),BitmapUtils.clearBitmap());
        stackButton.setBackground(bdrawable);

        stackButton.setOnClickListener(new View.OnClickListener() {
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

    public void updateCompleteTower(Tile[] tiles) {
        for (int i = 0; i < tiles.length && tiles[i] != null; i++) {
            completeImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(getBitmapFromResId(tiles[i]), slave3Presenter.getTileSizes()[i].getMultiplier(), Color.TRANSPARENT));
        }
    }

    public void updateSlotsTower(Tile[] tiles) {
        //TODO UPDATE NOT ONLY THE slotsImageView[I]!=NULL BUT I MUST CLEAR THE NULL ELMENT
        for (int i = 0; i < tiles.length && tiles[i] != null; i++) {
            slotsImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(getBitmapFromResId(tiles[i]), slave3Presenter.getTileSizes()[i].getMultiplier(), Color.TRANSPARENT));
        }
        //TODO
//        for (int i = 0; i < tiles.length && tiles[i] == null; i++) {
//            slotsImageView[i].setImageBitmap(free transparent image);
//        }
    }

    private Bitmap getBitmapFromResId(Tile t) {
        int resId = this.getActivity().getResources().getIdentifier(t.getName(), InternalConfig.DRAWABLE_RESOURCE, InternalConfig.PACKAGE_NAME);
        return BitmapFactory.decodeResource(this.getActivity().getResources(), resId);
    }

    public void showEndTurnDialog() {
        EndTurnDialogFragment endStageDialogFragment = (EndTurnDialogFragment) getFragmentManager()
                .findFragmentByTag("endTurnDialogFragment");

        if (endStageDialogFragment == null) {
            endStageDialogFragment = EndTurnDialogFragment.newInstance("title", "message");
            endStageDialogFragment.setTargetFragment(this, 3);

            endStageDialogFragment.show(getFragmentManager(), "endTurnDialogFragment");
            getFragmentManager().executePendingTransactions();
        }
    }

    public void hideEndTurnDialog() {
        EndTurnDialogFragment endStageDialogFragment = (EndTurnDialogFragment) getFragmentManager()
                .findFragmentByTag("endTurnDialogFragment");
        if (endStageDialogFragment != null) {
            endStageDialogFragment.dismiss();
        }
    }

    public void updateDialogSlotsTower(Tile[] stack) {
        EndTurnDialogFragment endStageDialogFragment = EndTurnDialogFragment.newInstance("title", "message");
        endStageDialogFragment.setTargetFragment(this, 3);

        endStageDialogFragment.show(getFragmentManager(), "endTurnDialogFragment");
        getFragmentManager().executePendingTransactions();

        endStageDialogFragment.updateSlotsTower(stack);
    }
}
