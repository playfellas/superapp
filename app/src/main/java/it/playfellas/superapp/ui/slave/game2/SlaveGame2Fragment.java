package it.playfellas.superapp.ui.slave.game2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    ImageView photoImageView;
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
            int resId = this.getActivity().getResources().getIdentifier(tiles[i].getName(), InternalConfig.DRAWABLE_RESOURCE, InternalConfig.PACKAGE_NAME);
            Drawable drawable = this.getActivity().getResources().getDrawable(resId);
            Bitmap immutable = BitmapFactory.decodeResource(getResources(), resId);
//
            Bitmap mutable = immutable.copy(Bitmap.Config.ARGB_8888, true);

            Bitmap newMutable = BitmapUtils.scaleInsideWithFrame(mutable,tiles[i].getSize().getMultiplier(), Color.TRANSPARENT);
//            Canvas c = new Canvas(newMutable);
//            Paint p = new Paint();
////            p.setColor(Color.TRANSPARENT);
//            p.setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP));
//            c.drawBitmap(mutable, 0.f, 0.f, p);
//            BitmapDrawable dwb3 = new BitmapDrawable(this.getActivity().getResources(), newMutable);
//
//            slotsImageView[i].setImageDrawable(dwb3);
            slotsImageView[i].setImageBitmap(newMutable);
//            slotsImageView[i].setImageDrawable(silohuetteDrawable);
//            slotsImageView[i].setImageBitmap(BitmapUtils.scaleInsideWithFrame(silohuetteBitmap, tiles[i].getSize().getMultiplier(), Color.TRANSPARENT));
        }
    }
}
