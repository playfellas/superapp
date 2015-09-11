package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import it.playfellas.superapp.CompositeBgSprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;

public class SizeConveyor extends Conveyor {

  private int foundTiles;

  public SizeConveyor(BaseListener listener) {
    super(listener);
  }

  @Override public void init() {
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        Texture bgTexture = new Texture("_slot.png");
        bgTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        CompositeBgSprite bgSprite = new CompositeBgSprite();
        for (int i = 0; i < 4; i++) {
          Sprite s = new Sprite(bgTexture);
          float slotWidth = height;
          float slotSpace = (width / 4);
          float x = (slotSpace * i) + (slotSpace - slotWidth) / 2;
          s.setBounds(x, relativeVPosition, height, height);
          bgSprite.addSprite(s);
        }
        setBgSprite(bgSprite);
      }
    });
  }

  @Override public void update() {
  }

  @Override public void start() {
  }

  @Override public void stop() {
  }

  @Override public void addTile(Tile tile) {
  }

  @Override public void touch(Vector3 touchPos) {
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
   * @param sprite
   * @param position the position of the sprite on the sizeConveyor. It starts at 0.
   * @return the x coordinate.
   */
  protected float calculateTileX(Sprite sprite, int noTile, int position) {
    float slotSpace = width / noTile;
    float x = position * slotSpace;
    x += slotSpace / 2;
    x -= sprite.getWidth() / 2;
    return x;
  }

  private Sprite makeBlackSprite(Tile tile) {
    Sprite sprite = makeSprite(tile);
    sprite.setColor(Color.BLACK);
    return sprite;
  }

  private Sprite positionSprite(Sprite sprite, int noTile, int position) {
    sprite.setPosition(calculateTileX(sprite, noTile, position), calculateTileY(sprite));
    return sprite;
  }
}
