package it.playfellas.superapp.logic.tiles;

/**
 * Created by affo on 27/07/15.
 */
public class DummyTile extends Tile {
    private String foo;

    public DummyTile(String foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return foo;
    }
}
