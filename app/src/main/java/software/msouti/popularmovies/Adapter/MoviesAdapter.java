package software.msouti.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import software.msouti.popularmovies.Model.Movie;
import software.msouti.popularmovies.R;

/**
 * Created by kh200 on 27/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    final private ListItemClickListener mOnClickListener;
    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    private String BASE_IMAGE_PATH="http://image.tmdb.org/t/p/w185/";

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView movieTitle;
    TextView data;
    TextView movieDescription;
    ImageView imageView;

    public MovieViewHolder(View v) {
        super(v);
        imageView=(ImageView)v.findViewById(R.id.imageView);

    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }
}

    public MoviesAdapter(ListItemClickListener mOnClickListener, List<Movie> movies, int rowLayout, Context context) {
        this.mOnClickListener = mOnClickListener;
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(context).load(BASE_IMAGE_PATH + movies.get(position).getPoster_path()).into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return movies.size();
    }
    // allows clicks events to be caught


    // parent activity will implement this method to respond to click events
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
