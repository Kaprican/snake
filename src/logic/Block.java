package logic;

import java.awt.*;

public class Block {

  private Point location;
  private int value;
  private Color color;

  public Point getLocation(){
    return location;
  }

  public void setLocation(int x, int y) {
      location = new Point(x, y);
  }

  public int getValue(){
    return value;
  }

  public Color getColor() { return color; }

  public Block(Point coordinate, int value, Color color) {
    this.location = coordinate;
    this.value = value;
    this.color = color;
  }

  public void tryMoveBlock(Snake snake, Point point) {
    if (snake.looksUp()){
      if ()
    }
  }
}
