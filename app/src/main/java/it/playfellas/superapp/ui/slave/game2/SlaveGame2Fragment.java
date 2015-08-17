package it.playfellas.superapp.ui.slave.game2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.slave.Conveyor;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 12/08/15.
 */
public class SlaveGame2Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame2Fragment.class.getSimpleName();

    protected Slave2Presenter presenter;

    @Bind(R.id.photoImageView)
    ImageView photoImageView;

    private static Bitmap photo;

    @Getter
    private Conveyor conveyorDown;

    @Bind(R.id.downConveyor)
    LinearLayout downConveyorLayout;

    protected static Config2 config;
    protected static TileSelector db;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame2Fragment newInstance(TileSelector ts, Config2 config1, Bitmap photoBitmap) {
        SlaveGame2Fragment fragment = new SlaveGame2Fragment();
        db = ts;
        config = config1;
        photo = photoBitmap;
        return fragment;
    }

    public SlaveGame2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game2_fragment, container, false);

        ButterKnife.bind(this, root);

        conveyorDown = new Conveyor(downConveyorLayout, 100, Conveyor.RIGHT);

        conveyorDown.start();

        photoImageView.setImageBitmap(photo);

        return root;
    }
}
