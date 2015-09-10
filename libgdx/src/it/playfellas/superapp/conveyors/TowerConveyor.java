package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;

public class TowerConveyor extends Conveyor {

  private Array<TileRepr> completeStackReprs;
  private Array<TileRepr> slotStackReprs;

  public TowerConveyor(BaseListener listener) {
    super(listener);
    completeStackReprs = new Array<TileRepr>();
    slotStackReprs = new Array<TileRepr>();
  }

  @Override public Array<TileRepr> getTileReprs() {
    tileReprs.clear();
    tileReprs.addAll(completeStackReprs);
    tileReprs.addAll(slotStackReprs);
    return super.getTileReprs();
  }

  public void updateCompleteStack(final Tile[] stack) {
    completeStackReprs.clear();
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        for (int i = 0; i < stack.length; i++) {
          Sprite tileSprite = makeSprite(stack[i]);
          // Setting correct dimension
          float tileSize = tileSprite.getWidth() * (1 - 0.25f * i);
          tileSprite.setSize(tileSize, tileSize);
          tileSprite = positionSprite(tileSprite, true);
          TileRepr tileRepr = new TileRepr(tileSprite, stack[i]);
          completeStackReprs.add(tileRepr);
        }
      }
    });
  }

  public void updateSlotStack(final Tile[] stack) {
    slotStackReprs.clear();
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        for (int i = 0; i < stack.length; i++) {
          Sprite tileSprite = makeSprite(stack[i]);
          // Setting correct dimension
          float tileSize = tileSprite.getWidth() * (1 - 0.25f * i);
          tileSprite.setSize(tileSize, tileSize);
          tileSprite = positionSprite(tileSprite, false);
          TileRepr tileRepr = new TileRepr(tileSprite, stack[i]);
          slotStackReprs.add(tileRepr);
        }
      }
    });
  }

  public Sprite positionSprite(Sprite sprite, boolean complete) {
    sprite.setPosition(calculateSpriteX(sprite, complete), calculateTileY(sprite));
    return sprite;
  }

  protected float calculateSpriteX(Sprite sprite, boolean complete) {
    float x = complete ? (width * 1 / 3) : (width * 2 / 3);
    x -= sprite.getWidth() / 2;
    return x;
  }

  @Override public void update() {

  }

  @Override public void start() {

  }

  @Override public void stop() {

  }

  @Override public void clear() {
    completeStackReprs.clear();
  }

  @Override public void addTile(Tile tile) {

  }
}
