package it.playfellas.superapp.logic;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by affo on 30/07/15.
 */
public abstract class Config {
    private static final int decreaseSteps = 5;
    @Getter
    private static final int rttUpdatePeriod = 10;
    private static final float baseMaxRtt = 5;
    private static final float baseMinRtt = 2;

    @Getter
    @Setter
    private int difficultyLevel;
    @Getter
    @Setter
    private int tileDensity;
    @Getter
    @Setter
    private int maxScore;
    @Getter
    @Setter
    private int noStages;
    @Getter
    @Setter
    private int ruleChange;
    @Getter
    @Setter
    private boolean speedUp;

    public float getDefaultRtt() {
        return baseMaxRtt - (float) difficultyLevel / 2;
    }

    public float getMinRtt() {
        return baseMinRtt - (float) difficultyLevel / 4;
    }

    public float getRttDecreaseDelta() {
        return Math.abs(getDefaultRtt() - getMinRtt()) / decreaseSteps;
    }
}
