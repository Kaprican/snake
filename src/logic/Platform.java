package logic;


import java.awt.*;

public class Platform {

  private Point location;
  private int value;
  private int currentValue;
  private Color color;

  public Point getLocation(){ return location; }

  public int getValue() { return value; }

  public Color getColor(){ return color; }

  public int getCurrentValue() { return currentValue; }

  public void setCurrentValue(int value) { currentValue = value; }

  public Platform(Point coordinate, int value) {
    this.location = coordinate;
    this.value = value;
    this.currentValue = 0;
  }
}
