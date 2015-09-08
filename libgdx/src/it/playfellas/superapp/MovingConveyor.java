package it.playfellas.superapp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.tiles.Tile;
import java.util.Iterator;

public class MovingConveyor extends Conveyor {

  private MovingConveyorListenerInterface listener;

  public static final int RIGHT = 1;
  public static final int LEFT = -1;

  private float rtt = 5;
  private int direction;
  private int pixelSpeed;
  private boolean running = false;

  private Array<TileRepr> tileReprs;

  public MovingConveyor(MovingConveyorListenerInterface listener, float rtt, int direction) {
    this.listener = listener;
    this.rtt = rtt;
    this.direction = direction;
    tileReprs = new Array<TileRepr>();
    changeRTT(rtt);
  }

  @Override public Array<Sprite> getTileSprites() {
    Array<Sprite> sprites = new Array<Sprite>();
    for (TileRepr tileRepr : tileReprs) {
      sprites.add(tileRepr.getSprite());
    }
    return sprites;
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
  public void start() {
    running = true;
  }

  /**
   * Stops moving all the tiles on the conveyor.
   */
  public void stop() {
    running = false;
  }

  /**
   * @return a boolean that indicates if the conveyor is running.
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Removes all the tiles from the conveyor. It leaves all the parameters unchanged and doesn't
   * change the conveyor state.
   */
  public void clear() {
    tileReprs.clear();
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
        // Image
        Texture tileTexture = new Texture(tile.getName() + ".png");
        Sprite tileSprite = new Sprite(tileTexture);
        // Size
        float multiplier = tile.getSize().getMultiplier();
        int tileSize = (int) ((height * 0.5) * multiplier);
        // Color
        //TODO
        // Direction
        // If the tile is directable rotates the tile of 90 degrees for the number of times represented by the direction of the tile.
        if (tile.isDirectable()) {
          for (int i = 0; i < tile.getDirection().ordinal(); i++) {
            tileSprite.rotate90(true);
          }
        }
        int x;
        // Set the y considering the size and the relative position of the conveyor and the tile size
        int y = (int) ((height / 2 - tileSize / 2) + relativeVPosition);
        if (direction == LEFT) {
          x = (int) width;
        } else {
          x = 0 - tileSize;
        }
        tileSprite.setBounds(x, y, tileSize, tileSize);
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

  /**
   * Set the conveyor width and also update the speed.
   *
   * @param width the new width.
   */
  @Override public void setWidth(float width) {
    super.setWidth(width);
    updatePixelSpeed();
  }

  public interface MovingConveyorListenerInterface {
    void tileClicked(Tile tile);
  }
}
