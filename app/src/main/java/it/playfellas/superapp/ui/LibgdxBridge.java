package it.playfellas.superapp.ui;

import it.playfellas.superapp.Bridge;
import it.playfellas.superapp.TileWrapper;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

public class LibgdxBridge implements Bridge{
  @Override public void tileClicked(TileWrapper tileWrapper) {
    TenBus.get().post(new ClickedTileEvent(new Tile(tileWrapper)));
  }
}
