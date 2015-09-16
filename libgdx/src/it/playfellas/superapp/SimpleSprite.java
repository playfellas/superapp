package it.playfellas.superapp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SimpleSprite extends Sprite {

    public SimpleSprite() {
    }

    public SimpleSprite(Texture texture) {
        super(texture);
    }

    public void incrementX(float increment) {
        setX(getX() + increment);
    }
}
