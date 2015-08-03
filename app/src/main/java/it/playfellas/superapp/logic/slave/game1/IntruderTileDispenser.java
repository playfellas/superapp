package it.playfellas.superapp.logic.slave.game1;

import java.util.List;
import java.util.Random;

import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 31/07/15.
 */
public abstract class IntruderTileDispenser extends TileDispenser {
    private static final int noCritical = 3;
    private static final int noEasy = 4;
    private static final int noTarget = 5;
    private static final int tgtProb = 75;
    private static final int easyProb = 20;

    protected List<Tile> tgt;
    protected List<Tile> critical;
    protected List<Tile> easy;
    private Random rng;

    public IntruderTileDispenser() {
        super();
        rng = new Random();
        this.tgt = getTargets(noTarget);
        this.critical = getCritical(noCritical, tgt);
        this.easy = getEasy(noEasy, tgt);
    }

    abstract List<Tile> getTargets(int n);

    abstract List<Tile> getCritical(int n, List<Tile> targets);

    abstract List<Tile> getEasy(int n, List<Tile> targets);

    <T> T randomSelect(List<T> l) {
        return l.get(rng.nextInt(l.size()));
    }

    @Override
    public Tile next() {
        int choice = (int) (rng.nextFloat() * 100);

        if (choice <= tgtProb) {
            return randomSelect(this.tgt);
        }

        if (choice <= tgtProb + easyProb) {
            return randomSelect(this.easy);
        }

        return randomSelect(this.critical);
    }
}
