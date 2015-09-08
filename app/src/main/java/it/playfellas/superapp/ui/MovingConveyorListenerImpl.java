package it.playfellas.superapp.ui;

import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.listeners.MovingConveyorListener;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

public class MovingConveyorListenerImpl extends MovingConveyorListener {
  @Override public void onTileClicked(Tile tile) {
    TenBus.get().post(new ClickedTileEvent(tile));
  }
}
