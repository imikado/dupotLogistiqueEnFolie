package mika.dupot.logistiqueenfolie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mika.dupot.logistiqueenfolie.Database.HighScore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


    }

    public void startGame(View view) {
        Intent oIntent = new Intent(this, GameActivity.class);
        startActivity(oIntent);
    }

    public void gotoParameters(View view) {
        Intent oIntent = new Intent(this, ParametersActivity.class);
        startActivity(oIntent);
    }

    public void gotoHighScores(View view) {
        Intent oIntent = new Intent(this, HighScoresActivity.class);
        startActivity(oIntent);
    }
}
