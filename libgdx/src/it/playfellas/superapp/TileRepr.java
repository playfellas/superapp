package it.playfellas.superapp;

import com.badlogic.gdx.graphics.g2d.Sprite;
import it.playfellas.superapp.tiles.Tile;

public class TileRepr {
  Sprite sprite;
  Tile tile;

  public TileRepr(Sprite sprite, Tile tile) {
    this.sprite = sprite;
    this.tile = tile;
  }

  public Sprite getSprite() {
    return sprite;
  }

  public void setSprite(Sprite sprite) {
    this.sprite = sprite;
  }

  public Tile getTile() {
    return tile;
  }

  public void setTile(Tile tile) {
    this.tile = tile;
  }
}
