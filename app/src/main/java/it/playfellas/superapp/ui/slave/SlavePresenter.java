package it.playfellas.superapp.ui.slave;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 07/08/15.
 */
public abstract class SlavePresenter {

    private SlaveGameFragment fragment;
    /**
     * Object to be registered on {@link it.playfellas.superapp.network.TenBus}.
     * We need it to make extending classes inherit @Subscribe methods.
     */
    private Object busListener;

    public SlavePresenter() {
        busListener = new Object() {
            @Subscribe
            public void onUIRWEvent(UIRWEvent e) {
                fragment.onRightOrWrong(e);
            }

            @Subscribe
            public void onNewTileEvent(NewTileEvent event) {
                newTileEvent(event);
            }
        };
        TenBus.get().register(busListener);
    }

    protected abstract void newTileEvent(NewTileEvent event);
}
