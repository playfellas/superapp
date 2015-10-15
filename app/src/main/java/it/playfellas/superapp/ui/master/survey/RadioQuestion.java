package it.playfellas.superapp.ui.master.survey;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import it.playfellas.superapp.R;
import lombok.Getter;
import lombok.Setter;

public class RadioQuestion extends Question {
    @Getter
    @Setter
    private String itWas;

    public RadioQuestion(String ID, String longName, String itWas) {
        super(ID, longName);
        this.itWas = itWas;
    }

    @Override
    public Spanned getQuestionText() {
        return Html.fromHtml("Vorresti che <b>" + getLongName() + "</b> fosse:");
    }

    @Override
    public Spanned getActualValueText(Context context) {
        String actualValue;

        if (getID().equals(SurveyActivity.DIFFICULTY_LEVEL_ID)) {
            actualValue = context.getResources().getStringArray(R.array.difficulty_string_array)[Integer.valueOf(itWas)];
        } else {
            actualValue = itWas;
        }
        return Html.fromHtml("Valore attuale: <b>" + actualValue);
    }
}
