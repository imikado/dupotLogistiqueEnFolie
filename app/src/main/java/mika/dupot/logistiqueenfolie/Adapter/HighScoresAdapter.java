package mika.dupot.logistiqueenfolie.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mika.dupot.logistiqueenfolie.Database.HighScore;
import mika.dupot.logistiqueenfolie.HighScoresActivity;
import mika.dupot.logistiqueenfolie.R;

public class HighScoresAdapter extends RecyclerView.Adapter<HighScoresAdapter.ScoreHolder> {

    private ArrayList<HighScore> highscoreList;

    public HighScoresAdapter(ArrayList<HighScore> highScoreList_){
        highscoreList=highScoreList_;
    }

    @Override
    public int getItemCount() {
        return highscoreList.size();
    }

    @Override
    public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_hightscore, parent, false);
        return new ScoreHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreHolder holder, int position) {
        HighScore highScoreCurrent = highscoreList.get(position);
        holder.display(highScoreCurrent);
    }

    public class ScoreHolder extends RecyclerView.ViewHolder {

        private  TextView player;
        private  TextView score;


        public ScoreHolder(  View itemView) {
            super(itemView);

            player = ((TextView) itemView.findViewById(R.id.player));
            score = ((TextView) itemView.findViewById(R.id.score));


        }

        public void display(HighScore highScoreCurrent) {
            player.setText(highScoreCurrent.getPlayer());
            score.setText( String.format("%05d",highScoreCurrent.getScore()));
        }
    }

}