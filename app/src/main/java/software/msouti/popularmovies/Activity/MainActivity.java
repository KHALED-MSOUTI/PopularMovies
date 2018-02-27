package software.msouti.popularmovies.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
    private static final String TAG = MainActivity.class.getSimpleName();
    List<Movie> movies;
    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "957413e202e8b911c7b5134c3e38bb52";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                movies= response.body().getResults();
                MoviesAdapter myAdapter = new MoviesAdapter(MainActivity.this,
                        movies,
                        R.layout.list_item_movie,
                        getApplicationContext()) ;
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
//TODO : setup OnClickListener as well !!
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(getApplicationContext(),movies.get(clickedItemIndex).getTitle(),Toast.LENGTH_LONG).show();
    }



}
