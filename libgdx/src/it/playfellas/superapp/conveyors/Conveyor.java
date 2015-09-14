package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileType;

/**
 * Abstract class that must be extended to implement the different types of conveyor. It defines
 * the
 * abstract method to retrieve the sprites to be drawn by the Scene.
 */
public abstract class Conveyor {

  protected static final float tileHeightMult = 0.8f;

  private Sprite bgSprite;

  protected float width;
  protected float height;
  protected float relativeVPosition;

  private boolean greyscale = false;

  protected BaseListener listener;

  protected Array<TileRepr> tileReprs;

  public Conveyor(BaseListener listener) {
    this.listener = listener;
    tileReprs = new Array<TileRepr>();
  }

  /**
   * Method called every frame to allow the conveyor to update its state.
   */
  public abstract void update();

  public abstract void start();

  public abstract void stop();

  public void clear() {
    tileReprs.clear();
  }

  public abstract void addTile(Tile tile);

  public abstract void touch(Vector3 touchPos);

  public void toggleGreyscale() {
    greyscale = !greyscale;
  }

  public Array<TileRepr> getTileReprs() {
    return tileReprs;
  }

  ;

  /**
   * Method to be called when the conveyor is added to a scene
   */
  public abstract void init();

  public BaseListener getListener() {
    return listener;
  }

  /**
   * Calculates the vertical position of a Tile. It is se same for all th Conveyors
   */
  protected int calculateTileY(Sprite sprite) {
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
   * Gdx.app.postRunnable()
   *
   * @param tile to be represented in a Sprite.
   * @return tileSprite
   */
  protected Sprite makeSprite(Tile tile) {
    // Image
    Texture tileTexture;
    // If in greyscale mode, load the greyscale version of the texture
    if (greyscale && tile.getType().equals(TileType.CONCRETE)) {
      tileTexture = new Texture(tile.getName() + "_grey.png");
    } else {
      tileTexture = new Texture(tile.getName() + ".png");
    }
    tileTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
    Sprite tileSprite = new Sprite(tileTexture);
    // Size
    float multiplier = tile.getSize().getMultiplier();
    float tileSize = ((height * tileHeightMult) * multiplier);
    // Color
    if (tile.getType().equals(TileType.ABSTRACT) && !greyscale) {
      tileSprite.setColor(Color.valueOf(tile.getColor().hex().replace("#", "")));
    }
    // Direction
    // If the tile is directable rotates the tile of 90 degrees for the number of times represented by the direction of the tile.
    if (tile.isDirectable()) {
      for (int i = 0; i <= tile.getDirection().ordinal(); i++) {
        tileSprite.rotate90(true);
      }
    }
    tileSprite.setSize(tileSize, tileSize);
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

  public Sprite getBgSprite() {
    return bgSprite;
  }

  public void setBgSprite(Sprite bgSprite) {
    this.bgSprite = bgSprite;
  }
}
