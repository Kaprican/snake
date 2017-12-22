package logic;


import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

public class Game {

  private List<Snake> snakes = new ArrayList<>();
  private int score;
  private Config config;
  private Map<Platform, Entrance> closedEntrances = new HashMap<>();
  private List<Level> levels = new ArrayList<>();
  private boolean paused = false;
  private Level currentLevel;
  private boolean twoPlayers;

  public List<Snake> getSnakes() { return snakes; }

  public void setSnakes(List<Snake> list) { snakes = list; }

  public int getScore() { return  score; }

  public void setScore(int value) { score = value; }

  public Config getConfig() { return config; }

  public Map<Platform, Entrance> getClosedEntrances() { return  closedEntrances; }

  public void setClosedEntrances(Map<Platform, Entrance> value) { closedEntrances = value; }

  public List<Level> getLevels() { return levels; }

  public boolean isPaused() { return paused; }

  public void setPaused(boolean isPaused) {paused = isPaused; }

  public Level getCurrentLevel() { return currentLevel; }

  public void setCurrentLevel(Level level) { currentLevel = level; }


  public Game(Config config, Level level, boolean twoPlayers) {
    this.config = config;
    levels.add(level);
    currentLevel = level;
    this.twoPlayers = twoPlayers;
    placeSnakes(twoPlayers);
  }


  public Game(Config config, List<Level> levels, boolean twoPlayers) {
    this.config = config;
    closedEntrances = getClosedEntrances(levels);
    this.levels = levels;
    currentLevel = levels.get(0);
    placeSnakes(twoPlayers);
    this.twoPlayers = twoPlayers;
  }

  private void placeSnakes(boolean twoPlayers) {
    snakes.add(new Snake());
    if (twoPlayers) {
      snakes.add(new Snake());
    }

    for (Level level : levels) {
      level.initializeSnakes(getSnakes());
    }
  }

  private void addScore() {
    setScore(getScore() + 10);
    tryToOpenEntrance();
  }

  private void tryToOpenEntrance() {
      opened:
      for (Level level : levels) {
          for (Platform platform : level.getPlatforms()) {
              for (Entrance entrance : level.getEntrances()) {
                  if (platform.getValue() == platform.getCurrentValue()) {
                      entrance.setOpen(true);

                      getClosedEntrances().remove(platform);
                      break opened;
                  }
              }
          }
      }
  }


  /**
   * Найти все закрытые входы в игре (на всех уровнях) и поставить им в соответствие очки,
   * котовые нужно набрать для открытия двери
   *
   * @param levels уровни, которые существуют в этой игре
   * @return словарик, где ключь - это очки, а значение - "вход"/дверь уровня
   */
  private Map<Platform, Entrance> getClosedEntrances(List<Level> levels) {
    Map<Platform, Entrance> closedEntrances = new HashMap<>();
    //Integer count = 50;
    for (Level level : levels) {
        for(Platform platform : level.getPlatforms()){
            for (Entrance entrance : level.getEntrances()) {
                if (!entrance.isOpen()) {
                    closedEntrances.put(platform, entrance);
                 }
            }
        }
    }

    return closedEntrances;
  }

  public void findSnakesInSublevels() {
    List<Point> snakesPositions = new ArrayList<>();
    // кидаем все змеечные локации в один список
    for (Snake snake : getSnakes()) {
      snakesPositions.addAll(currentLevel.findSnakePartsOnBoard(snake));
    }
    int xAxis = currentLevel.getXAxis();
    int yAxis = currentLevel.getYAxis();
    //тут идёт фильтрация всех локаций змейки.
    //я пытаюсь тут отфильровать от всех змеек те клетки,
    //которые попадают в один из углов.
    //Соответственно, если в угле что то есть - рисуем поверх картинку
    List<Point> upperLeft = snakesPositions.stream()
        .filter(p -> p.x < xAxis && p.y < yAxis)
        .collect(Collectors.toList());
    List<Point> upperRight = snakesPositions.stream()
        .filter(p -> p.x > xAxis && p.y < yAxis)
        .collect(Collectors.toList());
    List<Point> lowerLeft = snakesPositions.stream()
        .filter(p -> p.x < xAxis && p.y > yAxis)
        .collect(Collectors.toList());
    List<Point> lowerRight = snakesPositions.stream()
        .filter(p -> p.x > xAxis && p.y > yAxis)
        .collect(Collectors.toList());

    currentLevel.getSubLevels().put(Sublevels.UpperLeft, upperLeft);
    currentLevel.getSubLevels().put(Sublevels.UpperRight, upperRight);
    currentLevel.getSubLevels().put(Sublevels.LowerRight, lowerRight);
    currentLevel.getSubLevels().put(Sublevels.LowerLeft, lowerLeft);

  }

  public void tryEatFood(Game game, Snake snake) {
    Point[] snakeLocations = currentLevel.getSnakesBodies().get(snake);
    Point snakeHead = snakeLocations[0];
    if (snakeHead.x == currentLevel.getFood().getLocation().x
        && snakeHead.y == currentLevel.getFood().getLocation().y) {
      snake.eatFood();
      game.addScore();
      currentLevel.generateFood();
      int i = snake.getLength() - 1;
      snakeLocations[i] = new Point(snakeLocations[i - 1].x, snakeLocations[i - 1].y);
    }
  }

  public void tryTakeBlock(Snake snake){
    Point[] snakeLocations = currentLevel.getSnakesBodies().get(snake);
    Point snakeHead = snakeLocations[0];
    for(Block block: currentLevel.getBlocks()){
        if(snakeHead.x == block.getLocation().x && snakeHead.y == block.getLocation().y){

        }
    }
    /*
    if (snakeHead.x == currentLevel.getBlock().getLocation().x
        && snakeHead.y == currentLevel.getBlock().getLocation().y) {
        int blockValue = currentLevel.getBlock().getValue();
        if (snake.getLength() < blockValue + snake.getCapacity()){
            snake.die();
        }
        snake.takeBlock(blockValue);
        currentLevel.generateBlock();
    }*/
  }

  public void tryMoveBlock(Set<Block> blocks) {
    int x = point.x;
    int y = point.y;
    Point next = new Point(x, y);
    for (Snake snake : snakes){
      if (snake.looksUp()){
          next.y -= 1;
      }
      if (snake.looksDown()){
          next.y += 1;
      }
      if (snake.looksLeft()){
          next.x -= 1;
      }
      if (snake.looksRight()){
          next.x += 1;
      }
    }
    if (currentLevel.getMazeLocations().contains(next)){

    }

  }
/*
  public void tryPutBlocks(Snake snake){
      Point[] snakeLocations = currentLevel.getSnakesBodies().get(snake);
      Point snakeHead = snakeLocations[0];
      for(Platform platform: currentLevel.getPlatforms()){
          if (snakeHead.x == platform.getLocation().x
              && snakeHead.y == platform.getLocation().y) {
              if (platform.getCurrentValue() + snake.getCapacity() < platform.getValue()) {
                  platform.setCurrentValue(platform.getCurrentValue() + snake.getCapacity());
                  snake.setCapacity(0);
              }
              else{
                  int countToFull = platform.getValue() - platform.getCurrentValue();
                  snake.setCapacity(snake.getCapacity() - countToFull);
                  platform.setCurrentValue(platform.getValue());
                  for(Entrance entrance: currentLevel.getEntrances()){
                      entrance.setOpen(true);
                  }
              }
          }
          snake.eatFood();
          currentLevel.generateFood();
          int i = snake.getLength() - 1;
          snakeLocations[i] = new Point(snakeLocations[i - 1].x, snakeLocations[i - 1].y);
      }

  }
      */

  public void moveSnake(Level level, Snake snake) {
    Point[] snakeLocations = level.getSnakesBodies().get(snake);
    Point snakeHead = snakeLocations[0];
    if (snakeLocations == null) {
      return;
    }
    int snakeOnBoard = level.findSnakePartsOnBoard(snake).size();
    Point tail = null;
    if (snakeOnBoard == 0) {
      return;
    }
    if (snakeOnBoard != snake.getLength()) {
      tail =
          new Point(snakeLocations[snakeOnBoard - 1].x, snakeLocations[snakeOnBoard - 1].y);
    }
    for (int i = snakeOnBoard - 1; i > 0; i--) {
      snakeLocations[i].x = snakeLocations[i - 1].x;
      snakeLocations[i].y = snakeLocations[i - 1].y;
    }

    if (snake.looksRight()) {
      snakeHead.x++;

    }
    if (snake.looksLeft()) {
      snakeHead.x--;

    }
    if (snake.looksUp()) {
      snakeHead.y--;

    }
    if (snake.looksDown()) {
      snakeHead.y++;
    }
    if (snakeOnBoard != snake.getLength()) {
      snakeLocations[snakeOnBoard] = tail;
    }

  }

  public void tryToDie(Snake snake) {
    Point[] snakeLocations = currentLevel.getSnakesBodies().get(snake);
    Point snakeHead = snakeLocations[0];
    int snakeOnBoard = currentLevel.findSnakePartsOnBoard(snake).size();
    if (snakeOnBoard == 0) {
      return;
    }
    for (int j = 2; j < snakeOnBoard; j++) {
      if (snakeHead.x == snakeLocations[j].x &&
          snakeHead.y == snakeLocations[j].y) {
        snake.die();
      }
    }
    if (snakeHead.x < 0 ||
        snakeHead.y < 0 ||
        snakeHead.x >= currentLevel.getWidth() ||
        snakeHead.y >= currentLevel.getHeight()) {
      snake.die();
    }
    for (Wall wall : currentLevel.getMazeLocations()) {
      if (snakeHead.x == wall.getLocation().x &&
          snakeHead.y == wall.getLocation().y) {
        snake.die();
      }
    }
    if (twoPlayers) {
      for (Snake anotherSnake : currentLevel.getSnakesBodies().keySet()) {
        if (anotherSnake == snake) {
          continue;
        }
        for (Point anotherSnakeLocation : currentLevel.findSnakePartsOnBoard(anotherSnake)) {
          if (snakeHead.x == anotherSnakeLocation.x &&
              snakeHead.y == anotherSnakeLocation.y) {
            snake.die();
          }
        }
      }
    }
  }

}
