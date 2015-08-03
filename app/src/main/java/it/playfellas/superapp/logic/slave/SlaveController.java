package it.playfellas.superapp.logic.slave;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 28/07/15.
 * This is the controller of a player.
 * Once extended, the `SlaveController` needs 3 methods to be implemented:
 * - isTileRight
 * - getNormalDispenser
 * - getSpecialDispenser
 * (see the doc of them).
 * The `getTile` method has to be called from the UI (or from a presenter),
 * and it uses a `TileDispenser` to provide a new `Tile` to be placed.
 */
public abstract class SlaveController {
    private static final String TAG = SlaveController.class.getSimpleName();
    private boolean dispenserToggle;
    private TileDispenser normalDispenser;
    private TileDispenser specialDispenser;
    private TileDispenser dispenser;

    // Object to be registered on `TenBus`.
    // We need it to make extending classes inherit
    // `@Subscribe` methods.
    private Object busListener;

    public SlaveController() {
        super();
        normalDispenser = getNormalDispenser();
        specialDispenser = getSpecialDispenser();

        busListener = new Object() {
            @Subscribe
            public void onTileClicked(ClickedTileEvent e) {
                Tile t = e.getTile();
                boolean rw = isTileRight(t);
                String rwWord = rw ? "Correct" : "Incorrect";
                Log.d(TAG, rwWord + " answer given");
                TenBus.get().post(EventFactory.rw(rw));
            }

            @Subscribe
            public void onGameChange(ToggleGameModeEvent e) {
                toggleDispenser();
            }

            @Subscribe
            public void onBeginStage(BeginStageEvent e) {
                synchronized (SlaveController.this) {
                    dispenserToggle = true;
                    dispenser = normalDispenser;
                }
            }
        };
        TenBus.get().register(busListener);
    }

    /**
     * Override to implement the logic of the game.
     * This method will be called every time a `Tile` is clicked
     * by the user. The correctness of the `Tile` could be determined
     * by the history of the clicks. In this case, it is important
     * to store, using this method, every clicked `Tile`.
     *
     * @param t the clicked `Tile`
     * @return true if the answer is right, false otherwise
     */
    abstract boolean isTileRight(Tile t);

    /**
     * @return a new `TileDispenser` for normal game mode
     */
    abstract TileDispenser getNormalDispenser();

    /**
     * @return a new `TileDispenser` for special game mode
     */
    abstract TileDispenser getSpecialDispenser();

    synchronized boolean isNormalMode() {
        return dispenserToggle;
    }

    public synchronized void toggleDispenser() {
        dispenserToggle = !dispenserToggle;
        if (dispenserToggle) {
            dispenser = normalDispenser;
        } else {
            dispenser = specialDispenser;
        }
    }

    public Tile getTile() {
        return dispenser.next();
    }
}
