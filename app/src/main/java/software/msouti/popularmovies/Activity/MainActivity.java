package software.msouti.popularmovies.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.msouti.popularmovies.Adapter.MoviesAdapter;
import software.msouti.popularmovies.Model.AppContract;
import software.msouti.popularmovies.Model.ListMovieCP;
import software.msouti.popularmovies.Model.Movie;
import software.msouti.popularmovies.Model.MovieResponse;
import software.msouti.popularmovies.R;
import software.msouti.popularmovies.Rest.ApiClient;
import software.msouti.popularmovies.Rest.ApiInterface;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {
    private final static String SORT_BY_POPULARITY = "popular";
    private final static String SORT_BY_TOP_RATED = "top_rated";
    private final static String SORT_BY_FAVORITE = "favorite";
    MovieResponse movieResponse;
    List<Movie> movies;
    RecyclerView recyclerView;
    public static int sort;
    public static final int sMostPopular=0;
    public static final int sTopRated=1;
    public static final int sFavorite=2;
    SharedPreferences sortBy;
    SharedPreferences.Editor sortByEditor;
    String API_KEY = null;
    GridLayoutManager gridLayoutManager;
    MoviesAdapter myAdapter;
    Parcelable mListState;
    String LIST_STATE_KEY="state_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO - insert your themoviedb.org API KEY in String.xml
        API_KEY = getString(R.string.apiKey);
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sortByBoolean), 0);
        sort = mSharedPreferences.getInt(getString(R.string.sortByBoolean), 0);
        if (API_KEY.isEmpty() || API_KEY == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.API_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            recyclerView = findViewById(R.id.movies_recycler_view);
            recyclerView.setHasFixedSize(true);
            gridLayoutManager=new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(gridLayoutManager);
            if (sort== sMostPopular) {
                sortMoviesBy(SORT_BY_POPULARITY);
            } else if (sort == sTopRated){
                sortMoviesBy(SORT_BY_TOP_RATED);
            }else if(sort == sFavorite){
                sortMoviesBy(SORT_BY_FAVORITE);
            }
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
        intent.putExtra(getString(R.string.intentID), movies.get(clickedItemIndex).getId());
        startActivity(intent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sortByBoolean), 0);
        sort = mSharedPreferences.getInt(getString(R.string.sortByBoolean), 0);
        if (API_KEY == null || TextUtils.isEmpty(API_KEY)) {
            Toast.makeText(getApplicationContext(), getString(R.string.API_error), Toast.LENGTH_SHORT).show();
        } else {
            if (sort== sMostPopular) {

                sortMoviesBy(SORT_BY_POPULARITY);
            } else if (sort == sTopRated){
                sortMoviesBy(SORT_BY_TOP_RATED);
            }else if(sort == sFavorite){
                sortMoviesBy(SORT_BY_FAVORITE);
            }
        }
        if (mListState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    public void sortMoviesBy(String sortBy) {
        myAdapter = new MoviesAdapter(MainActivity.this,
                movies,
                R.layout.list_item_movie
                ) ;
        if (sortBy == SORT_BY_FAVORITE){
            Toast.makeText(getApplicationContext(), "sorted by "+ SORT_BY_FAVORITE, Toast.LENGTH_SHORT).show();

            //If user select to SORT_BY_FAVORITE this method will get the movies from ContentProvider and Show it
            Cursor mCursor= getContentResolver().query(AppContract.AppEatery.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if (mCursor!=null){
                Toast.makeText(getApplicationContext(), "Cursor not null !", Toast.LENGTH_SHORT).show();
                List<ListMovieCP> listMovieCPS=new ArrayList<>();
                mCursor.moveToFirst();
                for (int count=0;count<mCursor.getCount();count++){
                    // Fill listMovieCPS with data from mCursor and
                    listMovieCPS.add(new ListMovieCP(
                            mCursor.getString(1),//MovieID
                            mCursor.getString(4),//MoviePosterPath
                            mCursor.getString(2),//MovieTitle
                            mCursor.getString(5),//MovieReleaseDate
                            mCursor.getString(6),//MovieVoteAvg
                            mCursor.getString(3)//MovieOverView
                            ));
                    mCursor.moveToNext();
                }
                //TODO: show Data with the second Constructor of MovieAdapter
                //HERE !!

            }
        }else{

            //This method will send request to get JSON from internet using Retrofit2 library
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<MovieResponse> call = apiService.getMoviesSortBy(sortBy, API_KEY);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    movieResponse = response.body();
                    movies= response.body().getResults();
                     myAdapter = new MoviesAdapter(MainActivity.this,
                            movies,
                            R.layout.list_item_movie) ;
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }


                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                sort = sMostPopular;
                sortBy = getSharedPreferences(getString(R.string.sortByBoolean), 0);
                sortByEditor = sortBy.edit();
                sortByEditor.putInt(getString(R.string.sortByBoolean), sort);
                sortByEditor.apply();
                sortMoviesBy(SORT_BY_POPULARITY);
                break;

            case R.id.top_rated:
                sort = sTopRated;
                sortBy = getSharedPreferences(getString(R.string.sortByBoolean), 0);
                sortByEditor = sortBy.edit();
                sortByEditor.putInt(getString(R.string.sortByBoolean), sort);
                sortByEditor.apply();
                sortMoviesBy(SORT_BY_TOP_RATED);
                break;

            case R.id.favorite:
                sort = sFavorite;
                sortBy = getSharedPreferences(getString(R.string.sortByBoolean), 0);
                sortByEditor = sortBy.edit();
                sortByEditor.putInt(getString(R.string.sortByBoolean), sort);
                sortByEditor.apply();
                sortMoviesBy(SORT_BY_FAVORITE);
                break;
            default:
                Toast.makeText(getApplicationContext(), getString(R.string.menu_error), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }
}
