package software.msouti.popularmovies.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasksDb.db";
    private static final int VERSION = 4;
    public myDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
// create table name (column1 type , column2 type );
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + AppContract.AppEntery.TableName + " (" +
                AppContract.AppEntery._ID                + " INTEGER PRIMARY KEY, " +
                AppContract.AppEntery.ColumnMovieID + " INTEGER  NOT NULL, " +
                AppContract.AppEntery.ColumnMovieName    + " TEXT NOT NULL, " +
                AppContract.AppEntery.ColumnMovieOverView +" TEXT , "+
                AppContract.AppEntery.ColumnMoviePosterPath+" TEXT , "+
                AppContract.AppEntery.ColumnMovieRelaseDate+" TEXT , "+
                AppContract.AppEntery.ColumnMovieVoteAverge+" TEXT ," +
                AppContract.AppEntery.ColumnFav+" BOOLEAN "+");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppContract.AppEntery.TableName);
        onCreate(db);
    }
}
