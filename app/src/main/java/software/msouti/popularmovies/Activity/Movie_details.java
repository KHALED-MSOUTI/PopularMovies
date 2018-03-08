package software.msouti.popularmovies.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.msouti.popularmovies.Model.MovieIDRequest;
import software.msouti.popularmovies.Model.Tools;
import software.msouti.popularmovies.R;
import software.msouti.popularmovies.Rest.ApiClient;
import software.msouti.popularmovies.Rest.ApiInterface;

public class Movie_details extends AppCompatActivity {
    String API_KEY;
    MovieIDRequest movie = new MovieIDRequest();

    ImageView mImageViewMovie;
    ImageView mImageViewArrow;
    TextView mTextViewReleaseDate;
    TextView mTextViewOverView;
    AppCompatRatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.secoundTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        API_KEY = getString(R.string.apiKey);
        mImageViewArrow = findViewById(R.id.mImageViewArraw);
        mImageViewMovie = findViewById(R.id.mImageViewMovie);
        mTextViewOverView = findViewById(R.id.mTextViewOverViewText);
        mTextViewReleaseDate = findViewById(R.id.mTextViewYearText);
        ratingBar = findViewById(R.id.rateBar);

        String id = getIntent().getExtras().getString(getString(R.string.intentID));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieIDRequest> call = apiService.getMovieByID(id, API_KEY);
        call.enqueue(new Callback<MovieIDRequest>() {
            @Override
            public void onResponse(Call<MovieIDRequest> call, Response<MovieIDRequest> response) {
                movie = response.body();
                Picasso.with(Movie_details.this).load(Tools.getImageURL(movie.getPoster_path())).into(mImageViewMovie);
                mTextViewReleaseDate.setText(movie.getRelease_date());
                mTextViewOverView.setText(movie.getOverview());
                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle(movie.getTitle());
                ratingBar.setRating((float) (movie.getVote_average() / 2));
                ratingBar.setClickable(false);

            }

            @Override
            public void onFailure(Call<MovieIDRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Faild To get response from Json", Toast.LENGTH_LONG).show();
            }
        });
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        mImageViewArrow.startAnimation(animation);

    }


}
