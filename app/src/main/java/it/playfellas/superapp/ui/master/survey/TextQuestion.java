package it.playfellas.superapp.ui.master.survey;

public class TextQuestion extends Question {

    public TextQuestion(String ID, String longName) {
        super(ID, longName);
    }

    @Override
    public int getViewType() {
        return SurveyAdapter.VIEW_TYPE_TEXT_CARD;
    }
}
