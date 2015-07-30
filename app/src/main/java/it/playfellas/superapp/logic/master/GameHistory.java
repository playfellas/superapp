package it.playfellas.superapp.logic.master;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by affo on 29/07/15.
 */
public class GameHistory {
    private Map<String, Integer> right;
    private Map<String, Integer> wrong;
    private Date startDate;

    public GameHistory() {
        right = new HashMap<>();
        wrong = new HashMap<>();
        startDate = new Date();
    }

    public void right(String k){
        int rightCount = 0;
        if(right.containsKey(k)){
            rightCount = right.get(k);
        }
        rightCount++;
        right.put(k, rightCount);
    }

    public void wrong(String k){
        int wrongCount = 0;
        if(wrong.containsKey(k)){
            wrongCount = wrong.get(k);
        }
        wrongCount++;
        wrong.put(k, wrongCount);
    }

    /**
     * @return elapsed time in milliseconds from the instantiation
     * of the object untill the invocation of this method.
     */
    public long getElapsedTime(){
        Date d = new Date();
        return d.getTime() - startDate.getTime();
    }
}
