package it.playfellas.superapp.logic.slave.game2;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 07/08/15.
 */
public class Slave2Controller extends SlaveController {

    @Override
    protected void onBeginStage(BeginStageEvent e) {

    }

    @Override
    protected void onEndStage(EndStageEvent e) {

    }

    @Override
    protected void onEndGame(EndGameEvent e) {

    }

    @Override
    protected boolean isTileRight(Tile t) {
        return false;
    }

    @Override
    protected TileDispenser getDispenser() {
        return new SizeDispenser();
    }
}
