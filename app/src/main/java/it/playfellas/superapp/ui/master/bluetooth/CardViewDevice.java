package it.playfellas.superapp.ui.master.bluetooth;

import android.support.v7.widget.CardView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Stefano Cappa on 12/09/15.
 */
class CardViewDevice {
    private CardView cardView;
    private TextView address;
    private TextView name;
    private TextView countdown;
    private ProgressBar progressBar;

    public CardViewDevice(CardView cardView, TextView address, TextView name, TextView countdown, ProgressBar progressBar) {
        this.cardView = cardView;
        this.address = address;
        this.name = name;
        this.countdown = countdown;
        this.progressBar = progressBar;
    }

    public void update(int isCardVisible, String address, String name, int countdown, int isProgressVisible) {

        this.cardView.setVisibility(isCardVisible);
        YoYo.with(Techniques.DropOut).duration(1500).playOn(this.cardView);

        this.address.setText(address);
        this.name.setText(name);
        if (countdown != 0) {
            this.countdown.setText(countdown);
        } else {
            this.countdown.setText("");
        }
        this.progressBar.setVisibility(isProgressVisible);
        YoYo.with(Techniques.FadeOut).duration(1500).playOn( this.progressBar);
    }
}