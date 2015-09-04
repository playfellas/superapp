package it.playfellas.superapp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Tile {
  TileWrapper tileWrapper;
  Sprite sprite;

  public Tile(TileWrapper tileWrapper, Sprite sprite) {
    this.tileWrapper = tileWrapper;
    this.sprite = sprite;
  }

  public TileWrapper getTileWrapper() {
    return tileWrapper;
  }

  public void setTileWrapper(TileWrapper tileWrapper) {
    this.tileWrapper = tileWrapper;
  }

  public Sprite getSprite() {
    return sprite;
  }

  public void setSprite(Sprite sprite) {
    this.sprite = sprite;
  }
}
