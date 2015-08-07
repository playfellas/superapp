package it.playfellas.superapp.ui.master.game1;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.PhotoEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.Master1Controller;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.BitmapUtils;
import it.playfellas.superapp.ui.master.GamePresenter;

public class Game1Presenter extends GamePresenter {
    private static final String TAG = Game1Presenter.class.getSimpleName();
    private Game1Fragment fragment;
    private Config1 config1;
    private MasterController master;

    public Game1Presenter(Game1Fragment fragment, Config1 config) {
        super(fragment, config);

        this.fragment = fragment;
        this.config1 = config;

        TenBus.get().register(this);

        super.init();
        this.master = super.getMaster();

        this.fragment.initCentralImage(config.getNoStages());
        this.master.beginStage();
    }

    // TODO REMOVE FROM THIS CLASS. THIS EVENT CANNOT BE CATCHED HERE; BECAUSE IT'S POSTED WHEN THIS PRESENTER ISN'T CREATED.
    // TODO MOVE THIS IN AN ACTIVITY LIKE GAMEACTIVITY.
    // TODO remove all this if-else in the final version. They are here only for testing
    @Subscribe
    public void onBTPhotoEvent(PhotoEvent event) {
        if (fragment != null && fragment.photo1ImageView != null) {
            if (event.getPhotoByteArray() != null) {
                Bitmap bitmap = BitmapUtils.toBitmap(event.getPhotoByteArray());
                if (bitmap != null) {
                    fragment.photo1ImageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "onBTPhotoEvent, The bitmap from BitmapUtils.toBitmap was null");
                }
            } else {
                Log.e(TAG, "onBTPhotoEvent, you received a null photo!!!!");
            }
        } else {
            Log.e(TAG, "onBTPhotoEvent error, very bad! Probably you should call " +
                    "ButterKnife.bind in GameFragment superclass, but it's only a supposition");
        }
    }

    @Override
    protected MasterController newMasterController() {
        return new Master1Controller(config1);
    }
}