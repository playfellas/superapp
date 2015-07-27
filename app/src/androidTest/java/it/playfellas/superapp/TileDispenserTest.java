package it.playfellas.superapp;

import android.test.AndroidTestCase;
import android.util.Log;

import com.squareup.otto.Subscribe;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.events.NewTileEvent;
import it.playfellas.superapp.logic.TileDispenser;
import it.playfellas.superapp.logic.tiles.DummyTile;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.utils.NineBus;

/**
 * Created by affo on 27/07/15.
 */
public class TileDispenserTest extends AndroidTestCase {
    private TileDispenser td;
    private List<Tile> tiles;
    private final float rtt = (float) 3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        td = new TileDispenser(rtt) {
            @Override
            protected Tile getTile() {
                return new DummyTile("dummy");
            }
        };
        tiles = new ArrayList<>();
        NineBus.get().register(this);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        td.kill();
    }

    public void testStartStop(){
        assertFalse(td.isDispensing());
        td.dispense();
        assertTrue(td.isDispensing());
        td.kill();
        assertFalse(td.isDispensing());
    }

    public void testTileEvents() throws InterruptedException {
        td.dispense();
        Thread.sleep((long) (rtt * 1000));
        assertTrue(tiles.size() > 0); // some event should be happened
    }

    @Subscribe public void onTile(NewTileEvent e){
        Log.d(getClass().getSimpleName(), e.toString());
        tiles.add(e.getTile());
    }
}
