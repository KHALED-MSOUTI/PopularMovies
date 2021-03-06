package software.msouti.popularmovies.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.msouti.popularmovies.Model.MovieReview;
import software.msouti.popularmovies.R;
import software.msouti.popularmovies.Rest.ApiClient;
import software.msouti.popularmovies.Rest.ApiInterface;

public class Reviews extends AppCompatActivity {
    ListView listView;
    String API_KEY;
    String ID;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        listView= findViewById(R.id.Reviews_ListView);
        API_KEY=getString(R.string.apiKey);
        ID=getIntent().getExtras().getString("id");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieReview> call = apiService.getReviews(ID,API_KEY);
        call.enqueue(new Callback<MovieReview>() {
            @Override
            public void onResponse(Call<MovieReview> call, Response<MovieReview> response) {
                adapter= new MyAdapter(response.body().getResults());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieReview> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class MyAdapter extends BaseAdapter {
        List<MovieReview.Results> list ;
        MyAdapter(List<MovieReview.Results> list){
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i).getId();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater1=getLayoutInflater();
            View view0=inflater1.inflate(R.layout.listreview,null);
            TextView name= view0.findViewById(R.id.title_textView_Reviews);
            TextView body= view0.findViewById(R.id.body_textView_Reviews);
            name.setText(list.get(i).getAuthor());
            body.setText(list.get(i).getContent());
            return view0;
        }
    }
}
