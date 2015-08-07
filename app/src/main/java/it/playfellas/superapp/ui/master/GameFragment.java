package it.playfellas.superapp.ui.master;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import it.playfellas.superapp.R;

public class GameFragment extends Fragment {

    private static final String TAG = GameFragment.class.getSimpleName();

    @Bind(R.id.scoreTextView)
    public TextView scoreTextView;
    @Bind(R.id.globalScoreTextView)
    public TextView globalScoreTextView;

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
     * Method to init central image, creating a grayscale version of {@code photoBitmap}.
     *
     * @param numStages the maximum number of stages used to split the original bitmap.
     */
    public void initCentralImage(int numStages) {
        //split the original bitmap and store its pieces in a List
        piecesList = BitmapUtils.splitImage(photoBitmap, numStages);
        //create a gray scale version of the original bitmap
        Bitmap gray = BitmapUtils.toGrayscale(photoBitmap);
        //update the gui with the gray scale version
        centralImageView.setImageBitmap(gray);
    }

    /**
     * Method to update the central image coloring {@code currentStages} pieces,
     * and leaving {@code numStages-currentStages} pieces in gray scale.
     *
     * @param currentStage starts from 0 to numStages-1
     * @param numStages    the maximum number of stages
     */
    public void updateStageImage(int currentStage, int numStages) {
        if (currentStage > numStages) {
            return;
        }

        Log.d("GameFragment", "currentStage: " + currentStage + " , maxStages: " + numStages);

        //Copy the arrayList of the photoBitmap's pieces
        List<Bitmap> bitmapListCopy = new ArrayList<>(piecesList);

        //update the pieces by the value of currentStages
        for (int i = 0; i < numStages; i++) {
            if (i <= currentStage) {
                bitmapListCopy.set(i, bitmapListCopy.get(i));
            } else {
                bitmapListCopy.set(i, BitmapUtils.toGrayscale(bitmapListCopy.get(i)));
            }
        }

        //get the combined image
        Bitmap finalBitmap = BitmapUtils.getCombinedBitmapByPieces(bitmapListCopy, numStages);

        //set the combined image in the gui
        centralImageView.setImageBitmap(finalBitmap);
    }

    /**
     * Method to update the current stage's score. This is not the global score.
     *
     * @param currentStageScore The total score.
     */
    public void setCurrentStageScore(int currentStageScore) {
        this.scoreTextView.setText(currentStageScore + "");
    }

    /**
     * Method to update the global score, non only of the current stage, but it's the sum of all stages scores.
     *
     * @param currentStageScore The score of the current stage.
     * @param maxScorePerStage  The max score that you must obtain to complete the current stage.
     * @param currentStageNum   The current stage number (0 to maxNumStages - 1).
     */
    public void setGlobalScore(int currentStageScore, int maxScorePerStage, int currentStageNum) {
        int globalScore = (maxScorePerStage * currentStageNum) + currentStageScore;
        Log.d(TAG, "globalscore: " + globalScore);
        this.globalScoreTextView.setText(globalScore + "");
    }
}
