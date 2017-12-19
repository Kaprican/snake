package logic;


public class Separator {

  private int coordinate;
  private boolean isHorizontal;

  public int getCoordinate() { return coordinate; }

  public boolean isHorizontal() { return isHorizontal; }

  public Separator(int coordinate, boolean isHorizontal) {
    this.coordinate = coordinate;
    this.isHorizontal = isHorizontal;
  }
}
