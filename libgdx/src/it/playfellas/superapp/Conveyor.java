package it.playfellas.superapp;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.listeners.BaseListener;

/**
 * Abstract class that must be extended to implement the different types of conveyor. It defines the abstract method to retrieve
 * the sprites to be drawn by the Scene.
 */
public abstract class Conveyor {

  /**
   * Must return all the tile sprites to be drawn on the conveyor.
   * @return the list of sprites.
   */
  public abstract Array<TileRepr> getTileReprs();

  protected float width;
  protected float height;
  protected float relativeVPosition;

  /**
   * Method called every frame to allow the conveyor to update its state.
   */
  public abstract void update();

  public abstract void start();

  public abstract BaseListener getListener();

  public void setHeight(float height) {
    this.height = height;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public void setRelativeVPosition(float relativeVPosition) {
    this.relativeVPosition = relativeVPosition;
  }
}
