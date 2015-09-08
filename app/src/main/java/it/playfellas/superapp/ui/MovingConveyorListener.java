package it.playfellas.superapp.ui;

import it.playfellas.superapp.MovingConveyor;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

public class MovingConveyorListener implements MovingConveyor.MovingConveyorListenerInterface {

  @Override public void tileClicked(Tile tile) {
    TenBus.get().post(new ClickedTileEvent(tile));
  }


}
