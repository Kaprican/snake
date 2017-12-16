package logic;

import lombok.Getter;

import java.awt.*;

public class Block {

  @Getter
  private Point location;
  @Getter
  private int value;

  public Block(Point coordinate, int value) {
    this.location = coordinate;
    this.value = value;
  }
}
