package it.playfellas.superapp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.conveyors.Conveyor;

public class Scene implements ApplicationListener {

  public static final float PROPORTION = 1f;

  private static final float conveyorSizeMultiplier = 3f / 7f;

  private SceneListener sceneListener;

  private float width;
  private float height;
  private int screenWidth;
  private int screenHeight;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Vector3 touchPos;
  private Sprite sceneBgSprite;
  private Texture orangeBg;
  private Texture greenBg;

  private Conveyor conveyorUp;
  private Conveyor conveyorDown;
  private boolean inverted;

  public Scene(SceneListener sceneListener, int screenWidth, int screenHeight) {
    this.sceneListener = sceneListener;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  @Override public void create() {
    width = Gdx.graphics.getWidth() * PROPORTION;
    height = Gdx.graphics.getHeight() * PROPORTION;
    // Creating sprite that contains the game sceneListener
    batch = new SpriteBatch();
    // Creating the camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false, width, height);
    orangeBg = new Texture("_sfondo_arancio.png");
    greenBg = new Texture("_sfondo_verde.png");
    orangeBg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
    greenBg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
    sceneBgSprite = new Sprite(orangeBg);
    sceneBgSprite.setBounds(0, 0, screenWidth * PROPORTION, screenHeight * PROPORTION);

    Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
      @Override public boolean tap(float x, float y, int count, int button) {
        touchPos = new Vector3();
        touchPos.set(x, y, 0);
        camera.unproject(touchPos);

        Conveyor touchedConveyor = touchedConveyor(touchPos);
        if (touchedConveyor != null) {
          touchedConveyor.touch(touchPos);
        }
        return true;
      }
    }));

    sceneListener.onSceneReady(this);
  }

  @Override public void resize(int width, int height) {

  }

  @Override public void render() {
    // Clearing OpenGL sceneListener
    Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    if (inverted) {
      sceneBgSprite.setTexture(greenBg);
    } else {
      sceneBgSprite.setTexture(orangeBg);
    }
    camera.update();
    // Setting projection matrix
    batch.setProjectionMatrix(camera.combined);

    // Real drawing
    batch.begin();
    //Drawing scene background
    sceneBgSprite.draw(batch);
    //Up Conveyor
    if (conveyorUp != null) {
      // Drawing background
      if (conveyorUp.getBgSprite() != null) {
        conveyorUp.getBgSprite().draw(batch);
      }
      conveyorUp.update();
      for (TileRepr tileRepr : conveyorUp.getTileReprs()) {
        // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
        tileRepr.getSprite().draw(batch);
      }
    }

    // Down Conveyor
    if (conveyorDown != null) {
      // Drawing background
      if (conveyorDown.getBgSprite() != null) {
        conveyorDown.getBgSprite().draw(batch);
      }
      conveyorDown.update();
      for (TileRepr tileRepr : conveyorDown.getTileReprs()) {
        // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
        tileRepr.getSprite().draw(batch);
      }
    }

    batch.end();
  }

  /**
   * Method to determine the touched conveyor.
   *
   * @param touchPos the Vector3 representing the touch position.
   * @return a string representing the touched conveyor (value: CONVEYOR_UP, CONVEYOR_DOWN). null if
   * the middle space is touched.
   */
  private Conveyor touchedConveyor(Vector3 touchPos) {
    if (touchPos.y > calculateRelativeVPosition()) {
      return conveyorUp;
    }
    if (touchPos.y < height * conveyorSizeMultiplier) {
      return conveyorDown;
    }
    return null;
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
    conveyor.setHeight(height * conveyorSizeMultiplier);
    conveyor.setWidth(width);
    conveyor.setRelativeVPosition(calculateRelativeVPosition());
    conveyor.init();
    this.conveyorUp = conveyor;
  }

  public void addConveyorDown(Conveyor conveyor) {
    conveyor.setHeight(height * conveyorSizeMultiplier);
    conveyor.setWidth(width);
    conveyor.setRelativeVPosition(0);
    conveyor.init();
    this.conveyorDown = conveyor;
  }

  public void swapBackground() {
    inverted = !inverted;
  }

  private float calculateRelativeVPosition() {
    return height - height * conveyorSizeMultiplier;
  }

  /**
   * Interface to communicate to the android module
   */
  public interface SceneListener {
    void onSceneReady(Scene scene);
  }
}
