package it.playfellas.superapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano Cappa on 05/08/15.
 */
public class BitmapUtils {

    public static Bitmap copy(Bitmap sourceBitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);
        newBitmap.recycle();
        return mutableBitmap;
    }

    public static Bitmap copyNoRecycle(Bitmap sourceBitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);
        return mutableBitmap;
    }

    public static void recycleBitmap(Bitmap source) {
        if (source == null) {
            return;
        }
        source.recycle();
    }

    public static void recycleBitmapList(Bitmap[] source) {
        if (source == null || source.length <= 0) {
            return;
        }
        for (Bitmap b : source) {
            BitmapUtils.recycleBitmap(b);
        }
    }

    public static void recycleBitmapList(List<Bitmap> source) {
        if (source == null || source.size() <= 0) {
            return;
        }
        for (Bitmap b : source) {
            BitmapUtils.recycleBitmap(b);
        }
    }

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
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
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
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
    }

    public static Bitmap scaleBitmap(Bitmap source, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public static Bitmap scaleBitmapByFactor(Bitmap source, float factor) {
        int newWidth = (int) (source.getWidth() * factor);
        int newHeight = (int) (source.getHeight() * factor);
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    /**
     * Method to scale {@code sourceBitmap}, maintaining the same original size of the bitmap,
     * but with a transparent frame and the scaled and centered {@code sourceBitmap} inside.
     *
     * @return
     */
    public static Bitmap scaleInsideWithFrame(Bitmap sourceBitmap, float factor, int color) {
        Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap clearBitmap = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        clearBitmap.eraseColor(color);

        Bitmap resizedInsideBitmap = scaleBitmapByFactor(mutableBitmap, factor);

        int frameWidth = clearBitmap.getWidth();
        int frameHeight = clearBitmap.getHeight();
        int imageWidth = resizedInsideBitmap.getWidth();
        int imageHeight = resizedInsideBitmap.getHeight();

        Canvas canvas = new Canvas(clearBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(resizedInsideBitmap, (frameWidth - imageWidth) / 2, (frameHeight - imageHeight) / 2, paint);
        return clearBitmap;
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

//    /**
//     * TODO NOT WORKING WITH BITMAP BUT ONLY WITH DRAWABLE
//     * For more info about the PorterDuffMode: http://ssp.impulsetrain.com/porterduff/colordodge-table.png
//     *
//     * @param sourceBitmap
//     * @param color
//     * @return
//     */
//    public static Bitmap getBitmapSilhouetteWithColor(Bitmap sourceBitmap, int color) {
//        Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
//        Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(mutableBitmap);
//        Paint paint = new Paint();
//        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.DST_ATOP);
//        paint.setColorFilter(filter);
//        canvas.drawBitmap(mutableBitmap, 0, 0, paint);
//        return mutableBitmap;
//    }

    /**
     * TODO doc NOT WORKING
     * For more info about the PorterDuffMode: http://ssp.impulsetrain.com/porterduff/colordodge-table.png
     *
     * @param sourceBitmap
     * @param color
     * @return
     */
    public static Bitmap getBitmapSilhouetteWithColor(Drawable sourceBitmap, int color) {
        sourceBitmap.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        Bitmap result = BitmapUtils.drawableToBitmap(sourceBitmap);
        return result;
    }

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

    /**
     * Method to split an image in {@code numStages} pieces.
     *
     * @param bmpOriginal The original Bitmap.
     * @param numStages   int that represents the number of pieces.
     * @return A List of Bitmap, i.e. a List of pieces of {@code bmpOriginal}
     */
    public static List<Bitmap> splitImage(Bitmap bmpOriginal, int numStages) {
        List<Bitmap> pieces = new ArrayList<>();
        int width = bmpOriginal.getWidth() / numStages;
        int start = 0;
        for (int i = 0; i < numStages; i++) {
            Bitmap pieceBitmap = Bitmap.createBitmap(bmpOriginal, start, 0, width - 1, bmpOriginal.getHeight() - 1);
            pieces.add(pieceBitmap);
            start = (bmpOriginal.getWidth() / numStages) * (i + 1);
        }
        return pieces;
    }

    /**
     * Method to combine images side by side.
     *
     * @param leftBmp  The left Bitmap.
     * @param rightBmp The right Bitmap.
     * @return A Bitmap with left and right bitmap are glued side by side.
     */
    public static Bitmap combineImagesSideBySide(Bitmap leftBmp, Bitmap rightBmp) {
        int width;
        int height = leftBmp.getHeight();

        if (leftBmp.getWidth() > rightBmp.getWidth()) {
            width = leftBmp.getWidth() + rightBmp.getWidth();
        } else {
            width = rightBmp.getWidth() + rightBmp.getWidth();
        }

        Bitmap cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(leftBmp, 0f, 0f, null);
        comboImage.drawBitmap(rightBmp, leftBmp.getWidth(), 0f, null);

        return cs;
    }

    /**
     * Method to get a single Bitmap combining multiple pieces side by side.
     * Pieces are combined from left to right iterating over {@code bitmapListCopy}.
     *
     * @param bitmapListCopy The List of Bitmaps' pieces.
     * @param numStages      the maximum number of stages
     * @return The file Bitmap with all pieces combined.
     */
    public static Bitmap getCombinedBitmapByPieces(List<Bitmap> bitmapListCopy, int numStages) {
        Bitmap finalBitmap = bitmapListCopy.get(0);

        for (int i = 0; i < numStages; i++) {
            if (i > 0) { //skip first cycle
                finalBitmap = combineImagesSideBySide(finalBitmap, bitmapListCopy.get(i));
            }
        }
        return finalBitmap;
    }

    /**
     * TODO doc
     *
     * @param b
     * @return
     */
    public static byte[] toByteArray(Bitmap b) {
        //calculate how many bytes our image consists of.
        int bytes = byteSizeOf(b);
        //or we can calculate bytes this way.
        //Use a different value than 4 if you don't use 32bit images.
        //int bytes = b.getWidth()*b.getHeight()*4;

        //Create a new buffer
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        //Move the byte data to the buffer
        b.copyPixelsToBuffer(buffer);

        return buffer.array();
    }

    /**
     * TODO doc
     *
     * @param b
     * @return
     */
    public static Bitmap fromByteArraytoBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * TODO doc
     *
     * @param data
     * @return
     */
    private static int byteSizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount();
        } else {
            return data.getAllocationByteCount();
        }
    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
