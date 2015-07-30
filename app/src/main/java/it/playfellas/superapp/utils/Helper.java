package it.playfellas.superapp.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Helper {

  public static int calculateScreenWidth(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display d = wm.getDefaultDisplay();
    Point size = new Point();
    d.getSize(size);
    return size.x;
  }
}
