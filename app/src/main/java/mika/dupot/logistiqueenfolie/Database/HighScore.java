package mika.dupot.logistiqueenfolie.Database;

public class HighScore {


    private long id;
    private long score;
    private String player;

    public long getId(){
        return id;
    }
    public long getScore(){
        return score;
    }
    public String getPlayer(){
        return player;
    }

    public void setScore(long score_){
        score=score_;
    }
    public void setPlayer(String player_){
        player=player_;
    }
}
