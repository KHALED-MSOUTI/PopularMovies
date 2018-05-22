package software.msouti.popularmovies.Model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static software.msouti.popularmovies.Model.AppContract.AppEatery.TableName;


public class myContentProvider extends ContentProvider {
    public static final int FAVORITE =100;
    public static final int FAVORITE_WITH_ID=101;

    public static final UriMatcher sUriMatcher= BuildUriMatcher();
    public static UriMatcher BuildUriMatcher(){

        UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AppContract.AUTHORITY,AppContract.PATH_TASKS,FAVORITE);
        uriMatcher.addURI(AppContract.AUTHORITY, AppContract.PATH_TASKS+"/#",FAVORITE_WITH_ID);

        return uriMatcher;
    }

    myDbHelper helper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        helper= new myDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db= helper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case FAVORITE:
                retCursor= db.query(TableName,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new android.database.SQLException("Wrong URI >>" + uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITE:
                return AppContract.AppEatery.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db=helper.getWritableDatabase();

        int match=sUriMatcher.match(uri);

        Uri returnUri;
        switch (match){
            case FAVORITE:
                long id=db.insert(TableName,null,values);
                if(id>0){
                    //Success
                    returnUri= ContentUris.withAppendedId(AppContract.AppEatery.CONTENT_URI,id);
                }else{
                    //print error msg
                    throw new android.database.SQLException("Wrong URI >>" + uri);
                }
                break;
            default:
                    throw new android.database.SQLException("Wrong URI >>" + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;
        switch (match) {
            case FAVORITE:
                rowDeleted = db.delete(TableName, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri need to be deleted: " + uri);
        }
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        // Student: Use the uriMatcher to match the  and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.

        // Student: return the actual rows deleted
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        int i = db.update(TableName,values,selection,null);
        return i;
    }
}
