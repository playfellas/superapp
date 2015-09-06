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
import java.util.Iterator;

public class Conveyor implements ApplicationListener {

  private Bridge bridge;

  private float width = 800;
  private float height = 480;

  private float rtt = 5;
  private int pixelSpeed;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Array<Tile> tiles;
  private Vector3 touchPos;

  private boolean running = false;

  public Conveyor(Bridge bridge) {
    this.bridge = bridge;
  }

  @Override public void create() {
    // Creating sprite that contains the game scene
    batch = new SpriteBatch();
    // Creating the camera
    camera = new OrthographicCamera();
    width = Gdx.graphics.getWidth() / 2;
    height = Gdx.graphics.getHeight() / 2;
    camera.setToOrtho(false, width, height);
    tiles = new Array<Tile>();
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
      Iterator iterator = tiles.iterator();
      while (iterator.hasNext()) {
        Tile tile = (Tile) iterator.next();
        Sprite tileSprite = tile.getSprite();
        tileSprite.setX(tileSprite.getX() + pixelSpeed * Gdx.graphics.getDeltaTime());
        if (tileSprite.getX() > width) iterator.remove();
      }
    }

    // Touch check
    if (Gdx.input.isTouched()) {
      touchPos = new Vector3();
      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(touchPos);
      Iterator iterator = tiles.iterator();
      while (iterator.hasNext()) {
        Tile tile = (Tile) iterator.next();
        Rectangle tileRect = tile.getSprite().getBoundingRectangle();
        if (tileRect.contains(touchPos.x, touchPos.y)) {
          bridge.tileClicked(tile.getTileWrapper());
        }
      }
    }

    // Real drawing
    batch.begin();
    for (Tile tile : tiles) {
      tile.getSprite().draw(batch);
    }
    batch.end();
  }

  @Override public void pause() {

  }

  @Override public void resume() {

  }

  @Override public void dispose() {
    for (Tile tile : tiles) {
      tile.getSprite().getTexture().dispose();
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
    tiles.clear();
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
   * @return the current rtt.
   */
  public float getRTT() {
    return rtt;
  }

  /**
   * Adds a new tile to the conveyor.
   *
   * @param tileWrapper the wrapper containing the info needed to draw the tile.
   */
  public void addTile(final TileWrapper tileWrapper) {
    //Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        // Image
        Texture tileTexture = new Texture(tileWrapper.getName());
        Sprite tileSprite = new Sprite(tileTexture);
        // Size
        int tileSize = (int) ((height * 1) * tileWrapper.calculateSizeMultiplier());
        tileSprite.setSize(tileSize, tileSize);
        // Color
        //TODO
        // Direction
        // If the tile is directable rotates the tile of 90 degrees for the number of times represented by the direction of the tile.
        if (tileWrapper.isDirectable()) {
          for (int i = 0; i < tileWrapper.getDirection(); i++) {
            tileSprite.rotate90(true);
          }
        }

        tileSprite.setPosition(0 - tileSize, height / 2 - tileSize / 2);
        Tile tile = new Tile(tileWrapper, tileSprite);
        tiles.add(tile);
      }
    });
  }

  /**
   * Removes the given tile from the conveyor.
   *
   * @param tileWrapper the wrapper containing the info needed to draw the tile.
   */
  public void addTile(final TileWrapper tileWrapper) {
    //Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        // Image
        Texture tileTexture = new Texture(tileWrapper.getName());
        Sprite tileSprite = new Sprite(tileTexture);
        // Size
        int tileSize = (int) ((height * 1) * tileWrapper.calculateSizeMultiplier());
        tileSprite.setSize(tileSize, tileSize);
        // Color
        //TODO
        // Direction
        // If the tile is directable rotates the tile of 90 degrees for the number of times represented by the direction of the tile.
        if (tileWrapper.isDirectable()) {
          for (int i = 0; i < tileWrapper.getDirection(); i++) {
            tileSprite.rotate90(true);
          }
        }

        tileSprite.setPosition(0 - tileSize, height / 2 - tileSize / 2);
        Tile tile = new Tile(tileWrapper, tileSprite);
        tiles.add(tile);
      }
    });
  }
}
