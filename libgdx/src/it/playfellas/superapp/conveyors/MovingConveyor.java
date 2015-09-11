package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import java.util.Iterator;

public class MovingConveyor extends Conveyor {

  public static final int RIGHT = 1;
  public static final int LEFT = -1;

  private float rtt = 5;
  private int direction;
  private int pixelSpeed;
  private boolean running = false;

  public MovingConveyor(BaseListener listener, float rtt, int direction) {
    super(listener);
    this.rtt = rtt;
    this.direction = direction;
    changeRTT(rtt);
  }

  @Override public void init() {
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        Texture bgTexture = new Texture("_conveyor_bg.png");
        bgTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        Sprite bgSprite = new Sprite(bgTexture);
        bgSprite.setBounds(0, relativeVPosition, width, height);
        setBgSprite(bgSprite);
      }
    });
  }


  @Override public void update() {
    // Moving tiles
    if (running) {
      Iterator iterator = tileReprs.iterator();
      while (iterator.hasNext()) {
        TileRepr tileRepr = (TileRepr) iterator.next();
        Sprite tileSprite = tileRepr.getSprite();
        if (direction == LEFT) {
          tileSprite.setX(tileSprite.getX() - pixelSpeed * Gdx.graphics.getDeltaTime());
        } else {
          tileSprite.setX(tileSprite.getX() + pixelSpeed * Gdx.graphics.getDeltaTime());
        }
        if (tileSprite.getX() > width || tileSprite.getX() < -tileSprite.getWidth()) {
          iterator.remove();
        }
      }
    }
  }

    /* API */

  /**
   * Starts moving all the tiles on the conveyor.
   */
  @Override public void start() {
    running = true;
  }

  /**
   * Stops moving all the tiles on the conveyor.
   */
  @Override public void stop() {
    running = false;
  }

  /**
   * Handles a touche event
   */
  @Override public void touch(Vector3 touchPos) {
    Iterator iterator = getTileReprs().iterator();
    while (iterator.hasNext()) {
      TileRepr tileRepr = (TileRepr) iterator.next();
      Rectangle tileRect = tileRepr.getSprite().getBoundingRectangle();
      if (tileRect.contains(touchPos.x, touchPos.y)) {
        listener.onTileClicked(tileRepr.getTile());
        tileReprs.removeValue(tileRepr, false);
      }
    }
  }

  /**
   * @return a boolean that indicates if the conveyor is running.
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Change the rtt (Round Trip Time) that is the time a tile spend to go through the conveyor.
   *
   * @param rtt the new round trip time.
   */
  public void changeRTT(float rtt) {
    this.rtt = rtt;
    pixelSpeed = (int) (width / rtt);
  }

  /**
   * Increment the RTT of a given value.
   *
   * @param increment the increment in seconds.
   */
  public void incrementRTT(float increment) {
    this.rtt += increment;
    pixelSpeed = (int) (width / rtt);
  }

  /**
   * Increment the RTT of a given value.
   *
   * @param decrement the decrement in seconds.
   */
  public void decrementRTT(float decrement) {
    this.rtt -= decrement;
    updatePixelSpeed();
  }

  /**
   * Change the direction of movement of the conveyor.
   *
   * @param direction int representing the direction. (LEFT or RIGHT)
   */
  public void changeDirection(int direction) {
    if (direction != LEFT && direction != RIGHT) {
      throw new IllegalArgumentException(
          "direction must be 1 or -1. See Conveyor.LEFT and Conveyor.RIGHT");
    }
    this.direction = direction;
  }

  /**
   * Invert the direction of movement of the conveyor.
   */
  public void invertDirection() {
    this.direction *= -1;
  }

  /**
   * @return the current rtt.
   */
  public float getRTT() {
    return rtt;
  }

  /**
   * Adds a new tile to the conveyor.
   *
   * @param tile the tile to be drawn
   */
  public void addTile(final Tile tile) {
    // Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        Sprite tileSprite = makeSprite(tile);

        tileSprite.setPosition(calculateSpriteX(tileSprite), calculateTileY(tileSprite));

        TileRepr tileRepr = new TileRepr(tileSprite, tile);
        tileReprs.add(tileRepr);
      }
    });
  }

  private void updatePixelSpeed() {
    pixelSpeed = (int) (width / rtt);
  }

  public float getRtt() {
    return rtt;
  }

  public int getDirection() {
    return direction;
  }

  private float calculateSpriteX(Sprite sprite) {
    float tileSize = sprite.getWidth();
    float x;
    if (direction == LEFT) {
      x = (int) width;
    } else {
      x = 0 - tileSize;
    }
    return x;
  }

  /**
   * Set the conveyor width and also update the speed.
   *
   * @param width the new width.
   */
  @Override public void setWidth(float width) {
    super.setWidth(width);
    updatePixelSpeed();
  }
}
