package it.playfellas.superapp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.tiles.Tile;
import java.util.Iterator;

public class Conveyor implements ApplicationListener {

  public static final int RIGHT = 1;
  public static final int LEFT = -1;
  
  private Listener listener;

  private float width = 800;
  private float height = 480;

  private float rtt = 5;
  private int direction;
  private int pixelSpeed;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Array<TileRepr> tileReprs;
  private Vector3 touchPos;

  private boolean running = false;

  public Conveyor(Listener listener, float rtt, int direction) {
    this.listener = listener;
    this.rtt = rtt;
    this.direction = direction;
  }

  @Override public void create() {
    // Creating sprite that contains the game scene
    batch = new SpriteBatch();
    // Creating the camera
    camera = new OrthographicCamera();
    width = Gdx.graphics.getWidth() / 2;
    height = Gdx.graphics.getHeight() / 2;
    camera.setToOrtho(false, width, height);
    tileReprs = new Array<TileRepr>();
    //Setting the RTT the first time to calculate the speed considering the width.
    changeRTT(rtt);
  }

  @Override public void resize(int width, int height) {
  }

  @Override public void render() {
    // Clearing OpenGL scene
    Gdx.gl.glClearColor(0.47f, 0.47f, 0.47f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.update();
    // Setting projection matrix
    batch.setProjectionMatrix(camera.combined);

    // Moving tiles
    if (running) {
      Iterator iterator = tileReprs.iterator();
      while (iterator.hasNext()) {
        TileRepr tileRepr = (TileRepr) iterator.next();
        Sprite tileSprite = tileRepr.getSprite();
        if(direction == LEFT) {
          tileSprite.setX(tileSprite.getX() - pixelSpeed * Gdx.graphics.getDeltaTime());
        }else{
          tileSprite.setX(tileSprite.getX() + pixelSpeed * Gdx.graphics.getDeltaTime());
        }
        if (tileSprite.getX() > width || tileSprite.getX() < -tileSprite.getWidth()) iterator.remove();
      }
    }

    // Touch check
    if (Gdx.input.isTouched()) {
      touchPos = new Vector3();
      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(touchPos);
      Iterator iterator = tileReprs.iterator();
      while (iterator.hasNext()) {
        TileRepr tileRepr = (TileRepr) iterator.next();
        Rectangle tileRect = tileRepr.getSprite().getBoundingRectangle();
        if (tileRect.contains(touchPos.x, touchPos.y)) {
          listener.tileClicked(tileRepr.getTile());
        }
      }
    }

    // Real drawing
    batch.begin();
    for (TileRepr tileRepr : tileReprs) {
      tileRepr.getSprite().draw(batch);
    }
    batch.end();
  }

  @Override public void pause() {
    running = false;
  }

  @Override public void resume() {
    running = true;
  }

  @Override public void dispose() {
    for (TileRepr tileRepr : tileReprs) {
      tileRepr.getSprite().getTexture().dispose();
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
   * Change the direction of movement of the conveyor.
   *
   * @param direction int representing the direction. (LEFT or RIGHT)
   */
  public void changeDirection(int direction) {
    if(direction != LEFT && direction != RIGHT){
      throw new IllegalArgumentException("direction must be 1 or -1. See Conveyor.LEFT and Conveyor.RIGHT");
    }
    this.direction = direction;
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
    //Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        // Image
        Texture tileTexture = new Texture(tile.getName() + ".png");
        Sprite tileSprite = new Sprite(tileTexture);
        // Size
        float multiplier = tile.getSize().getMultiplier();
        int tileSize = (int) ((height * 1) * multiplier);
        tileSprite.setSize(tileSize, tileSize);
        // Color
        //TODO
        // Direction
        // If the tile is directable rotates the tile of 90 degrees for the number of times represented by the direction of the tile.
        if (tile.isDirectable()) {
          for (int i = 0; i < tile.getDirection().ordinal(); i++) {
            tileSprite.rotate90(true);
          }
        }
        if(direction == LEFT) {
          tileSprite.setPosition(width, height / 2 - tileSize / 2);
        }else {
          tileSprite.setPosition(0 - tileSize, height / 2 - tileSize / 2);
        }
        TileRepr tileRepr = new TileRepr(tileSprite, tile);
        tileReprs.add(tileRepr);
      }
    });
  }

  public float getRtt() {
    return rtt;
  }

  public int getDirection() {
    return direction;
  }
}
