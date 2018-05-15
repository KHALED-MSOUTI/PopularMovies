package software.msouti.popularmovies.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.msouti.popularmovies.Adapter.MoviesAdapter;
import software.msouti.popularmovies.Model.Movie;
import software.msouti.popularmovies.Model.MovieResponse;
import software.msouti.popularmovies.R;
import software.msouti.popularmovies.Rest.ApiClient;
import software.msouti.popularmovies.Rest.ApiInterface;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {
    private final static String SORT_BY_POPULARITY = "popular";
    private final static String SORT_BY_TOP_RATED = "top_rated";
    MovieResponse movieResponse;
    List<Movie> movies;
    RecyclerView recyclerView;
    Boolean sort;
    SharedPreferences sortBy;
    SharedPreferences.Editor sortByEditor;
    String API_KEY = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO - insert your themoviedb.org API KEY in String.xml
        API_KEY = getString(R.string.apiKey);
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sortByBoolean), 0);
        sort = mSharedPreferences.getBoolean(getString(R.string.sortByBoolean), true);
        if (API_KEY.isEmpty() || API_KEY == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.API_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            recyclerView = findViewById(R.id.movies_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            if (sort) {
                sortMoviesBy(SORT_BY_POPULARITY);
            } else {
                sortMoviesBy(SORT_BY_TOP_RATED);
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
        sort = mSharedPreferences.getBoolean(getString(R.string.sortByBoolean), true);
        if (API_KEY == null || TextUtils.isEmpty(API_KEY)) {
            Toast.makeText(getApplicationContext(), getString(R.string.API_error), Toast.LENGTH_SHORT).show();
        } else {
            if (sort) {
                sortMoviesBy(SORT_BY_POPULARITY);
            } else {
                sortMoviesBy(SORT_BY_TOP_RATED);
            }
        }
    }

    public void sortMoviesBy(String sortBy) {
        //This method will send request to get JSON from internet using Retrofit2 library
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getMoviesSortBy(sortBy, API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieResponse = response.body();
                movies= response.body().getResults();
                MoviesAdapter myAdapter = new MoviesAdapter(MainActivity.this,
                        movies,
                        R.layout.list_item_movie,
                        getApplicationContext()) ;
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
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
                sort = true;
                sortBy = getSharedPreferences(getString(R.string.sortByBoolean), 0);
                sortByEditor = sortBy.edit();
                sortByEditor.putBoolean(getString(R.string.sortByBoolean), sort);
                sortByEditor.apply();
                sortMoviesBy(SORT_BY_POPULARITY);
                break;

            case R.id.top_rated:
                sort = false;
                sortBy = getSharedPreferences(getString(R.string.sortByBoolean), 0);
                sortByEditor = sortBy.edit();
                sortByEditor.putBoolean(getString(R.string.sortByBoolean), sort);
                sortByEditor.apply();
                sortMoviesBy(SORT_BY_TOP_RATED);
                break;
            default:
                Toast.makeText(getApplicationContext(), getString(R.string.menu_error), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

}
