package it.playfellas.superapp.ui.slave.game2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.ui.BitmapUtils;
import it.playfellas.superapp.ui.slave.Conveyor;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame2Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame2Fragment.class.getSimpleName();

    @Bind(R.id.downConveyor)
    RelativeLayout downConveyorLayout;

    @Bind(R.id.photoImageView)
    CircleImageView photoImageView;

    @Bind(R.id.slot1ImageView)
    ImageView slot1ImageView;
    @Bind(R.id.slot2ImageView)
    ImageView slot2ImageView;
    @Bind(R.id.slot3ImageView)
    ImageView slot3ImageView;
    @Bind(R.id.slot4ImageView)
    ImageView slot4ImageView;


    private static Bitmap photo;

    @Getter
    private Conveyor conveyorUp;
    @Getter
    private Conveyor conveyorDown;

    protected static Config2 config;
    protected static TileSelector db;
    private Slave2Presenter slave2Presenter;
    private ImageView[] slotsImageView = new ImageView[InternalConfig.NO_FIXED_TILES];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game2_fragment, container, false);

        ButterKnife.bind(this, root);

        Log.d(TAG, "Creating Converyors...");

        conveyorDown = new Conveyor(downConveyorLayout, 100, Conveyor.RIGHT);
        conveyorDown.start();

        photoImageView.setImageBitmap(photo);

        slotsImageView[0] = slot1ImageView;
        slotsImageView[1] = slot2ImageView;
        slotsImageView[2] = slot3ImageView;
        slotsImageView[3] = slot4ImageView;

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
    public static SlaveGame2Fragment newInstance(TileSelector ts, Config2 config2, Bitmap photoBitmap) {
        SlaveGame2Fragment fragment = new SlaveGame2Fragment();
        db = ts;
        config = config2;
        photo = photoBitmap;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.pausePresenter();
        this.slave2Presenter = new Slave2Presenter(db, this, config);
        this.slave2Presenter.startTileDisposer();
    }

    @Override
    public void pausePresenter() {
        if (this.slave2Presenter != null) {
            this.slave2Presenter.pause();
        }
    }

    @Override
    public void restartPresenter() {
        if (this.slave2Presenter != null) {
            this.slave2Presenter.restart();
        }
    }

    public void showBaseTiles(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            int resId = this.getActivity().getResources().getIdentifier(tiles[i].getName(),
                    InternalConfig.DRAWABLE_RESOURCE, InternalConfig.PACKAGE_NAME);
            Bitmap immutable = BitmapFactory.decodeResource(getResources(), resId);
            Bitmap mutableScaledSilhouette = BitmapUtils.getScaledColorSilhouetteInsideColoredFrame(immutable,
                    tiles[i].getSize().getMultiplier(), Color.TRANSPARENT, Color.RED);
            slotsImageView[i].setImageBitmap(mutableScaledSilhouette);
        }
    }
}
