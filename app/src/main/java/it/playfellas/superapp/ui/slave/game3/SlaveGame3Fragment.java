package it.playfellas.superapp.ui.slave.game3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private ImageView[] slotsImageView = new ImageView[InternalConfig.NO_FIXED_TILES];
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
        slotsImageView[0] = slot4ImageView;
        slotsImageView[1] = slot3ImageView;
        slotsImageView[2] = slot2ImageView;
        slotsImageView[3] = slot1ImageView;

        //init the complete tower
        completeImageView[0] = complete4ImageView;
        completeImageView[1] = complete3ImageView;
        completeImageView[2] = complete2ImageView;
        completeImageView[3] = complete1ImageView;

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

    public void showBaseTiles(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            int resId = this.getActivity().getResources().getIdentifier(tiles[i].getName(), DRAWABLE_RESOURCE, PACKAGE_NAME);
            //create the complete tower
            Bitmap origBitmap = BitmapFactory.decodeResource(this.getActivity().getResources(), resId);
            completeImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(origBitmap, tiles[i].getSize().getMultiplier(), Color.TRANSPARENT));

            //create the target tower to complete, composed by slots
            Bitmap greyScale = BitmapUtils.copy(origBitmap);
            slotsImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(greyScale, tiles[i].getSize().getMultiplier(), Color.TRANSPARENT));
        }
    }
}
