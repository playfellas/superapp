package it.playfellas.superapp.logic.master.game23;

import android.bluetooth.BluetoothDevice;

import org.apache.commons.lang3.ArrayUtils;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.events.EventFactory;
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
    private Config3 conf;
    private TileSelector ts;

    public Master3Controller(TileSelector ts, Config3 conf) {
        super(conf);
        this.conf = conf;
        this.ts = ts;
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
    protected void onBeginStage() {
        super.onBeginStage();
        BluetoothDevice firstPlayer = nextPlayer();
        TenBus.get().post(EventFactory.yourTurn(firstPlayer));
    }

    @Override
    protected void onEndStage() {
        // does nothing
    }

    @Override
    protected void onAnswer(Tile tile, boolean rw) {
        if (rw) {
            incrementScore();
        }
        TenBus.get().post(EventFactory.yourTurn(nextPlayer()));
    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame3(conf);
    }
}
