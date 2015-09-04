package it.playfellas.superapp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;

public class Conveyor implements ApplicationListener {

  private float width = 800;
  private float height = 480;

  private float rtt = 5;
  private int pixelSpeed;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Array<Tile> tiles;
  private boolean running = false;

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
    //Clearing OpenGL scene
    Gdx.gl.glClearColor(0.47f, 0.47f, 0.47f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.update();
    //Setting projection matrix
    batch.setProjectionMatrix(camera.combined);

    //Moving tiles
    if (running) {
      Iterator iterator = tiles.iterator();
      while (iterator.hasNext()) {
        Tile tile = (Tile) iterator.next();
        Sprite tileSprite = tile.getSprite();
        tileSprite.setX(tileSprite.getX() + pixelSpeed * Gdx.graphics.getDeltaTime());
        if (tileSprite.getX() > width) iterator.remove();
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
   * Removes all the tiles from the conveyor. It leaves all the parameters unchanged and doesn't change the conveyor state.
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
        Texture tileTexture = new Texture(tileWrapper.getName());
        int tileSize = (int) ((height * 1) * tileWrapper.getSizeMultiplier());
        Sprite tileSprite = new Sprite(tileTexture);
        tileSprite.setPosition(0 - tileSize, height / 2 - tileSize / 2);
        tileSprite.setSize(tileSize, tileSize);
        // Check if the tile is directionable. If it is rotates the tile of 90 degrees for the number of times represented
        // by the direction of the tile.
        if(tileWrapper.getDirection() < TileWrapper.DIRECTION_NONE) {
          for (int i = 0; i < tileWrapper.getDirection(); i++) {
            tileSprite.rotate90(true);
          }
        }
        Tile tile = new Tile(null, tileSprite);
        tiles.add(tile);
      }
    });
  }
}
