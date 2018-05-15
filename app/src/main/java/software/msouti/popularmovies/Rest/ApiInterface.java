package software.msouti.popularmovies.Rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import software.msouti.popularmovies.Model.MovieIDRequest;
import software.msouti.popularmovies.Model.MovieResponse;
import software.msouti.popularmovies.Model.MovieReview;
import software.msouti.popularmovies.Model.MovieVideos;


public interface ApiInterface {
    @GET("movie/{sortBy}")
    Call<MovieResponse> getMoviesSortBy(@Path("sortBy") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieIDRequest> getMovieByID(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieVideos> getVideos(@Path("id") String id,@Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReview> getReviews(@Path("id") String id, @Query("api_key") String apiKey);
}


