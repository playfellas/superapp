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
import it.playfellas.superapp.conveyors.Conveyor;
import java.util.Iterator;

public class Scene implements ApplicationListener {

  public static final float PROPORTION = 0.5f;

  private SceneListener sceneListener;

  private float width;
  private float height;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Vector3 touchPos;

  private Conveyor conveyorUp;
  private Conveyor conveyorDown;

  public Scene(SceneListener sceneListener) {
    this.sceneListener = sceneListener;
  }

  @Override public void create() {
    width = Gdx.graphics.getWidth() * PROPORTION;
    height = Gdx.graphics.getHeight() * PROPORTION;
    // Creating sprite that contains the game sceneListener
    batch = new SpriteBatch();
    // Creating the camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false, width, height);
    sceneListener.onSceneReady(this);

  }

  @Override public void resize(int width, int height) {

  }

  @Override public void render() {
    // Clearing OpenGL sceneListener
    Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.update();
    // Setting projection matrix
    batch.setProjectionMatrix(camera.combined);

    // Real drawing
    batch.begin();
    //Up Conveyor
    if (conveyorUp != null) {
      // Drawing background
      if(conveyorUp.getBgSprite() != null) {
        conveyorUp.getBgSprite().draw(batch);
      }
      conveyorUp.update();
      for (TileRepr tileRepr : conveyorUp.getTileReprs()) {
        // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
        tileRepr.getSprite().draw(batch);
      }
      // Touch check
      if (Gdx.input.isTouched()) {
        touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        Iterator iterator = conveyorUp.getTileReprs().iterator();
        while (iterator.hasNext()) {
          TileRepr tileRepr = (TileRepr) iterator.next();
          Rectangle tileRect = tileRepr.getSprite().getBoundingRectangle();
          if (tileRect.contains(touchPos.x, touchPos.y)) {
            conveyorUp.getListener().onTileClicked(tileRepr.getTile());
            conveyorUp.getTileReprs().removeValue(tileRepr, false);
          }
        }
      }
    }

    // Down Conveyor
    if (conveyorDown != null) {
      conveyorDown.update();
      for (TileRepr tileRepr : conveyorDown.getTileReprs()) {
        // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
        tileRepr.getSprite().draw(batch);
      }
      // Touch check
      if (Gdx.input.isTouched()) {
        touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        Iterator iterator = conveyorDown.getTileReprs().iterator();
        while (iterator.hasNext()) {
          TileRepr tileRepr = (TileRepr) iterator.next();
          Rectangle tileRect = tileRepr.getSprite().getBoundingRectangle();
          if (tileRect.contains(touchPos.x, touchPos.y)) {
            conveyorDown.getListener().onTileClicked(tileRepr.getTile());
            conveyorDown.getTileReprs().removeValue(tileRepr, false);
          }
        }
      }
    }
    batch.end();
  }

  @Override public void pause() {

  }

  @Override public void resume() {

  }

  @Override public void dispose() {
    // Cleaning resources.
    for (TileRepr tileRepr : conveyorUp.getTileReprs()) {
      tileRepr.getSprite().getTexture().dispose();
    }
    for (TileRepr tileRepr : conveyorDown.getTileReprs()) {
      tileRepr.getSprite().getTexture().dispose();
    }
  }

  public Conveyor getConveyorUp() {
    return conveyorUp;
  }

  public Conveyor getConveyorDown() {
    return conveyorDown;
  }

  public void addConveyorUp(Conveyor conveyor) {
    conveyor.setHeight(height / 3);
    conveyor.setWidth(width);
    conveyor.setRelativeVPosition(height * 2 / 3);
    this.conveyorUp = conveyor;
  }

  public void addConveyorDown(Conveyor conveyor) {
    conveyor.setHeight(height / 3);
    conveyor.setWidth(width);
    conveyor.setRelativeVPosition(0);
    this.conveyorDown = conveyor;
  }

  /**
   * Interface to communicate to the android module
   */
  public interface SceneListener {
    void onSceneReady(Scene scene);
  }
}
