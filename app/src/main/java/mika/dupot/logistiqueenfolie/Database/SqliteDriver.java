package mika.dupot.logistiqueenfolie.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqliteDriver extends SQLiteOpenHelper {

    private static final String DATABASE = "scores.db";
    private static final int VERSION = 2;


    public SqliteDriver(Context context) {
        super(context, DATABASE, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE HighScores " +
                "(" +
                "id integer primary key autoincrement, " +
                "score integer,"+
                "player varchar"+
                "); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion < 3){
            db.execSQL("ALTER TABLE HighScores\n" +
                    "  ADD tempsDeJeu INTEGER " );
            onCreate(db);
        }
        if(oldVersion < 4){
            db.execSQL("ALTER TABLE HighScores\n" +
                    "  ADD autreChamp INTEGER " );
            onCreate(db);
        }
    }
}
