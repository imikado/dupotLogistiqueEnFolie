package mika.dupot.logistiqueenfolie.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HighScoreRepository {

    private final String table="HighScores";
    private final String field_id="id";
    private final String field_score="score";
    private final String field_player="player";

    private SQLiteDatabase database;
    private SqliteDriver dbDriver;

    public HighScoreRepository(Context context_){
        dbDriver=new SqliteDriver(context_);
    }

    public void open() throws SQLException {
        database = dbDriver.getWritableDatabase();
    }

    public void close() {
        dbDriver.close();
    }

    public void insertScore(String player_,long score_){
        ContentValues values = new ContentValues();
        values.put( this.field_player, player_);
        values.put( this.field_score, score_);

        database.insert(this.table,null,values);

        Log.i("HighScoreRepo insert","insert player:"+player_);

    }

    public long findBestScore(){
        Cursor cursor=database.rawQuery("SELECT max(score) FROM "+this.table,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            return cursor.getLong(0);
        }

        return 0;

    }

    public ArrayList<HighScore> findAll(){

        String[] columnList={this.field_score,this.field_player};

        ArrayList<HighScore> highScoresList=new ArrayList<HighScore>();

        Cursor cursor = database.query(this.table,
                columnList, null, null, null, null, "score DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HighScore highScore = getItemByCursor(cursor);
            highScoresList.add(highScore);
            cursor.moveToNext();
        }

        cursor.close();
        return highScoresList;

    }

    private HighScore getItemByCursor(Cursor cursor) {
        HighScore highScore=new HighScore();
        highScore.setScore(cursor.getLong(0));
        highScore.setPlayer(cursor.getString(1));
        return highScore;
    }


}
