package logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class Level implements Serializable {

    private Food food;
    private Set<Block> blocks;
    private Set<Platform> platforms;
    private int width;
    private int height;
    private Random rnd = new Random();
    private String levelName;
    private Set<Wall> mazeLocations;
    private Set<Separator> subLevelSeparators;
    private Set<Entrance> entrances;
    private Map<Snake, Point[]> snakesBodies;
    private int xAxis;
    private int yAxis;
    private Map<Sublevels, List<Point>> subLevels;

    public Food getFood(){
      return food;
    }
    public void setFood(Food food){
        this.food = food;
    }

    /*public Block getBlock(){
      return block;
    }
    public void setBlock(Block block) {
        this.block = block;
    }*/
    public Set<Block> getBlocks() {return blocks;}

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLevelName() {
        return levelName;
    }

    public Set<Wall> getMazeLocations() { return mazeLocations; }

    public void setMazeLocations(Set<Wall> mazeLocations) {
        this.mazeLocations = mazeLocations;
    }
    public Set<Separator> getSubLevelSeparators() { return subLevelSeparators; }

    public void setSubLevelSeparators(Set<Separator> subLevelSeparators) { this.subLevelSeparators = subLevelSeparators; }

    public Set<Entrance> getEntrances() {
        return entrances;
    }
    public void setEntrances(Set<Entrance> entrances) {
        this.entrances = entrances;
    }

    public Map<Snake, Point[]> getSnakesBodies() { return snakesBodies; }

    public void setSnakesBodies(Map<Snake, Point[]> snakesBodies) {
        this.snakesBodies = snakesBodies;
    }

    public int getXAxis() {
        return xAxis;
    }

    public int getYAxis() {
        return yAxis;
    }

    public Map<Sublevels, List<Point>> getSubLevels() {
        return subLevels;
    }

    public Level(Config config, String level) {
        levelName = level;
        snakesBodies = new HashMap<>();
        mazeLocations = new HashSet<>();
        subLevelSeparators = new HashSet<>();
        entrances = new HashSet<>();
        subLevels = new HashMap<>();
        platforms = new HashSet<>();
        width = config.getFieldWidth();
        height = config.getFieldHeight();
        blocks = new HashSet<>();
        //generateBlocks();
        //generateFood();
  }

  public void initializeSnakes(List<Snake> snakes) {
    for (Snake snake : snakes) {
      snakesBodies.put(snake, new Point[height * width]);
      placeSnake(snake);
    }
  }

  public void generateFood() {
    food = new Food(findFreeSpot());
  }

  public void generateBlocks(){
    for(Platform platform : platforms){
        Block block = new Block(findFreeSpot(), 1, platform.getColor());
        if(block.getLocation().x == 1)
            block.setLocation(2, block.getLocation().y);
        if(block.getLocation().y == 1)
            block.setLocation(block.getLocation().x, 2);
        if(block.getLocation().x == 1)
            block.setLocation(22, block.getLocation().y);
        if(block.getLocation().y == 1)
            block.setLocation(block.getLocation().x, 22);
        blocks.add(block);
    }
  }

  public void createRandomField() {
    mazeLocations = new HashSet<>();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (new Random().nextInt(50) == 0) {
          mazeLocations.add(new Wall(x, y));
        }
      }
    }
    if (mazeLocations.size() == 0) {
      mazeLocations.add(new Wall(0, 0));
    }
    generateFood();
  }

  private void initAxis() {
    for (Separator separator : getSubLevelSeparators()) {
      if (separator.isHorizontal()) {
        xAxis = separator.getCoordinate();
      } else {
        yAxis = separator.getCoordinate();
      }
    }
  }

  public void createSublevels(Set<Separator> separators){
    setSubLevelSeparators(separators);
    initAxis();
  }
/*
   private Point findFreeSpot() {
     boolean repeat = true;
       while (repeat) {
           int x = rnd.nextInt(width - 1);
           int y = rnd.nextInt(height - 1);
           Point point = new Point(x, y);
           for (Wall wall : mazeLocations) {
             if (wall.getLocation().x == x && wall.getLocation().y == y) {
               repeat = true;
               break;
             }
           }
           for (Snake snake : snakesBodies.keySet()) {
             if (findSnakePartsOnBoard(snake).contains(point)){
               continue;
             }
           }
           return point;
       }
   }
*/
  private Point findFreeSpot() {
    while (true) {
      boolean repeat = false;
      int x = rnd.nextInt(width - 1);
      int y = rnd.nextInt(height - 1);
      for (Wall wall : mazeLocations) {
        if (wall.getLocation().x == x && wall.getLocation().y == y) {
          repeat = true;
          break;
        }
      }
      outer:
      for (Snake snake : snakesBodies.keySet()) {
        for (Point point : findSnakePartsOnBoard(snake)) {
          if (point.x == x && point.y == y) {
            repeat = true;
            break outer;
          }
        }
      }
      for (Platform platform : platforms) {
        if (platform.getLocation().x == x && platform.getLocation().y == y) {
          repeat = true;
          break;
        }
      }
      if (!repeat) {
        return new Point(x, y);
      }
    }
  }

  public Set<Entrance> findOpenEntrances() {
    Set<Entrance> openedEntrances = new HashSet<>();
    for (Entrance entry : getEntrances()) {
      if (entry.isOpen()) {
        openedEntrances.add(entry);
      }
    }
    return openedEntrances;
  }

  /**
   * Размещает змейку при первом запуске игры
   */
  private void placeSnake(Snake snake) {
    snakesBodies.get(snake)[0] = findFreeSpot();
  }

  /**
   * Размещает змейку с учётом того, что она вылезла из какого то входа
   *
   * @param previousLevel прерыдущий уровень
   */
  public void placeSnake(Level previousLevel, Snake snake) {
    Point prevSnakeHead = previousLevel.snakesBodies.get(snake)[0];
    char inputEntry = previousLevel.getOpenedEntranceName(prevSnakeHead);
    snakesBodies.get(snake)[0] = findOpenedEntry(inputEntry);
  }


  /**
   * Ищет в данном уровне открытый вход с названием inputEntry
   *
   * @return местоположение входа
   */
  public Point findOpenedEntry(char inputEntry) {
    Set<Entrance> openedEntrances = findOpenEntrances();
    for (Entrance openedEntry : openedEntrances) {
      if (openedEntry.getName() == inputEntry) {
        Point location = openedEntry.getLocation();
        return new Point(location.x, location.y);
      }
    }
    return null;
  }

  /**
   * Клетки поля, занятых змейкой
   */
  public Set<Point> findSnakePartsOnBoard(Snake snake) {
    Set<Point> result = new HashSet<Point>();
    for (Point point : snakesBodies.get(snake)) {
      if (point != null) {
        result.add(point);
      }
    }
    return result;
  }


  /**
   * Проверяем, находимся ли мы в ячейке с открытым входом.
   *
   * @return Вход, в который мы попали или null
   */
  public Entrance canMoveToNextLevel(Snake snake) {
    Point[] snakeLocations = snakesBodies.get(snake);
    for (Entrance entrance : entrances) {
      if (snakeLocations[0].x == entrance.getLocation().x &&
          snakeLocations[0].y == entrance.getLocation().y) {

        if (entrance.isOpen()) {
          return entrance;
        } else {
          return null;
        }
      }
    }
    return null;
  }


  /**
   * Ищет в данном уровне вход с местоположением location
   *
   * @return имя входа
   */
  public char getOpenedEntranceName(Point location) {
    Set<Entrance> openedEntrances = findOpenEntrances();
    for (Entrance openedEntry : openedEntrances) {
      Point entryLocation = openedEntry.getLocation();
      if (entryLocation.x == location.x && entryLocation.y == location.y) {
        return openedEntry.getName();
      }
    }
    //дефолтное значение для char == ошибка
    return '\u0000';
  }
}
