package it.playfellas.superapp.logic.slave.game1;

import java.util.List;

import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 31/07/15.
 */
public class ShapeIntruderDispenser extends IntruderTileDispenser {

    @Override
    List<Tile> getTargets(int n) {
        return null;
    }

    @Override
    List<Tile> getCritical(int n, List<Tile> targets) {
        return null;
    }

    @Override
    List<Tile> getEasy(int n, List<Tile> targets) {
        return null;
    }
}
