package it.playfellas.superapp.ui.slave;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.List;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.BitmapUtils;
import lombok.Getter;

public class Tile {

    private static final String DRAWABLE_RESOURCE = "drawable";
    private static final String PACKAGE_NAME = "it.playfellas.superapp";

    @Getter
    private ImageView view;

    private it.playfellas.superapp.logic.tiles.Tile tileInfo;
    @Getter
    private float speed;
    @Getter
    private int direction;
    @Getter
    private ObjectAnimator animator = null;

    private int displayWidth;

    public Tile(final Context context, final it.playfellas.superapp.logic.tiles.Tile tileInfo, float speed,
                int direction) {
        // Verify parameters
        if (direction != Conveyor.LEFT && direction != Conveyor.RIGHT) {
            String msg = "Direction must be 1 or -1. Check Conveyor class for static values";
            throw new InvalidParameterException(msg);
        }
        this.tileInfo = tileInfo;
        this.speed = speed;
        this.direction = direction;
        view = new ImageView(context) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                super.setMeasuredDimension(100, 100);
            }
        };

        int resId = context.getResources().getIdentifier(tileInfo.getName(), DRAWABLE_RESOURCE, PACKAGE_NAME);
        Bitmap origBitmap = BitmapFactory.decodeResource(context.getResources(), resId);

        view.setImageBitmap(origBitmap);

        //ricycle but not now. Use this to recycle a origBitmap when you have also a new Bitmap obtained from BitamUtils
        //origBitmap.recycle();

        //TODO Add real tile info
        //TODO change image dimension

        this.displayWidth = this.calculateScreenWidth(context) + 100;/* + tileWidth */

        this.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getVisibility() != View.INVISIBLE) {
                    TenBus.get().post(EventFactory.clickedTile(tileInfo));
                }
                v.setVisibility(View.INVISIBLE);
            }
        });

        initAnimator();
    }


    public Bitmap getBitmapFromAssets(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    /**
     * Loads image from file system.
     *
     * @param context         the application context
     * @param filename        the filename of the image
     * @param originalDensity the density of the image, it will be automatically
     *                        resized to the device density
     * @return image drawable or null if the image is not found or IO error occurs
     */
    public Drawable loadImageFromFilesystem(Context context, String filename, int originalDensity) {
        Drawable drawable = null;
        InputStream is = null;

        // set options to resize the image
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDensity = originalDensity;

        try {
            is = context.openFileInput(filename);
            drawable = Drawable.createFromResourceStream(context.getResources(), null, is, filename, opts);
        } catch (Throwable e) {
            Log.d("tile", "tile", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable e1) {
                    Log.d("tile", "tile e1", e1);
                }
            }
        }
        return drawable;
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

    public int calculateScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        return size.x;
    }
}
