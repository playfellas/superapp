package it.playfellas.superapp.ui.slave;

import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 04/08/15.
 * <p/>
 * A `TileDisposer` posts `NewTileEvent`s basing on the current rtt.
 * It subscribes to `RTTUpdateEvent`s and reacts internally to the changes in RTT.
 * It lefts the decision to really post a `NewTileEvent` to implementors
 * leaving `shouldIStayOrShouldISpawn` method abstract (random boolean?).
 * <p/>
 * A `TileDisposer`, after instantiation, has to be started (`start` method),
 * and it should be stopped (`stop` method) when unused.
 *
 * @deprecated use {@link DisposingService} instead.
 */
@Deprecated
public abstract class TileDisposer {
    private SlaveController sc;
    private int tileDensity;
    private float baseRtt;
    private boolean tutorialMode;

    private Timer tilePoser;

    private Object busListener;

    public TileDisposer(final SlaveController sc, final Config conf) {
        super();
        this.sc = sc;
        this.tileDensity = conf.getTileDensity();
        this.baseRtt = conf.getDefaultRtt();
        this.tutorialMode = conf.isTutorialMode();
        this.tilePoser = new Timer();
        this.busListener = new Object() {
            @Subscribe
            public void onRttUpdate(RTTUpdateEvent e) {
                reschedule(e.getRtt());
            }
        };
    }

    private TimerTask getSpawnTask() {
        return new TimerTask() {
            @Override
            public void run() {
                newTile();
            }
        };
    }

    private void reschedule(float rttInSeconds) {
        tilePoser.cancel();
        tilePoser.purge();

        tilePoser = new Timer();

        long rtt = (long) (rttInSeconds * 1000); // from s to ms
        tilePoser.schedule(getSpawnTask(), rtt / tileDensity, rtt / tileDensity);
    }

    private void newTile() {
        if (shouldIStayOrShouldISpawn()) {
            Tile t = sc.getTile();
            if (tutorialMode) {
                boolean rw = sc.isTileRight(t);
                TenBus.get().post(EventFactory.newTutorialTile(t, rw));
            } else {
                TenBus.get().post(EventFactory.newTile(t));
            }
        }
    }

    protected abstract boolean shouldIStayOrShouldISpawn();

    public void restart() {
        reschedule(baseRtt);
    }

    public void start() {
        reschedule(baseRtt);
        TenBus.get().register(busListener);
    }

    public void pause() {
        tilePoser.cancel();
        tilePoser.purge();
    }

    public void stop() {
        this.pause();
        TenBus.get().unregister(busListener);
    }
}
