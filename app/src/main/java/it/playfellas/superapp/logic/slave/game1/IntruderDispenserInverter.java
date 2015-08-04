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
        int noCritical = n / 2;
        int noEasy = n - noCritical;
        List<Tile> tgts = normal.getTargets(n);
        List<Tile> easy = normal.getEasy(noEasy, tgts);
        List<Tile> critical = normal.getCritical(noCritical, tgts);
        easy.addAll(critical);
        return easy;
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
