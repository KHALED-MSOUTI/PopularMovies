package software.msouti.popularmovies.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasksDb.db";
    private static final int VERSION = 5;
    public myDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
// create table name (column1 type , column2 type );
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + AppContract.AppEatery.TableName + " (" +
                AppContract.AppEatery._ID                + " INTEGER PRIMARY KEY, " +
                AppContract.AppEatery.ColumnMovieID + " INTEGER  NOT NULL, " +
                AppContract.AppEatery.ColumnMovieName    + " TEXT NOT NULL, " +
                AppContract.AppEatery.ColumnMovieOverView +" TEXT , "+
                AppContract.AppEatery.ColumnMoviePosterPath+" TEXT , "+
                AppContract.AppEatery.ColumnMovieRelaseDate+" TEXT , "+
                AppContract.AppEatery.ColumnMovieVoteAverge+" TEXT ," +
                AppContract.AppEatery.ColumnFav+" BOOLEAN "+");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppContract.AppEatery.TableName);
        onCreate(db);
    }
}
