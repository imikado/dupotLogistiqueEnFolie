package mika.dupot.logistiqueenfolie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import mika.dupot.logistiqueenfolie.Database.HighScoreRepository;
import mika.dupot.logistiqueenfolie.Display.Game;
import mika.dupot.logistiqueenfolie.Domain.GamePlay;

public class GameoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gameover);


        TextView scoreInput = findViewById(R.id.score);
        scoreInput.setText( "Score: "+String.format("%05d",GamePlay.getInstance().getScore()));

        HighScoreRepository highScoreRepository=new HighScoreRepository(this);
        highScoreRepository.open();

        long lastBestScore=highScoreRepository.findBestScore();

        highScoreRepository.close();

        if(GamePlay.getInstance().getScore() > lastBestScore) {



            findViewById(R.id.highScoreTitle).setVisibility(View.VISIBLE);
            findViewById(R.id.highScoreTxt).setVisibility(View.VISIBLE);
            findViewById(R.id.highScoreTxtBtn).setVisibility(View.VISIBLE);


        }

    }


    public void startGame(View view) {

        GamePlay.resetInstance();

        Intent oIntent = new Intent(this, GameActivity.class);
        startActivity(oIntent);


    }

    public void gotoHighScores(View view) {
        Intent oIntent = new Intent(this, HighScoresActivity.class);
        startActivity(oIntent);
    }

    public void gotoMenuu(View view) {
        Intent oIntent = new Intent(this, MainActivity.class);
        startActivity(oIntent);
    }

    public void saveThenGotoHighScores(View view) {

        EditText inputText= (EditText)findViewById(R.id.highScoreTxt);
        String playerName=inputText.getText().toString();

        HighScoreRepository highScoreRepository=new HighScoreRepository(this);
        highScoreRepository.open();
        highScoreRepository.insertScore(playerName, GamePlay.getInstance().getScore());
        highScoreRepository.close();

        Intent oIntent = new Intent(this, HighScoresActivity.class);
        startActivity(oIntent);
    }
}


