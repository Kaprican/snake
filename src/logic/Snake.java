package logic;

import lombok.Getter;
import lombok.Setter;

public class Snake {

  @Getter
  private int length;
  @Getter
  @Setter
  private int capacity;
  @Getter
  private boolean alive;
  private boolean right;
  private boolean left;
  private boolean up;
  private boolean down;

  public Snake() {
    length = 1;
    alive = true;
  }


  public boolean looksRight() {
    return right;
  }

  public boolean looksLeft() {
    return left;
  }

  public boolean looksUp() {
    return up;
  }

  public boolean looksDown() {
    return down;
  }


  public void moveRight() {
    if (!left) {
      right = true;
      up = down = left = false;
    }
  }

  public void moveUp() {
    if (!down) {
      up = true;
      left = right = down = false;
    }
  }

  private void stopSnake() {
    left = right = down = up = false;
  }

  public void moveDown() {
    if (!down) {
      down = true;
      left = up = right = false;
    }
  }

  public void moveLeft() {
    if (!right) {
      left = true;
      right = up = down = false;
    }
  }

  public void addLength() {
    length++;
  }

  public void eatFood() {
    addLength();
  }

  public void die() {
    alive = false;
    stopSnake();
  }

  public void takeBlock(int value) {
      capacity = value;
  }
}
