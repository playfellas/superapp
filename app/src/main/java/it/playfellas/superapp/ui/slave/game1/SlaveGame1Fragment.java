package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.slave.Conveyor;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public abstract class SlaveGame1Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame1Fragment.class.getSimpleName();

    @Bind(R.id.upConveyor)
    LinearLayout downConveyorLayout;
    @Bind(R.id.downConveyor)
    LinearLayout upConveyorLayout;
    @Bind(R.id.photoImageView)
    ImageView photoImageView;

    private static Bitmap photo;

    @Getter
    private Conveyor conveyorUp;
    @Getter
    private Conveyor conveyorDown;

    protected static Config1 config;
    protected static TileSelector db;

    /**
     * Init method
     */
    public static void init(TileSelector ts, Config1 config1, Bitmap photoBitmap) {
        db = ts;
        config = config1;
        photo = photoBitmap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_game1_fragment, container, false);

        ButterKnife.bind(this, root);

        Log.d(TAG, "Creating Converyors...");

        conveyorUp = new Conveyor(upConveyorLayout, 100, Conveyor.LEFT);
        conveyorDown = new Conveyor(downConveyorLayout, 100, Conveyor.RIGHT);

        conveyorUp.start();
        conveyorDown.start();

        photoImageView.setImageBitmap(photo);

        return root;
    }

    @Override
    public void onDestroyView() {

        if (conveyorUp != null) {
            conveyorUp.stop();
        }
        if (conveyorDown != null) {
            conveyorDown.stop();
        }

        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
