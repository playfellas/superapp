package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;

public class TutorialMovingConveyor extends MovingConveyor {
  public TutorialMovingConveyor(BaseListener listener, float rtt, int direction) {
    super(listener, rtt, direction);
  }

  public void addTileWrong(final Tile tile) {
    // Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        Sprite tileSprite = makeSprite(tile);
        tileSprite.setColor(new Color(1f, 1f, 1f, 0.6f));
        tileSprite.setPosition(calculateSpriteX(tileSprite), calculateTileY(tileSprite));
        TileRepr tileRepr = new TileRepr(tileSprite, tile);
        tileReprs.add(tileRepr);
      }
    });
  }
}
