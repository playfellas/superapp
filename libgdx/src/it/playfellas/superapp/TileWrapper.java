package it.playfellas.superapp;

/**
 * Wrapper used to pass Tile informations to the libgdx module. The fields size, direction, shape and type are int
 * representing the position of the their respective enum constant.
 */
public class TileWrapper {


  private String name;
  private String color;
  private int size;
  private int direction;
  private int shape;
  private int type;

  private boolean directable;

  public TileWrapper(String name, String color, int direction, int size, int shape,
      int type) {
    this.name = name;
    this.color = color;
    this.direction = direction;
    this.size = size;
    this.shape = shape;
    this.type = type;
    if(direction < 4) directable = true;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public int getSize() {
    return size;
  }

  public int getDirection() {
    return direction;
  }

  public int getShape() {
    return shape;
  }

  public int getType() {
    return type;
  }

  public boolean isDirectable() {
    return directable;
  }

  /**
   * Calculates the multiplier factor from the enum ordinal.
   *
   * @return the multiplier factor.
   */
  public float calculateSizeMultiplier() {
    return 0.25f + 0.25f * this.size;
  }
}
