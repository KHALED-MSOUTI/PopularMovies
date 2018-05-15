package software.msouti.popularmovies.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.msouti.popularmovies.Model.MovieVideos;
import software.msouti.popularmovies.R;
import software.msouti.popularmovies.Rest.ApiClient;
import software.msouti.popularmovies.Rest.ApiInterface;

public class Trailers extends AppCompatActivity {
    String API_KEY;
    String ID;
    MyAdapter adapter;
    @BindView(R.id.ListView) ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        ButterKnife.bind(this);
        API_KEY=getString(R.string.apiKey);
        ID=getIntent().getExtras().getString("id");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieVideos> call = apiService.getVideos(ID,API_KEY);
        call.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Call<MovieVideos> call, final Response<MovieVideos> response) {
                    adapter= new MyAdapter(response.body().getResults());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String key =response.body().getResults().get(position).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + key));
                        startActivity(intent);
                        }

                    });
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




    private class MyAdapter extends BaseAdapter {
       List<MovieVideos.Results> list ;
        MyAdapter(List<MovieVideos.Results> list){
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
                View view0=inflater1.inflate(R.layout.listtrailers,null);
                TextView name= view0.findViewById(R.id.title_text_view);
                name.setText(list.get(i).getName());

            return view0;
        }
    }
}
