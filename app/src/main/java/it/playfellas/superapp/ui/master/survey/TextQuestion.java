package it.playfellas.superapp.ui.master.survey;

import android.content.Context;
import android.text.Spanned;
import android.text.SpannedString;

import it.playfellas.superapp.R;

public class TextQuestion extends Question {

    public TextQuestion(String ID, String longName) {
        super(ID, longName);
    }

    @Override
    public Spanned getQuestionText() {
        return new SpannedString(getLongName());
    }

    @Override
    public Spanned getActualValueText(Context context) {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.master_survey_question_text_cardview;
    }
}
