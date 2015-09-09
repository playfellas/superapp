package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileType;

/**
 * Abstract class that must be extended to implement the different types of conveyor. It defines the
 * abstract method to retrieve
 * the sprites to be drawn by the Scene.
 */
public abstract class Conveyor {

  /**
   * Must return all the tile sprites to be drawn on the conveyor.
   *
   * @return the list of sprites.
   */
  public abstract Array<TileRepr> getTileReprs();

  protected float width;
  protected float height;
  protected float relativeVPosition;

  protected BaseListener listener;

  public Conveyor(BaseListener listener) {
    this.listener = listener;
  }

  /**
   * Method called every frame to allow the conveyor to update its state.
   */
  public abstract void update();

  public abstract void start();

  public abstract void stop();

  public abstract void clear();

  public abstract void addTile(Tile tile);

  public BaseListener getListener() {
    return listener;
  }

  /**
   * Calculates the vertical position of a Tile. It is se same for all th Conveyors
   */
  protected int calculateSpriteY(Sprite sprite){
    // Set the y considering the size and the relative position of the conveyor and the tile size
    int y = (int) ((height / 2 - sprite.getWidth() / 2) + relativeVPosition);
    return y;
  }

  /**
   * Constructs a new Sprite starting from a Tile. It applies alle the needed transformations.
   * Remember to set bounds to the sprite in order to display it in the right position.
   *
   * IMPORTANT: call this method from the libgdx thread:
   *
   *      Gdx.app.postRunnable()
   *
   *
   * @param tile to be represented in a Sprite.
   * @return tileSprite
   */
  protected Sprite makeSprite(Tile tile){
    // Image
    Texture tileTexture = new Texture(tile.getName() + ".png");
    Sprite tileSprite = new Sprite(tileTexture);
    // Size
    float multiplier = tile.getSize().getMultiplier();
    int tileSize = (int) ((height * 0.8) * multiplier);
    // Color
    if(tile.getType().equals(TileType.ABSTRACT)){
      tileSprite.setColor(Color.valueOf(tile.getColor().hex().replace("#", "")));
    }
    // Direction
    // If the tile is directable rotates the tile of 90 degrees for the number of times represented by the direction of the tile.
    if (tile.isDirectable()) {
      for (int i = 1; i <= tile.getDirection().ordinal(); i++) {
        tileSprite.rotate90(true);
      }
    }
    tileSprite.setSize(tileSize,tileSize);
    return tileSprite;
  }

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
