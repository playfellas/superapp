package it.playfellas.superapp.activities.master;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public void nextStage() {
        //TODO UPDATE CENTRAL IMAGE COLORING ANOTHER PART
    }

    @OnClick(R.id.central_img)
    public void onClickCentral(View view) {
        this.nextStage();
    }

    public void initiCentralImage() {
        Bitmap gray = toGrayscale(photoBitmap);
        centralImageView.setImageBitmap(gray);
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
