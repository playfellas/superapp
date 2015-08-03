package it.playfellas.superapp.events;

import android.graphics.Bitmap;

import lombok.Getter;

/**
 * Created by Stefano Cappa on 03/08/15.
 */
public class PhotoEvent extends NetEvent {
    @Getter
    private Bitmap photo;

    public PhotoEvent(Bitmap photo) {
        this.photo = photo;
    }
}
