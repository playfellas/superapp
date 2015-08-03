package it.playfellas.superapp.logic.slave.game1;

import java.util.List;

import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 03/08/15.
 */
public class IntruderDispenserInverter extends IntruderTileDispenser {
    private IntruderTileDispenser normal;

    public IntruderDispenserInverter(IntruderTileDispenser normalDispenser) {
        this.normal = normalDispenser;
    }

    @Override
    List<Tile> getTargets(int n) {
        return normal.getEasy(n, normal.getTargets(n));
    }

    @Override
    List<Tile> getCritical(int n, List<Tile> targets) {
        return normal.getTargets(n);
    }

    @Override
    List<Tile> getEasy(int n, List<Tile> targets) {
        return normal.getTargets(n);
    }
}
