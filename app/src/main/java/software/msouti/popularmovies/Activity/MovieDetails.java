package software.msouti.popularmovies.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.msouti.popularmovies.Model.AppContract;
import software.msouti.popularmovies.Model.MovieIDRequest;
import software.msouti.popularmovies.Model.Tools;
import software.msouti.popularmovies.R;
import software.msouti.popularmovies.Rest.ApiClient;
import software.msouti.popularmovies.Rest.ApiInterface;

import static software.msouti.popularmovies.Model.AppContract.AppEntery.CONTENT_URI;

public class MovieDetails extends AppCompatActivity  {
    String API_KEY;
    MovieIDRequest movie = new MovieIDRequest();
    @BindView(R.id.mImageViewArraw)
    ImageView mImageViewArrow;
    @BindView(R.id.mImageViewMovie)
    ImageView mImageViewMovie;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.posterImageView)
    ImageView posterImageView;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.yearTextView)
    TextView yearTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.descTextView)
    TextView descTextView;
    @BindView(R.id.trailerImageView)
    ImageView trailerImageView;
    @BindView(R.id.commentsImageView)
    ImageView commentsImageView;
    @BindView(R.id.favoriteImageView)
    ImageView favoriteImageView;
    final String TAG= "XXX";
    String movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.secoundTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        API_KEY = getString(R.string.apiKey);
        movie_id = getIntent().getExtras().getString(getString(R.string.intentID));
        if (savedInstanceState!=null){
            movie= (MovieIDRequest) savedInstanceState.getSerializable("movie");
            fillOutDetails(movie);
        }else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MovieIDRequest> call = apiService.getMovieByID(movie_id, API_KEY);
            call.enqueue(new Callback<MovieIDRequest>() {
                @Override
                public void onResponse(Call<MovieIDRequest> call, Response<MovieIDRequest> response) {
                    movie = response.body();
                    fillOutDetails(movie);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(AppContract.AppEntery.ColumnMovieRelaseDate, movie.getRelease_date());
                    contentValues.put(AppContract.AppEntery.ColumnMovieName, movie.getTitle());
                    contentValues.put(AppContract.AppEntery.ColumnMovieID, movie.getId());
                    contentValues.put(AppContract.AppEntery.ColumnMovieOverView, movie.getOverview());
                    contentValues.put(AppContract.AppEntery.ColumnMoviePosterPath, movie.getPoster_path());
                    contentValues.put(AppContract.AppEntery.ColumnMovieRelaseDate, movie.getRelease_date());
                    contentValues.put(AppContract.AppEntery.ColumnFav, false);
                    //Insert film into Database !!
                    Uri uri = getContentResolver().insert(AppContract.AppEntery.CONTENT_URI, contentValues);
                    if (uri != null) {
                        Toast.makeText(getApplicationContext(), "Added to database Using RETROFIT !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERrOr !!!!", Toast.LENGTH_SHORT).show();
                    }
                    getContentResolver().notifyChange(CONTENT_URI, null);
                }

                @Override
                public void onFailure(Call<MovieIDRequest> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed To get response from Json", Toast.LENGTH_LONG).show();
                }
            });
        }



        trailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getExtras().getString(getString(R.string.intentID));
                Intent intent = new Intent(getApplicationContext(), Trailers.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        commentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getExtras().getString(getString(R.string.intentID));
                Intent intent = new Intent(getApplicationContext(), Reviews.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO (3): Create Content Provider and use it !!!
                int movieID = movie.getId();
                String movieName = movie.getTitle();
                String movieOverView = movie.getOverview();
                String moviePosterPath = movie.getPoster_path();
                String movieRelaseDate = movie.getRelease_date();
                Double movieRate = movie.getVote_average();
                ContentValues contentValues = new ContentValues();
                contentValues.put(AppContract.AppEntery.ColumnMovieRelaseDate, movieRelaseDate);
                contentValues.put(AppContract.AppEntery.ColumnMovieName, movieName);
                contentValues.put(AppContract.AppEntery.ColumnMovieID, movieID);
                contentValues.put(AppContract.AppEntery.ColumnMovieOverView, movieOverView);
                contentValues.put(AppContract.AppEntery.ColumnMoviePosterPath, moviePosterPath);
                contentValues.put(AppContract.AppEntery.ColumnMovieRelaseDate, movieRate);
                contentValues.put(AppContract.AppEntery.ColumnFav, true);
                String where = AppContract.AppEntery.ColumnMovieID + " = " + movieID;
                int isUpdated = getContentResolver().update(AppContract.AppEntery.CONTENT_URI, contentValues, where, null);
                if (isUpdated > 0) {
                    Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
                    favoriteImageView.setImageResource(R.drawable.golden_star);

                } else {
                    Toast.makeText(getApplicationContext(), "ERrOr !!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        mImageViewArrow.startAnimation(animation);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable("movie",movie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        movie= (MovieIDRequest) savedInstanceState.getSerializable("movie");
        fillOutDetails(movie);
    }
    public void fillOutDetails(MovieIDRequest movieIDRequest){
        Tools.loadPosterImage(TAG, Tools.getImageURL(movieIDRequest.getPoster_path()), mImageViewMovie);
        Tools.loadPosterImage(TAG, Tools.getImageURL(movieIDRequest.getPoster_path()), posterImageView);
        toolbar.setTitle(movieIDRequest.getTitle());
        titleTextView.setText(movieIDRequest.getTitle());
        yearTextView.setText(movieIDRequest.getRelease_date());
        ratingBar.setRating((float) (movieIDRequest.getVote_average() / 2));
        descTextView.setText(movieIDRequest.getOverview());

    }
}
