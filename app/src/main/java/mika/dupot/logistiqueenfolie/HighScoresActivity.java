package mika.dupot.logistiqueenfolie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.util.ArrayList;

import mika.dupot.logistiqueenfolie.Database.HighScore;
import mika.dupot.logistiqueenfolie.Database.HighScoreRepository;

import mika.dupot.logistiqueenfolie.Adapter.HighScoresAdapter;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_highscores);

        HighScoreRepository highScoreRepository=new HighScoreRepository(this);
        highScoreRepository.open();

        ArrayList<HighScore> highScoreList=highScoreRepository.findAll();

        highScoreRepository.close();

        HighScoresAdapter adapter=new HighScoresAdapter( highScoreList);

        RecyclerView recyclerView =findViewById(R.id.recycleViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void gotoMain(View view) {

        Intent oIntent = new Intent(this, MainActivity.class);
        startActivity(oIntent);

    }
}
