package it.playfellas.superapp.logic.master;

import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by affo on 13/10/15.
 * <p/>
 * Supposing to a have:
 * - Firebase fbref
 * - String gameID
 * - Class<? extends MasterController> masterClass
 * Here is a usage example:
 * <p/>
 * // creating a new GameSurvey s...
 * GameSurvey s = new GameSurvey(fbRef, gameID, masterClass);
 * <p/>
 * // setting educator name and additional notes...
 * s.setEducatorName("Linus Torvalds");
 * s.setAdditionalNotes("... and that's why I started implementing git");
 * <p/>
 * // setting answers using enum `Answers` in `GameSurvey`.
 * s.answer("answer1", "answer number one", "42", GameSurvey.Answers.GT);
 * s.answer("answer2", "answer number two", "Difficult", GameSurvey.Answers.LT);
 * s.answer("answer3", "answer number three", "Foo", GameSurvey.Answers.EQ);
 * <p/>
 * // pushing to Firebase
 * s.save();
 */
public class GameSurvey {
    public static final String FB_ROOT = "surveys";
    private DateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS", Locale.ITALY);

    private String gameMod;
    private Map<String, Answer> answers;
    @Setter
    private String educatorName;
    @Setter
    private String additionalNotes;

    private Firebase fbRef;

    public enum Answers {
        LT("<"),
        GT(">"),
        EQ("==");

        private String symbol;

        Answers(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return this.symbol;
        }
    }

    public GameSurvey(Firebase fbRef, String gameID, Class<? extends MasterController> masterClass) {
        this.fbRef = fbRef.child(FB_ROOT).child(gameID);
        this.gameMod = GameModTranslator.translate(masterClass);
        this.answers = new HashMap<>();
    }

    public void answer(String ID, String longName, String itWas, Answers answer) {
        this.answers.put(ID, new Answer(longName, itWas, answer));
    }

    public void save() {
        if (answers.isEmpty() && additionalNotes == null) {
            return;
        }

        Data data = new Data();
        data.setGameMod(this.gameMod);
        data.setEducator(this.educatorName);
        data.setAnswers(this.answers);
        data.setNotes(this.additionalNotes);
        data.setTs(dateFmt.format(new Date()));
        fbRef.setValue(data);
    }

    private class Answer {
        @Getter
        private String description;
        @Getter
        private String itWas;
        @Getter
        private String answer;

        public Answer() {
            this.description = "";
            this.itWas = "";
            this.answer = Answers.EQ.toString();
        }

        public Answer(String description, String itWas, Answers answer) {
            this.description = description;
            this.itWas = itWas;
            this.answer = answer.toString();
        }
    }

    private class Data {
        @Getter
        @Setter
        private String gameMod;
        @Getter
        @Setter
        private Map<String, Answer> answers;
        @Getter
        @Setter
        private String educator;
        @Getter
        @Setter
        private String notes;
        @Setter
        @Getter
        private String ts;
    }
}
