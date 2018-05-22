package software.msouti.popularmovies.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class AppContract {


    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "software.msouti.popularmovies";

    // The base content URI = "content://" + <authority>
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "favorites";


    public static class AppEatery implements BaseColumns{

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TableName = "favorites";
        public static final String ColumnMovieID="movieID";
        public static final String ColumnMovieName= "name";
        public static final String ColumnMovieVoteAverge= "rate";
        public static final String ColumnMoviePosterPath="path";
        public static final String ColumnMovieRelaseDate="date";
        public static final String ColumnMovieOverView="overview";
        public static final String ColumnFav="isFav";






        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TASKS;
    }


}
