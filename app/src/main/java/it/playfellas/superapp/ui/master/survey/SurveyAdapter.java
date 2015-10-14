package it.playfellas.superapp.ui.master.survey;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;

public class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_RADIO_CARD = 0;
    public static final int VIEW_TYPE_TEXT_CARD = 1;

    private List<Question> questions;

    public static abstract class QuestionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.questionTitleTextView)
        TextView questionTitleTextView;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void setData(Question q);
    }

    public static class QuestionRadioViewHolder extends QuestionViewHolder {
        @Bind(R.id.questionTextView)
        TextView questionTextView;

        public QuestionRadioViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Question q) {
            questionTitleTextView.setText(((RadioQuestion) q).getQuestionText());
            questionTextView.setText(((RadioQuestion) q).getActualValueText(itemView.getContext()));
        }
    }

    public static class QuestionTextViewHolder extends QuestionViewHolder {
        @Bind(R.id.answerEditText)
        EditText answerEditText;

        public QuestionTextViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Question q) {
            questionTitleTextView.setText(q.getLongName());
            answerEditText.setText(q.getLongName());
        }
    }


    public SurveyAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_RADIO_CARD:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.master_survey_question_radio_cardview, parent, false);
                return new QuestionRadioViewHolder(v);
            case VIEW_TYPE_TEXT_CARD:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.master_survey_question_text_cardview, parent, false);
                return new QuestionTextViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((QuestionViewHolder)holder).setData(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return questions.get(position).getViewType();
    }
}
