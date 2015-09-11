package it.playfellas.superapp.ui.master;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.BitmapUtils;

public class GameFragment extends Fragment implements
        MasterTimerDialogFragment.DialogTimerListener {

    private static final String TAG = GameFragment.class.getSimpleName();

    protected GamePresenter presenter;
    protected List<ImageView> photoimageViews = new ArrayList<>();
    protected List<Bitmap> photoBitmaps = new ArrayList<>();

    @Bind(R.id.showMasterInfos)
    Button showMasterInfosButton;
    @Bind(R.id.masterInfosRelativeLayout)
    RelativeLayout masterInfosRelativeLayout;
    @Bind(R.id.currentScoreOverTotalTextView)
    TextView currentScoreOverTotalTextView;
    @Bind(R.id.currentStageOverTotalTextView)
    TextView currentStageOverTotalTextView;
    @Bind(R.id.gameIdInDb)
    TextView gameIdInDb;

    //Photos taken on slave devices.
    @Bind(R.id.photo1ImageView)
    protected CircleImageView photo1ImageView;
    @Bind(R.id.photo2ImageView)
    protected CircleImageView photo2ImageView;
    @Bind(R.id.photo3ImageView)
    protected CircleImageView photo3ImageView;
    @Bind(R.id.photo4ImageView)
    protected CircleImageView photo4ImageView;

    //The central image, that represent the progress (in number of completed stages) of the current game.
    @Bind(R.id.central_img)
    ImageView centralImageView;

    private List<Bitmap> piecesList;
    protected Bitmap photoBitmap;

    @Override
    public void onCountdownFinished() {
        presenter.beginNextStage();
    }

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
    protected void updateStageImage(int currentStage, int numStages) {
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
    protected void setCurrentScoreOverTotal(int currentStageScore, int maxScore) {
        this.currentScoreOverTotalTextView.setText("Punteggio: " + currentStageScore + " / " + maxScore);
    }

    protected void setCurrentStageOverTotal(int currentStageNumber, int maxNumStages) {
        this.currentStageOverTotalTextView.setText("Manche: " + currentStageNumber + " / " + maxNumStages);
    }

    protected void setMasterGameId(String gameId) {
        this.gameIdInDb.setText("ID partita: " + gameId);
    }


    public void showDialogToProceed() {
        //show a dialog with this title and string, but this isn't a dialog to ask confirmation
        showDialogFragment("Stage completato", "Pronto per lo stage successivo?",
                InternalConfig.MASTER_DIAG_TAG, InternalConfig.MASTER_DIAG_ID);
    }

    private void showDialogFragment(String title, String message, String tag, int id) {
        MasterTimerDialogFragment masterTimerDialogFragment = (MasterTimerDialogFragment) getFragmentManager().findFragmentByTag(tag);
        if (masterTimerDialogFragment == null) {
            masterTimerDialogFragment = MasterTimerDialogFragment.newInstance(title);
            masterTimerDialogFragment.setTargetFragment(this, id);
            masterTimerDialogFragment.show(getFragmentManager(), tag);
            getFragmentManager().executePendingTransactions();
        }
    }

    protected void initPhotos() {
        this.photoimageViews.add(this.photo1ImageView);
        this.photoimageViews.add(this.photo2ImageView);
        this.photoimageViews.add(this.photo3ImageView);
        this.photoimageViews.add(this.photo4ImageView);
    }

    protected void updateImageViews() {
        for (int i = 0; i < photoBitmaps.size(); i++) {
            if (this.photoimageViews.get(i) == null || photoBitmaps.get(i) == null) {
                Log.e(TAG, "ImageView.get(i) or playerPhoto.get(i) are null");
                continue;
            }
            this.photoimageViews.get(i).setImageBitmap(photoBitmaps.get(i));
        }
    }

    public void updatePhotos(byte[] photoByteArray) {
        photoBitmaps.add(BitmapUtils.scaleBitmap(BitmapUtils.fromByteArraytoBitmap(photoByteArray),
                BitmapUtils.dpToPx(100), BitmapUtils.dpToPx(100)));
        this.updateImageViews();
    }




    @OnClick(R.id.showMasterInfos)
    public void onMasterInfosButtonClicked(View v) {
        if (masterInfosRelativeLayout.getVisibility() == View.GONE) {
            masterInfosRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            masterInfosRelativeLayout.setVisibility(View.GONE);
        }
    }
}
