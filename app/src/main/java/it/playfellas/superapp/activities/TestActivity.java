package it.playfellas.superapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.otto.Subscribe;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.tiles.DummyTile;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.presenters.Conveyor;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {

  @Bind(R.id.conveyorLayout) RelativeLayout conveyorLayout;
  @Bind(R.id.conveyorLayout2) RelativeLayout conveyorLayout2;

  private Conveyor conveyor;
  private Conveyor conveyor2;
  private Random rnd = new Random();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    ButterKnife.bind(this);
    TenBus.get().register(this);

    conveyor = new Conveyor(conveyorLayout, 6.5f, Conveyor.LEFT);
    conveyor2 = new Conveyor(conveyorLayout2, 6.5f, Conveyor.RIGHT);
    conveyor.start();
    conveyor2.start();
    (new Timer()).schedule(new TimerTask() {
      @Override public void run() {
        TenBus.get().post(new NewTileEvent(new DummyTile((rnd.nextBoolean()) ? "correct" : "wrong")));
      }
    }, 1500, 1500);

  }

  @Subscribe public void onNewTile(NewTileEvent event){
    conveyor.addTile(event.getTile());
    conveyor2.addTile(event.getTile());
  }

}
