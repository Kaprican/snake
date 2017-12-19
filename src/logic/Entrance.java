package logic;

import java.awt.Point;
import lombok.Getter;
import lombok.Setter;

public class Entrance {

  private Point location;
  private char name;
  private boolean open;

  public Point getLocation(){ return location; }

  public char getName(){ return name; }

  public void setOpen(boolean isOpen) { open = isOpen; }

  public Entrance(int x, int y, char name, boolean opened) {
    location = new Point(x, y);
    open = opened;
    this.name = name;
  }

  public boolean isOpen() {
    return open;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Entrance entrance = (Entrance) obj;

    return location != null ? location.equals(entrance.location) : entrance.location == null;
  }

  @Override
  public int hashCode() {
    return location != null ? location.hashCode() : 0;
  }

}
