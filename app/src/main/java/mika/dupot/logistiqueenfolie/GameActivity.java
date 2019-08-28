package mika.dupot.logistiqueenfolie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mika.dupot.logistiqueenfolie.Display.Game;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);


        Game.instance = new Game(this);


        setContentView(Game.instance);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Game.instance=null;
    }



}
