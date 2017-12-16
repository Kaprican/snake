package logic;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Platform {

  @Getter
  private Point location;
  @Getter
  private int value;
  @Getter
  @Setter
  private int currentValue;

  public Platform(Point coordinate, int value) {
    this.location = coordinate;
    this.value = value;
    this.currentValue = 0;
  }
}
