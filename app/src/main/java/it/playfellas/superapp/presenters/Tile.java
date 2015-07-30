package it.playfellas.superapp.presenters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import it.playfellas.superapp.R;
import it.playfellas.superapp.utils.Helper;
import lombok.Getter;

public class Tile {

  @Getter private ImageView view;
  private Context context;

  private it.playfellas.superapp.logic.common.tiles.Tile tileInfo;
  @Getter private int speed;
  @Getter private int direction;
  @Getter private ObjectAnimator animator = null;

  private int displayWidth;

  public Tile(Context context, it.playfellas.superapp.logic.common.tiles.Tile tileInfo, int speed, int direction) {
    this.tileInfo = tileInfo;
    this.speed = speed;
    this.direction = direction;
    this.context = context;
    this.displayWidth = Helper.calculateScreenWidth(context);
    view = new ImageView(context);
    view.setBackgroundResource(R.mipmap.ic_launcher);
    //TODO Add real tile info
    initAnimator();
  }

  /* Public Methods */

  public void start(){
    if(animator.isPaused()){
      animator.resume();
    } else {
      animator.start();
    }
  }

  public void pause(){
    animator.pause();
  }

  public void stop(){
    animator.cancel();
  }

  public void changeSpeed(int speed) {
    this.speed = speed;
    animator.setFloatValues(getCurrentX(), direction * displayWidth);
    animator.setDuration(calculateAnimTime());
  }

  /* Private Methods */

  private void initAnimator() {
    animator = ObjectAnimator.ofFloat(view, "translationX", getCurrentX(), direction * displayWidth);
    animator.setInterpolator(new LinearInterpolator());
    animator.setDuration(calculateAnimTime());
  }

  private long calculateAnimTime() {
    float seconds = ((float) displayWidth - getCurrentX()) / (float) speed;
    return (long) (seconds * 1000);
  }

  private float getCurrentX() {
    return animator != null ? (float) animator.getAnimatedValue() : 0;
  }

}
