package software.msouti.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import software.msouti.popularmovies.Model.Movie;
import software.msouti.popularmovies.Model.Tools;
import software.msouti.popularmovies.R;

/**
 * Created by kh200 on 27/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private static final String TAG ="XXX" ;
    final private ListItemClickListener mOnClickListener;
    private List<Movie> movies;
    private int rowLayout;
    private Context context;


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
        String imagePath = Tools.getImageURL(movies.get(position).getPoster_path());
        Tools.loadPosterImage(TAG, imagePath, holder.imageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;


        public MovieViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
