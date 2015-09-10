package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.conveyors.MovingConveyor;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.slave.MovingConveyorListener;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public abstract class SlaveGame1Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame1Fragment.class.getSimpleName();

    @Bind(R.id.gameFragmentRelativeLayout)
    RelativeLayout gameFragmentRelativeLayout;


    protected static Config1 config;
    @Getter
    private MovingConveyor conveyorUp;
    @Getter
    private MovingConveyor conveyorDown;

    /**
     * Init method
     */
    public static void init(TileSelector ts, Config1 config1, Bitmap photoBitmap) {
        init(photoBitmap);
        db = ts;
        config = config1;
    }

    @Override
    protected void onCreateView(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.slave_game1_fragment;
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

    @Override
    protected MovingConveyor newConveyorUp() {
        conveyorUp = new MovingConveyor(new MovingConveyorListener(), 5, MovingConveyor.LEFT);
        return conveyorUp;
    }

    @Override
    protected MovingConveyor newConveyorDown() {
        conveyorDown = new MovingConveyor(new MovingConveyorListener(), 5, MovingConveyor.RIGHT);
        return conveyorDown;
    }


    protected void swapBackground(boolean isInverted) {
        if (isInverted) {
            gameFragmentRelativeLayout.setBackground(getActivity().getResources().getDrawable(R.drawable._background_inverted));
        } else {
            gameFragmentRelativeLayout.setBackground(getActivity().getResources().getDrawable(R.drawable._background_normal));
        }
        sceneFragment.getScene().swapBackground();
    }
}
