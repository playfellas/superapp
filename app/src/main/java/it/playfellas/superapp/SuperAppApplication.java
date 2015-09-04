package it.playfellas.superapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by affo on 04/09/15.
 */
public class SuperAppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
