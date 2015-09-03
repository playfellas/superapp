package it.playfellas.superapp.logic.master.game23;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.PopEvent;
import it.playfellas.superapp.events.game.PushEvent;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Color;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;
import it.playfellas.superapp.logic.tiles.TileShape;
import it.playfellas.superapp.logic.tiles.TileType;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 02/09/15.
 */
public class Master3Controller extends Master23Controller {
    private static final String TAG = Master3Controller.class.getSimpleName();
    private Config3 conf;
    private TileSelector ts;
    private Tile[] stack;
    private int stackPtr;

    public Master3Controller(TileSelector ts, Config3 conf) {
        super(conf);
        this.conf = conf;
        this.ts = ts;

        TenBus.get().register(this);
    }

    @Override
    protected Tile[] newBaseTiles() {
        final int size = InternalConfig.NO_FIXED_TILES;
        // remove NONE...
        TileColor[] colors = TileColor.values();
        TileShape[] shapes = TileShape.values();
        int noneIndex;

        noneIndex = ArrayUtils.indexOf(colors, TileColor.NONE);
        colors = ArrayUtils.remove(colors, noneIndex);

        noneIndex = ArrayUtils.indexOf(shapes, TileShape.NONE);
        shapes = ArrayUtils.remove(shapes, noneIndex);

        colors = RandomUtils.choice(colors, size).toArray(new TileColor[size]);
        shapes = RandomUtils.choice(shapes, size).toArray(new TileShape[size]);
        Tile[] tiles = new Tile[InternalConfig.NO_FIXED_TILES];

        for (int i = 0; i < size; i++) {
            tiles[i] = ts.random(1, new Conjunction(
                    new Shape(BinaryOperator.EQUALS, shapes[i]),
                    new Color(BinaryOperator.EQUALS, colors[i]),
                    new Type(BinaryOperator.EQUALS, TileType.ABSTRACT)
            )).get(0);
        }

        return tiles;
    }

    @Override
    protected synchronized void onBeginStage() {
        super.onBeginStage();
        this.stack = new Tile[InternalConfig.NO_FIXED_TILES];
        Arrays.fill(this.stack, null);
        this.stackPtr = 0;
        BluetoothDevice firstPlayer = nextPlayer();
        TenBus.get().post(EventFactory.yourTurn(firstPlayer, stack));
    }

    @Override
    protected void onEndStage() {
        // does nothing
    }

    private synchronized void nextTurn() {
        TenBus.get().post(EventFactory.yourTurn(nextPlayer(), stack));
    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame3(conf);
    }

    @Subscribe
    public synchronized void onPush(PushEvent e) {
        stackPtr++;
        if (stackPtr >= stack.length) {
            Log.d(TAG, "Exceeding stack length!");
            stackPtr = stack.length - 1;
            return;
        }
        stack[stackPtr] = e.getTile();

        nextTurn();
    }

    @Subscribe
    public synchronized void onPop(PopEvent e) {
        if (e.isWrongMove()) {
            // in case someone removed a
            // right tile
            decrementScore();
        }

        stack[stackPtr] = null;
        stackPtr--;
        if (stackPtr < 0) {
            stackPtr = 0;
        }

        nextTurn();
    }
}
