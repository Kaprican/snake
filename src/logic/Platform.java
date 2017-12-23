package logic;


import java.awt.*;

public class Platform {

  private Point location;
  private boolean isPushed;
  private Colors color;

  public Point getLocation(){ return location; }

  public void setPushed(boolean pushed) {
      isPushed = pushed;
  }

  public boolean isPushed() {
      return isPushed;
  }

  public Colors getColor(){ return color; }

  public Platform(Point coordinate, boolean isPushed, Colors color) {
    this.location = coordinate;
    this.isPushed = isPushed;
    this.color = color;
  }
}
