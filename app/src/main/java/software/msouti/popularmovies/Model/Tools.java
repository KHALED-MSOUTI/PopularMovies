package software.msouti.popularmovies.Model;

import android.net.Uri;


public class Tools {
    public static String getImageURL(String imagePath) {
        //This method will build Uri for images from given poster_path
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w500")
                .appendEncodedPath(imagePath);
        return builder.build().toString();
    }
}
