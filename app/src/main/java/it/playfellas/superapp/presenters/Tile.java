package it.playfellas.superapp.presenters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import it.playfellas.superapp.R;
import it.playfellas.superapp.utils.Helper;
import java.security.InvalidParameterException;
import java.util.List;
import lombok.Getter;

public class Tile {

  @Getter private ImageView view;

  private it.playfellas.superapp.logic.tiles.Tile tileInfo;
  @Getter private float speed;
  @Getter private int direction;
  @Getter private ObjectAnimator animator = null;

  private int displayWidth;

  public Tile(Context context, it.playfellas.superapp.logic.tiles.Tile tileInfo, float speed,
      int direction) {
    // Verify parameters
    if (direction != Conveyor.LEFT && direction != Conveyor.RIGHT) {
      String msg = "Direction must be 1 or -1. Check Conveyor class for static values";
      throw new InvalidParameterException(msg);
    }
    this.tileInfo = tileInfo;
    this.speed = speed;
    this.direction = direction;
    view = new ImageView(context);
    view.setBackgroundResource(R.mipmap.ic_launcher);
    //TODO Add real tile info
    //TODO change image dimension
    int tileWidth = ((BitmapDrawable) view.getBackground()).getBitmap().getWidth();
    this.displayWidth = Helper.calculateScreenWidth(context) + tileWidth;

    initAnimator();
  }

  /* Public Methods */

  public void start() {
    if (animator.isPaused()) {
      animator.resume();
    } else {
      animator.start();
    }
  }

  public void pause() {
    animator.pause();
  }

  public void stop() {
    animator.cancel();
  }

  public void changeSpeed(float speed) {
    if (this.speed == speed) {
      return;
    }
    this.speed = speed;
    float from = getCurrentX();
    float to = (Conveyor.RIGHT == direction) ? displayWidth : 0;
    animator = generateAnimator(from, to);
  }

  public void changeDirection(int direction) {
    if (this.direction == direction) {
      return;
    }
    this.direction = direction;
    float from = getCurrentX();
    float to = (Conveyor.RIGHT == direction) ? displayWidth : 0;
    animator = generateAnimator(from, to);
  }

  public Animator.AnimatorListener getAnimatorListener() {
    List<Animator.AnimatorListener> listeners = animator.getListeners();
    if (listeners != null && listeners.size() != 0) {
      return listeners.get(0);
    }
    return null;
  }

  public void setAnimatorListener(Animator.AnimatorListener listener) {
    animator.addListener(listener);
  }

  /* Private Methods */

  private void initAnimator() {
    float from = (Conveyor.RIGHT == direction) ? 0 : displayWidth;
    float to = (Conveyor.RIGHT == direction) ? displayWidth : 0;
    animator = generateAnimator(from, to);
  }

  // Generate a new ObjectAnimator given start and end points, using the current speed and direction.
  private ObjectAnimator generateAnimator(float from, float to) {
    ObjectAnimator newAnimator = ObjectAnimator.ofFloat(view, "translationX", from, direction * to);
    newAnimator.setDuration(calculateAnimTime());
    newAnimator.setInterpolator(new LinearInterpolator());
    return newAnimator;
  }

  private long calculateAnimTime() {
    return (long) (speed * 1000);
  }

  private float getCurrentX() {
    return animator != null ? (float) animator.getAnimatedValue() : 0;
  }
}
