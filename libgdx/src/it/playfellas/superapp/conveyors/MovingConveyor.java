package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import it.playfellas.superapp.CompositeBgSprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;

public class MovingConveyor extends Conveyor {

  public static final int RIGHT = 1;
  public static final int LEFT = -1;

  private float rtt = 5;
  private int direction;
  private int pixelSpeed;
  private boolean running = false;

  private Texture fragmentTexture;
  private CompositeBgSprite bgSprite;
  private float fragmentWidth = 1f;

  public MovingConveyor(BaseListener listener, float rtt, int direction) {
    super(listener);
    this.rtt = rtt;
    this.direction = direction;
    changeRTT(rtt);
  }

  @Override public void init() {
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        fragmentTexture = new Texture("_conveyor_bg_fragment.png");
        fragmentTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        fragmentWidth = fragmentTexture.getWidth() / 1.5f;
        bgSprite = new CompositeBgSprite();
        float noFloatFragment = width / fragmentWidth;
        // Rounding
        int noFragment = (int) (noFloatFragment) + ((noFloatFragment % 1) == 0 ? 0 : 1);
        for (int i = -1; i < noFragment + 1; i++) {
          Sprite fragmentSprite = new Sprite(fragmentTexture);
          fragmentSprite.setBounds(i * fragmentWidth, relativeVPosition, fragmentWidth, height);
          bgSprite.addSprite(fragmentSprite);
        }
        setBgSprite(bgSprite);
      }
    });
  }

  @Override public void update() {
    // Moving tiles
    if (running) {
      // Updating background
      Array<Sprite> sprites = bgSprite.getSprites();
      boolean slide = false;
      if (direction == LEFT){
        if (sprites.get(sprites.size - 1).getX() < width){
          slide = true;
        }
      }else{
        if (sprites.get(0).getX() > 0){
          slide = true;
        }
      }
      for (Sprite sprite : sprites) {
        if(slide){
          if(direction == LEFT) {
            sprite.setX(sprite.getX() + fragmentWidth);
          }else{
            sprite.setX(sprite.getX() - fragmentWidth);
          }
        }
        if (direction == LEFT) {
          sprite.setX(sprite.getX() - pixelSpeed * Gdx.graphics.getDeltaTime());
        } else {
          sprite.setX(sprite.getX() + pixelSpeed * Gdx.graphics.getDeltaTime());
        }
      }
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

  @Override
  public void addTile(final TutorialTile tile) {
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

  private void updatePixelSpeed() {
    pixelSpeed = (int) (width / rtt);
  }

  public float getRtt() {
    return rtt;
  }

  public int getDirection() {
    return direction;
  }

  protected float calculateSpriteX(Sprite sprite) {
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
