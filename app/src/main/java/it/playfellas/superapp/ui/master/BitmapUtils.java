package it.playfellas.superapp.ui.master;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Created by Stefano Cappa on 05/08/15.
 */
public class BitmapUtils {


    /**
     * Method to remove color in a Bitmap, creating a gray scale image.
     *
     * @param bmpOriginal The original Bitmap.
     * @return The gray scale Bitmap.
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpGrayscale;
    }

    /**
     * TODO doc
     *
     * @param source
     * @param angle
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * TODO doc
     *
     * @param source
     * @return
     */
    public static Bitmap flipVerticallyBitmap(Bitmap source) {
        Matrix m = new Matrix();
        m.preScale(1, -1);
        Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
        return result;
    }

    /**
     * TODO doc
     *
     * @param source
     * @return
     */
    public static Bitmap flipHorizonallyBitmap(Bitmap source) {
        Matrix m = new Matrix();
        m.setScale(-1, 1);
        Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
        return result;
    }

    public static Bitmap scaleBitmap(Bitmap source, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public static Bitmap scaleBitmapByFactor(Bitmap source, int factor) {
        int newWidth = source.getWidth() * factor;
        int newHeight = source.getHeight() * factor;
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    /**
     * TODO write documentation
     *
     * @param sourceBitmap
     * @param color
     * @return
     */
    public static Bitmap overlayColor(Bitmap sourceBitmap, int color) {
        Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        paint.setColorFilter(filter);
        canvas.drawBitmap(mutableBitmap, 0, 0, paint);

        return mutableBitmap;
    }

    /**
     * TODO NOT WORKING WITH BITMAP BUT ONLY WITH DRAWABLE
     * For more info about the PorterDuffMode: http://ssp.impulsetrain.com/porterduff/colordodge-table.png
     *
     * @param sourceBitmap
     * @param color
     * @return
     */
    public static Bitmap getBitmapSilhouetteWithColor(Bitmap sourceBitmap, int color) {
        Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.DST_ATOP);
        paint.setColorFilter(filter);
        canvas.drawBitmap(mutableBitmap, 0, 0, paint);

        return mutableBitmap;
    }

    /**
     * TODO doc
     * For more info about the PorterDuffMode: http://ssp.impulsetrain.com/porterduff/colordodge-table.png
     *
     * @param sourceBitmap
     * @param color
     * @return
     */
    public static Drawable getDrawableSilhouetteWithColor(Drawable sourceBitmap, int color) {
        sourceBitmap.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return sourceBitmap;
    }

    /**
     * TODO doc
     *
     * @param original
     * @param color
     * @return
     */
    public static Bitmap overlayColorOnGrayScale(Bitmap original, int color) {
        Bitmap result = toGrayscale(original);
        return overlayColor(result, color);
    }

}
