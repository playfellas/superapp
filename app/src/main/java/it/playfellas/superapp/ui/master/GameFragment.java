package it.playfellas.superapp.ui.master;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import it.playfellas.superapp.R;

public class GameFragment extends Fragment {

    @Bind(R.id.scoreTextView)
    public TextView scoreTextView;

    //Photos taken on slave devices.
    @Bind(R.id.photo1ImageView)
    public ImageView photo1ImageView;
    @Bind(R.id.photo2ImageView)
    public ImageView photo2ImageView;
    @Bind(R.id.photo3ImageView)
    public ImageView photo3ImageView;
    @Bind(R.id.photo4ImageView)
    public ImageView photo4ImageView;

    //The central image, that represent the progress (in number of completed stages) of the current game.
    @Bind(R.id.central_img)
    public ImageView centralImageView;

    private List<Bitmap> piecesList;
    protected Bitmap photoBitmap;

    /**
     * Method to update the central image coloring {@code currentStages} pieces,
     * and leaving {@code numStages-currentStages} pieces in gray scale.
     *
     * @param currentStage starts from 0 to numStages-1
     * @param numStages    the maximum number of stages
     */
    public void updateStageImage(int currentStage, int numStages) {
        if (currentStage >= numStages) {
            //because it's not allowed.
            return;
        }

        Log.d("GameFragment", currentStage + "");

        //Copy the arrayList of the photoBitmap's pieces
        List<Bitmap> bitmapListCopy = new ArrayList<>(piecesList);

        //update the pieces by the value of currentStages
        for (int i = 0; i < 4; i++) {
            if (i <= currentStage) {
                bitmapListCopy.set(i, bitmapListCopy.get(i));
            } else {
                bitmapListCopy.set(i, toGrayscale(bitmapListCopy.get(i)));
            }
        }

        //get the combined image
        Bitmap finalBitmap = getCombinedBitmapByPieces(bitmapListCopy);

        //set the combined image in the gui
        centralImageView.setImageBitmap(finalBitmap);
    }


    //TODO not used now
    private void changeBitmapColor(Bitmap sourceBitmap, ImageView image, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        image.setImageBitmap(resultBitmap);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
    }


    //TODO remove -> only for testing purposes
    @OnClick(R.id.central_img)
    public void onClickCentral(View view) {
        this.updateStageImage(2, 4);
    }


    /**
     * Method to init central image, creating a grayscale version of {@code photoBitmap}.
     *
     * @param numStages the maximum number of stages used to split the original bitmap.
     */
    public void initCentralImage(int numStages) {
        //split the original bitmap and store its pieces in a List
        piecesList = splitImage(photoBitmap, numStages);
        //create a gray scale version of the original bitmap
        Bitmap gray = toGrayscale(photoBitmap);
        //update the gui with the gray scale version
        centralImageView.setImageBitmap(gray);
    }

    /**
     * Method to split an image in {@code num} pieces.
     *
     * @param bmpOriginal The original Bitmap.
     * @param num         int that represents the number of pieces.
     * @return A List of Bitmap, i.e. a List of pieces of {@code bmpOriginal}
     */
    private List<Bitmap> splitImage(Bitmap bmpOriginal, int num) {
        List<Bitmap> pieces = new ArrayList<>();
        int width = bmpOriginal.getWidth() / num;
        int start = 0;
        for (int i = 0; i < num; i++) {
            Bitmap pieceBitmap = Bitmap.createBitmap(bmpOriginal, start, 0, width - 1, bmpOriginal.getHeight() - 1);
            pieces.add(pieceBitmap);
            start = (bmpOriginal.getWidth() / num) * (i + 1);
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
    private Bitmap combineImagesSideBySide(Bitmap leftBmp, Bitmap rightBmp) {
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
     * Pices are combined from left to right iterating over {@code bitmapListCopy}.
     *
     * @param bitmapListCopy The List of Bitmaps' pieces.
     * @return The file Bitmap with all pieces combined.
     */
    private Bitmap getCombinedBitmapByPieces(List<Bitmap> bitmapListCopy) {
        Bitmap finalBitmap = bitmapListCopy.get(0);

        for (int i = 0; i < 4; i++) {
            if (i > 0) { //skip first cycle
                finalBitmap = combineImagesSideBySide(finalBitmap, bitmapListCopy.get(i));
            }
        }
        return finalBitmap;
    }

    /**
     * Method to remove color in a Bitmap, creating a gray scale image.
     *
     * @param bmpOriginal The original Bitmap.
     * @return The gray scale Bitmap.
     */
    private Bitmap toGrayscale(Bitmap bmpOriginal) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpGrayscale;
    }
}
