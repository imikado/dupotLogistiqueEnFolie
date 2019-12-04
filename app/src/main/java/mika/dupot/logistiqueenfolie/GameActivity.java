package mika.dupot.logistiqueenfolie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import mika.dupot.logistiqueenfolie.Display.Game;


public class GameActivity extends AppCompatActivity {

    final String DISPLAY_PATH_DOT="DISPLAY_PATH_DOT";
    final String LEVEL_DIFFICULTY="LEVEL_DIFFICULTY";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);


        Game.instance = new Game(this);

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Game.instance.enableDisableDisplayPathPoint(prefs.getBoolean(DISPLAY_PATH_DOT,true) );

        Game.instance.setLevelDifficulty(prefs.getInt(LEVEL_DIFFICULTY,2));


        setContentView(Game.instance);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Game.instance=null;
    }



}
