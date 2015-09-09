package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;

public class SizeConveyor extends Conveyor {

  private Array<TileRepr> tileReprs;
  private int foundTiles;

  public SizeConveyor(BaseListener listener) {
    super(listener);
    tileReprs = new Array<TileRepr>();
  }

  @Override public Array<TileRepr> getTileReprs() {
    return tileReprs;
  }

  @Override public void update() {
  }

  @Override public void start() {
  }

  @Override public void stop() {
  }

  @Override public void clear() {
  }

  @Override public void addTile(Tile tile) {
  }

  public void addBaseTiles(final Tile[] tiles) {
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        for (int i = 0; i < tiles.length; i++) {
          Sprite tileSprite = makeBlackSprite(tiles[i]);
          tileSprite = positionSprite(tileSprite, tiles.length, i);
          TileRepr tileRepr = new TileRepr(tileSprite, tiles[i]);
          tileReprs.add(tileRepr);
        }
      }
    });
  }

  public void correctTile() {
    if (foundTiles == tileReprs.size) {
      return;
    }
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        TileRepr tileRepr = tileReprs.get(foundTiles);
        Sprite tileSprite = makeSprite(tileRepr.getTile());
        tileSprite = positionSprite(tileSprite, tileReprs.size, foundTiles);
        tileRepr.setSprite(tileSprite);
        foundTiles++;
      }
    });
  }

  /**
   * Caluate the position of a tile given the tile number and the position.
   *
   * @param sprite the sprite to display.
   * @param position the position of the sprite on the sizeConveyor. It starts at 0.
   * @return the x coordinate.
   */
  protected float calculateSpriteX(Sprite sprite, int noTile, int position) {
    float sectionWidth = super.width / noTile;
    float x = position * sectionWidth;
    x += sectionWidth / 2;
    x -= sprite.getWidth() / 2;
    return x;
  }

  private Sprite makeBlackSprite(Tile tile) {
    Sprite sprite = super.makeSprite(tile);
    sprite.setColor(Color.BLACK);
    return sprite;
  }

  private Sprite positionSprite(Sprite sprite, int noTile, int position) {
    sprite.setPosition(calculateSpriteX(sprite, noTile, position), calculateSpriteY(sprite));
    return sprite;
  }
}
