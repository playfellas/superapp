package it.playfellas.superapp.presenters;

import it.playfellas.superapp.customviews.ConveyorView;
import it.playfellas.superapp.customviews.TileView;
import java.util.ArrayList;
import java.util.List;

public class Conveyor {

  private ConveyorView conveyorView;
  private List<TileView> tileViewList;

  private boolean running = false;

  public Conveyor(ConveyorView conveyorView) {
    this.conveyorView = conveyorView;
    tileViewList = new ArrayList<>();
    init();
  }

  private void init() {

  }

  public void addTileView(TileView tileView) {
    tileViewList.add(tileView);
  }

  public void removeTileView(TileView tileView) {
    tileViewList.remove(tileView);
  }

  public void start() {
    conveyorView.start();
    for (TileView tileView : tileViewList) {
      tileView.start();
    }
    running = true;
  }

  public void stop() {
    conveyorView.stop();
    for (TileView tileView : tileViewList) {
      tileView.stop();
    }
    running = false;
  }

  public boolean isRunning() {
    return running;
  }

  public void changeSpeed(int speed) {
    if (running) {
      stop();
      conveyorView.setSpeed(speed);
      for (TileView tileView : tileViewList) {
        tileView.setSpeed(speed);
      }
      start();
    } else {
      conveyorView.setSpeed(speed);
      for (TileView tileView : tileViewList) {
        tileView.setSpeed(speed);
      }
    }
  }

  public void changeDirection(int direction){
    if (running) {
      stop();
      conveyorView.setDirection(direction);
      for (TileView tileView : tileViewList) {
        tileView.setDirection(direction);
      }
      start();
    } else {
      conveyorView.setDirection(direction);
      for (TileView tileView : tileViewList) {
        tileView.setDirection(direction);
      }
    }
  }
}
