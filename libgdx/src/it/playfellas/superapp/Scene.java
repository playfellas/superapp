package it.playfellas.superapp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

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
    Gdx.gl.glClearColor(0.47f, 0.47f, 0.47f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.update();
    // Setting projection matrix
    batch.setProjectionMatrix(camera.combined);

    // Real drawing
    batch.begin();
    //Up Conveyor
    if (conveyorUp != null) {
      conveyorUp.update();
      for (Sprite sprite : conveyorUp.getTileSprites()) {
        // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
        sprite.draw(batch);
      }
    }

    // Down Conveyor
    if (conveyorDown != null) {
      conveyorDown.update();
      for (Sprite sprite : conveyorDown.getTileSprites()) {
        // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
        sprite.draw(batch);
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
    for (Sprite sprite : conveyorUp.getTileSprites()) {
      sprite.getTexture().dispose();
    }
    for (Sprite sprite : conveyorDown.getTileSprites()) {
      sprite.getTexture().dispose();
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
