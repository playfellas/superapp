package it.playfellas.superapp.logic.master.game1;

import org.apache.commons.lang3.ArrayUtils;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config1Color;
import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.tiles.TileColor;

/**
 * Created by affo on 07/08/15.
 */
public class Master1Color extends Master1Controller {
    private Config1Color conf;
    private TileColor baseColor;

    public Master1Color(Config1Color conf) {
        super(conf);
        this.conf = conf;
        TileColor[] colors = TileColor.values();
        // baseColor should not be NONE...
        int noneIndex = ArrayUtils.indexOf(colors, TileColor.NONE);
        colors = ArrayUtils.remove(colors, noneIndex);
        this.baseColor = RandomUtils.choice(colors);
        conf.setBaseColor(this.baseColor);
    }

    @Override
    protected void onBeginStage() {

    }

    @Override
    protected void onEndStage() {

    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame1Color(conf);
    }
}
