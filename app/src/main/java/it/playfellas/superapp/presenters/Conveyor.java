package it.playfellas.superapp.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Conveyor {

  public static final int RIGHT = 1;
  public static final int LEFT = -1;

  @Getter private float speed;
  @Getter private int direction;
  @Getter private boolean moving = false;

  private List<Tile> tiles;
  private ViewGroup conveyorLayout;

  public Conveyor(ViewGroup conveyorLayout, float speed, int direction) {
    // Verify parameters
    if (direction != Conveyor.LEFT && direction != Conveyor.RIGHT) {
      String msg = "Direction must be 1 or -1. Check Conveyor class for static values";
      throw new InvalidParameterException(msg);
    }
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

  public void addTile(it.playfellas.superapp.logic.tiles.Tile tileInfo) {
    boolean wasMoving = moving;
    if (wasMoving) {
      pause();
    }
    final Tile tile = new Tile(conveyorLayout.getContext(), tileInfo, speed, direction);
    tile.getAnimator().addListener(getOrGenerateListener(tile));
    tiles.add(tile);
    conveyorLayout.addView(tile.getView());
    if (wasMoving) {
      start();
    }
  }

  public void changeSpeed(float speed) {
    this.speed = speed;
    boolean wasMoving = moving;
    if (wasMoving) {
      pause();
    }
    for (final Tile tile : tiles) {
      Animator.AnimatorListener listener = getOrGenerateListener(tile);
      tile.changeSpeed(speed);
      tile.setAnimatorListener(listener);
    }
    if (wasMoving) {
      start();
    }
  }

  public void changeDirection(int direction) {
    this.direction = direction;
    boolean wasMoving = moving;
    if (wasMoving) {
      pause();
    }
    for (final Tile tile : tiles) {
      Animator.AnimatorListener listener = getOrGenerateListener(tile);
      tile.changeDirection(direction);
      tile.getAnimator().addListener(listener);
    }
    if (wasMoving) {
      start();
    }
  }

  /* Private Methods */

  // Get the animator listener if there is one; otherwise create it
  private Animator.AnimatorListener getOrGenerateListener(final Tile tile) {
    Animator.AnimatorListener listener = tile.getAnimatorListener();
    if (listener == null) {
      listener = new AnimatorEndListener(tile);
    }
    return listener;
  }

  private void fixLayout() {
    RelativeLayout.LayoutParams params =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    //TODO fix the margins
  }



  public class AnimatorEndListener extends AnimatorListenerAdapter {

    private Tile tile;

    public AnimatorEndListener(Tile tile) {
      this.tile = tile;
    }

    @Override public void onAnimationEnd(Animator animation) {
      super.onAnimationEnd(animation);
      conveyorLayout.removeView(tile.getView());
      tiles.remove(tile);
    }
  }
}
