package it.playfellas.superapp.activities.master;

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

    @Bind(R.id.photo1ImageView)
    public ImageView photo1ImageView;
    @Bind(R.id.photo2ImageView)
    public ImageView photo2ImageView;
    @Bind(R.id.photo3ImageView)
    public ImageView photo3ImageView;
    @Bind(R.id.photo4ImageView)
    public ImageView photo4ImageView;

    @Bind(R.id.central_img)
    public ImageView centralImageView;

    protected Bitmap photoBitmap;

    private List<Bitmap> pieces;

    private int stage = 0;

    public void nextStage() {
        //TODO improve this, without to re-split again
        List<Bitmap> bitmapList = splitImage(photoBitmap,4);

        Log.d("GameFragment" , stage + "");

        for (int i = 0; i < 4; i++) {
            if (i <= stage) {
                bitmapList.set(i, bitmapList.get(i));
            } else {
                bitmapList.set(i, toGrayscale(bitmapList.get(i)));
            }
        }

        Bitmap finalBitmap = bitmapList.get(0);

        for (int i = 0; i < 4; i++) {
            if (i > 0) { //skip first cycle
                finalBitmap = combineImages(finalBitmap, bitmapList.get(i));
            }
        }

        stage++;
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

    @OnClick(R.id.central_img)
    public void onClickCentral(View view) {
        this.nextStage();
    }


    public void initiCentralImage() {
        Bitmap gray = toGrayscale(photoBitmap);
        centralImageView.setImageBitmap(gray);
        pieces = splitImage(photoBitmap, 4);
    }

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

    public Bitmap combineImages(Bitmap c, Bitmap s) {
        Bitmap cs = null;

        int width, height = 0;

        if (c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);

        return cs;
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
