package it.playfellas.superapp.ui.master.survey;

import lombok.Data;

@Data
public abstract class Question {
    private String ID;
    private String longName;

    public Question(String ID, String longName) {
        this.ID = ID;
        this.longName = longName;
    }

    public abstract int getViewType();
}
