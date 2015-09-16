package it.playfellas.superapp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class CompositeBgSprite extends Sprite {

    private Array<Sprite> sprites;

    public CompositeBgSprite() {
        sprites = new Array<Sprite>();
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public Array<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(Array<Sprite> sprites) {
        this.sprites = sprites;
    }

    @Override
    public void draw(Batch batch) {
        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }
    }
}
