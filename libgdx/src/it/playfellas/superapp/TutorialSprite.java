package it.playfellas.superapp;

import com.badlogic.gdx.graphics.g2d.Batch;

public class TutorialSprite extends SimpleSprite {

    private SimpleSprite bgSprite;
    private SimpleSprite tileSprite;

    public TutorialSprite(SimpleSprite bgSprite, SimpleSprite tileSprite) {
        this.bgSprite = bgSprite;
        this.tileSprite = tileSprite;
    }

    @Override
    public void incrementX(float increment) {
        bgSprite.incrementX(increment);
        tileSprite.incrementX(increment);
    }

    @Override
    public float getWidth() {
        return bgSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return bgSprite.getHeight();
    }

    @Override
    public void setPosition(float x, float y) {
        bgSprite.setPosition(x, y);
        float tileX = x + ((bgSprite.getWidth() - tileSprite.getWidth()) / 2);
        float tileY = y + ((bgSprite.getHeight() - tileSprite.getHeight()) / 2);
        tileSprite.setPosition(tileX, tileY);
    }

    @Override
    public void draw(Batch batch) {
        tileSprite.draw(batch);
        bgSprite.draw(batch);
    }
}
