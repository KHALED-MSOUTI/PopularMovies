package software.msouti.popularmovies.Model;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


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
    public static void loadPosterImage(final String TAG, final String url, final ImageView imageView) {

        Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "success");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "loadPosterImage - onError: " + e.getLocalizedMessage());
                Picasso.get().load(url).into(imageView);
            }
        });
    }
}
