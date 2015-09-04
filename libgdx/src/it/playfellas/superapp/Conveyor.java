package it.playfellas.superapp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;

public class Conveyor implements ApplicationListener {

  private float width = 800;
  private float height = 480;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Array<Tile> tiles;

  @Override public void create() {
    // Creating sprite that contains the game scene
    batch = new SpriteBatch();
    // Creating the camera
    camera = new OrthographicCamera();
    //TODO: dynamic width and height
    camera.setToOrtho(false, width, height);
    tiles = new Array<Tile>();
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
    Iterator iterator = tiles.iterator();
    while (iterator.hasNext()) {
      Tile tile = (Tile) iterator.next();
      Sprite tileSprite = tile.getSprite();
      tileSprite.setX(tileSprite.getX() + 200 * Gdx.graphics.getDeltaTime());
      if (tileSprite.getX() > width) iterator.remove();
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

  }

  /* API */

  public void start() {

  }

  public void stop() {

  }

  public void clear() {

  }

  public void addTile(final String imageName) {
    //Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
    Gdx.app.postRunnable(new Runnable() {
      @Override public void run() {
        Texture tileTexture = new Texture(imageName);
        Sprite tileSprite = new Sprite(tileTexture);
        tileSprite.setPosition(0,0);
        tileSprite.setSize(128, 128);
        Tile tile = new Tile(null, tileSprite);
        tiles.add(tile);
      }
    });
  }
}
