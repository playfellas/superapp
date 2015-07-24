package it.playfellas.superapp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import it.playfellas.superapp.R;
import lombok.Getter;

public class ConveyorView extends ImageView {

  @Getter private Integer speed;
  @Getter private Integer direction;
  @Getter private Integer frameNum;
  private int src;
  private Bitmap baseBmp;

  private AnimationDrawable animation;

  public ConveyorView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Getting attributes;
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ConveyorView, 0, 0);
    try {
      speed = a.getInteger(R.styleable.ConveyorView_speed, 5);
      direction = a.getInteger(R.styleable.ConveyorView_direction, 0);
      frameNum = a.getInteger(R.styleable.ConveyorView_frame_num, 4);
      src = a.getResourceId(R.styleable.ConveyorView_src, 0);
    } finally {
      a.recycle();
    }

    init();
  }

  private void init() {
    baseBmp = BitmapFactory.decodeResource(getResources(), src);
    animation = new AnimationDrawable();
    // Creating frames and adding them to the animation list
    int delta = baseBmp.getWidth() / frameNum;
    if (direction == 1) {
      for (int i = frameNum - 1; i >= 0; i--) {
        Bitmap bmp = Bitmap.createBitmap(baseBmp, i * delta, 0, delta, baseBmp.getHeight());
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
        animation.addFrame(bitmapDrawable, 160 - speed * 20);
      }
    } else {
      for (int i = 0; i < frameNum; i++) {
        Bitmap bmp = Bitmap.createBitmap(baseBmp, i * delta, 0, delta, baseBmp.getHeight());
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
        animation.addFrame(bitmapDrawable, 160 - speed * 20);
      }
    }
    animation.setOneShot(false);
    setBackground(animation);
  }

  public void start() {
    ((AnimationDrawable) getBackground()).start();
  }

  public void stop() {
    ((AnimationDrawable) getBackground()).stop();
  }

  public boolean isMoving() {
    return ((AnimationDrawable) getBackground()).isRunning();
  }

  public void setSpeed(Integer speed) {
    this.speed = speed;
    invalidate();
    requestLayout();
  }

  public void setDirection(Integer direction) {
    this.direction = direction;
    invalidate();
    requestLayout();
  }
}
