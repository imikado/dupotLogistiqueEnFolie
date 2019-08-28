package mika.dupot.logistiqueenfolie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mika.dupot.logistiqueenfolie.Domain.GamePlay;

public class GameoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gameover);


        TextView scoreInput = findViewById(R.id.score);
        scoreInput.setText( "Score: "+String.format("%05d",GamePlay.getInstance().getScore()));
    }


    public void startGame(View view) {

        GamePlay.resetInstance();

        Intent oIntent = new Intent(this, GameActivity.class);
        startActivity(oIntent);


    }
}


