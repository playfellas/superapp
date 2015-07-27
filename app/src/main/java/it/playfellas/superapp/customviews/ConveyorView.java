package it.playfellas.superapp.customviews;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import it.playfellas.superapp.R;
import lombok.Getter;

public class ConveyorView extends ImageView {

  public static final int RIGHT = 1;
  public static final int LEFT = -1;

  @Getter private Integer speed;
  @Getter private Integer direction;
  @Getter private Integer frameNum;
  private int overflow;
  private int src;
  private Bitmap baseBmp;
  private ObjectAnimator mover = null;

  public ConveyorView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Getting attributes;
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ConveyorView, 0, 0);
    try {
      speed = a.getInteger(R.styleable.ConveyorView_speed, 100);
      direction = a.getInteger(R.styleable.ConveyorView_direction, 0);
      frameNum = a.getInteger(R.styleable.ConveyorView_frame_num, 4);
      src = a.getResourceId(R.styleable.ConveyorView_src, 0);
    } finally {
      a.recycle();
    }

    init();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
    int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

    int minh = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();
    int h = resolveSizeAndState(minh, heightMeasureSpec, 1);
    overflow = baseBmp.getWidth();
    setMeasuredDimension(w + overflow * 2, h);
  }

  private void init() {
    baseBmp = BitmapFactory.decodeResource(getResources(), src);
    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), baseBmp);
    bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
    setBackground(bitmapDrawable);
  }

  private void initAnimator() {
    mover = ObjectAnimator.ofFloat(this, "translationX", 0, direction * overflow);
    mover.setInterpolator(new LinearInterpolator());
    mover.setRepeatCount(ValueAnimator.INFINITE);
    setSpeed(speed);
  }

  public void start() {
    initAnimator();
    mover.start();
  }

  public void stop() {
    mover.cancel();
  }

  public boolean isMoving() {
    return mover != null && mover.isRunning();
  }

  // Speed is defined in pixels/second
  public void setSpeed(Integer speed) {
    this.speed = speed;
    float seconds = (float) overflow / (float) speed;
    mover.setDuration((long) (seconds * 1000));
  }

  public void setDirection(Integer direction) {
    this.direction = direction;
    this.start();
  }
}
