package logic;

import java.awt.*;

public class Block {

  private Point location;
  private int value;

  public Point getLocation(){
    return location;
  }

  public int getValue(){
    return value;
  }

  public Block(Point coordinate, int value) {
    this.location = coordinate;
    this.value = value;
  }
}
