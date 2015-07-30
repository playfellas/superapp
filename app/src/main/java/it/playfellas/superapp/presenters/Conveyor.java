package it.playfellas.superapp.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class Conveyor {

  public static final int RIGHT = 1;
  public static final int LEFT = -1;

  private List<Tile> tiles;

  private ViewGroup conveyorLayout;

  @Getter @Setter private int speed;
  @Getter @Setter private int direction;
  @Getter private boolean moving = false;

  public Conveyor(ViewGroup conveyorLayout, int speed, int direction) {
    this.conveyorLayout = conveyorLayout;
    this.speed = speed;
    this.direction = direction;
    tiles = new ArrayList<>();
  }

  /* Public Methods */

  public void start() {
    if (moving) return;
    for (Tile tile : tiles) {
      tile.start();
    }
    moving = true;
  }

  public void pause() {
    if (!moving) return;
    for (Tile tile : tiles) {
      tile.pause();
    }
    moving = false;
  }

  public void stop() {
    if (!moving) return;
    for (Tile tile : tiles) {
      tile.stop();
    }
    moving = false;
  }

  public void clear() {
    conveyorLayout.removeAllViews();
    tiles.clear();
    moving = false;
  }

  public void addTile(it.playfellas.superapp.logic.common.tiles.Tile tileInfo) {
    boolean wasMoving = moving;
    if (wasMoving) {
      pause();
    }
    final Tile tile = new Tile(conveyorLayout.getContext(), tileInfo, speed, direction);
    tile.getAnimator().addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        conveyorLayout.removeView(tile.getView());
        tiles.remove(tile);
      }
    });
    tiles.add(tile);
    conveyorLayout.addView(tile.getView());
    if (wasMoving) {
      start();
    }
  }

  public void changeSpeed(int speed) {
    this.speed = speed;
    boolean wasMoving = moving;
    if (wasMoving) {
      pause();
    }
    for (Tile tile : tiles) {
      tile.changeSpeed(speed);
    }
    if (wasMoving) {
      start();
    }
  }

  /* Private Methods */

  private void fixLayout() {
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    //TODO fix the margins
  }
}
