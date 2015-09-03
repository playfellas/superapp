package it.playfellas.superapp.ui.slave.game3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private static final String DRAWABLE_RESOURCE = "drawable";
    private static final String PACKAGE_NAME = "it.playfellas.superapp";

    @Bind(R.id.downConveyor)
    LinearLayout downConveyorLayout;
    @Bind(R.id.photoImageView)
    ImageView photoImageView;

    @Bind(R.id.relativeLayoutSlots)
    RelativeLayout slotsLayout;
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
        slotsImageView[0] = complete1ImageView;
        slotsImageView[1] = complete2ImageView;
        slotsImageView[2] = complete3ImageView;
        slotsImageView[3] = complete4ImageView;

        //init the complete tower
        completeImageView[0] = complete1ImageView;
        completeImageView[1] = complete2ImageView;
        completeImageView[2] = complete3ImageView;
        completeImageView[3] = complete4ImageView;

        slotsLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "OnTouch");
                //send event to remove an image from the slots tower
                slave3Presenter.stackClicked();
                return true;
            }
        });

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
        for (int i = 0; i < tiles.length; i++) {
            completeImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(getBitmapFromResId(tiles[i]), slave3Presenter.getTileSizes()[i].getMultiplier(), Color.TRANSPARENT));
        }
    }

    public void updateSlotsTower(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            slotsImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(getBitmapFromResId(tiles[i]), slave3Presenter.getTileSizes()[i].getMultiplier(), Color.TRANSPARENT));
        }
    }

    private Bitmap getBitmapFromResId(Tile t) {
        int resId = this.getActivity().getResources().getIdentifier(t.getName(), DRAWABLE_RESOURCE, PACKAGE_NAME);
        return BitmapFactory.decodeResource(this.getActivity().getResources(), resId);
    }
}
