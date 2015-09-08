package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import it.playfellas.superapp.MovingConveyor;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.BitmapUtils;
import it.playfellas.superapp.ui.MovingConveyorListenerImpl;
import it.playfellas.superapp.ui.slave.SceneFragment;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public abstract class SlaveGame1Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame1Fragment.class.getSimpleName();

    private SceneFragment sceneFragment;

    @Bind(R.id.photoImageView)
    CircleImageView photoImageView;

    private static Bitmap photo;

    protected static Config1 config;
    protected static TileSelector db;
    @Getter
    private MovingConveyor conveyorUp;
    @Getter
    private MovingConveyor conveyorDown;

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

        sceneFragment = SceneFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.scene, sceneFragment).commit();

        if (photo != null && photoImageView != null) {
            photoImageView.setImageBitmap(BitmapUtils.scaleBitmap(photo, 100, 100));
        }

        return root;
    }

    @Override
    public void onDestroyView() {

        if (conveyorUp != null) {
            conveyorUp.clear();
            conveyorUp.stop();
        }
        if (conveyorDown != null) {
            conveyorDown.clear();
            conveyorDown.stop();
        }

        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override protected MovingConveyor newConveyorUp() {
        conveyorUp = new MovingConveyor(new MovingConveyorListenerImpl(), 5, MovingConveyor.LEFT);
        return conveyorUp;
    }

    @Override protected MovingConveyor newConveyorDown() {
        conveyorDown = new MovingConveyor(new MovingConveyorListenerImpl(), 5, MovingConveyor.RIGHT);
        return conveyorDown;
    }

}
