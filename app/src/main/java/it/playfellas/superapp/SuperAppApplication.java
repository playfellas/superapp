package it.playfellas.superapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;

import io.fabric.sdk.android.Fabric;
import it.playfellas.superapp.logic.db.DbAccess;
import it.playfellas.superapp.logic.db.DbException;
import it.playfellas.superapp.logic.db.DbFiller;

/**
 * Created by affo on 04/09/15.
 */
public class SuperAppApplication extends Application {
    private static final String TAG = SuperAppApplication.class.getSimpleName();
    private static final String DBFILL_PREF = "dbfill";

    @Override
    public void onCreate() {
        super.onCreate();

        // Twitter Fabric
        Fabric.with(this, new Crashlytics());

        // Firebase DB
        Firebase.setAndroidContext(this);

        // Fill internal DB
        SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        if (!pref.getBoolean(DBFILL_PREF, false)) {
            try {
                DbAccess db = new DbAccess(this);
                (new DbFiller(db)).fill();
                pref.edit().putBoolean(DBFILL_PREF, true).apply();
            } catch (DbException e) {
                Log.e(TAG, "DbException", e);
            }
        }
    }
}
