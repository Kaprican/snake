package logic;

import java.awt.Point;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class Entrance {

    private Point location;
    private char name;
    private boolean open;
    private boolean isRedPushed;
    private boolean isBluePushed;
    private boolean isYellowPushed;
    private int countOfPlatforms;

    public int getCountOfPlatforms() {
        return countOfPlatforms;
    }

    public boolean isPushed(Colors color) {
        if (color == Colors.Red) {
            return isRedPushed;
        } else if (color == Colors.Blue) {
            return isBluePushed;
        } else {
            return isYellowPushed;
        }
    }

    public void setPushed(Colors color, boolean isPushed) {
        if(color == Colors.Red){
            isRedPushed = isPushed;
        }
        else if(color == Colors.Blue){
            isBluePushed = isPushed;
        }
        else if(color == Colors.Yellow){
            isYellowPushed = isPushed;
        }
    }

    public boolean isAllPushed(){
        return isYellowPushed && isBluePushed && isRedPushed;
    }

    public Point getLocation(){ return location; }

  public char getName(){ return name; }

  public void setOpen(boolean isOpen) { open = isOpen; }

  public Entrance(int x, int y, char name, boolean opened, int count) {
    location = new Point(x, y);
    open = opened;
    this.name = name;
    countOfPlatforms = count;
    if(count == 1){
        isRedPushed = false;
        isBluePushed = true;
        isYellowPushed = true;
    }
    else if(count == 2){
          isRedPushed = false;
          isBluePushed = false;
          isYellowPushed = true;
      }
      else if(count == 3){
          isRedPushed = false;
          isBluePushed = false;
          isYellowPushed = false;
      }
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
