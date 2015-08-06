package it.playfellas.superapp.ui.master.game1;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.master.GameFragment;
import it.playfellas.superapp.logic.Config1;

public class Game1Fragment extends GameFragment {
    public static final String TAG = Game1Fragment.class.getSimpleName();

    private static Game1FragmentPresenter presenter;

    public static final String CONFIG1_ARG = "config1";

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @param config1 The config object
     * @return This Fragment instance.
     */
    public static Game1Fragment newInstance(Config1 config1) {
        Game1Fragment fragment = new Game1Fragment();

        Bundle args = new Bundle();
        args.putSerializable(CONFIG1_ARG, config1);
        fragment.setArguments(args);

        return fragment;
    }

    public Game1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game1_fragment, container, false);

        //call this before the presenter.onTakeView
        super.photoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.master_central_img_example);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //Create the presenter
        if (presenter == null) {
            presenter = new Game1FragmentPresenter();
        }
        presenter.onTakeView(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Method to update the current stage's score. This is not the global score.
     * @param currentStageScore The total score.
     */
    public void setCurrentStageScore(int currentStageScore) {
        super.scoreTextView.setText(currentStageScore + "");
    }

    /**
     * Method to update the global score, non only of the current stage, but it's the sum of all stages scores.
     * @param currentStageScore The score of the current stage.
     * @param maxScorePerStage The max score that you must obtain to complete the current stage.
     * @param currentStageNum The current stage number (0 to maxNumStages - 1).
     */
    public void setGlobalScore(int currentStageScore, int maxScorePerStage, int currentStageNum) {
        int globalScore = (maxScorePerStage * (currentStageNum + 1)) + currentStageScore;
        super.scoreTextView.setText(globalScore + "");
    }
}
