package logic;


import java.awt.*;

public class Platform {

  private Point location;
  private int value;
  private int currentValue;
  private Colors color;

  public Point getLocation(){ return location; }

  public int getValue() { return value; }

  public Colors getColor(){ return color; }

  public int getCurrentValue() { return currentValue; }

  public void setCurrentValue(int value) { currentValue = value; }

  public Platform(Point coordinate, int value, Colors color) {
    this.location = coordinate;
    this.value = value;
    this.currentValue = 0;
    this.color = color;
  }
}
