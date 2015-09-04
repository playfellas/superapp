package it.playfellas.superapp;

public class TileWrapper {

  public static int DIRECTION_NONE = 4;

  private String name;
  private String color;
  private float sizeMultiplier;
  private int direction;

  public TileWrapper(String name, String color, float sizeMultiplier, int direction) {
    this.name = name;
    this.color = color;
    this.sizeMultiplier = sizeMultiplier;
    this.direction = direction;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public float getSizeMultiplier() {
    return sizeMultiplier;
  }

  public int getDirection() {
    return direction;
  }
}
