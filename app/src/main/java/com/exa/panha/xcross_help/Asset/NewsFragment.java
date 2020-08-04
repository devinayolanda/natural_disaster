package com.exa.panha.xcross_help.Asset;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Activity.Search_news;
import com.exa.panha.xcross_help.Entity.News;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private List<News> news;
    private SwipeRefreshLayout refreshLayout;
    private TextView warning;
    private ProgressBar pg_bar;
    private SearchView search_bar;
    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        news = new ArrayList<News>();

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        warning = (TextView) rootView.findViewById(R.id.warning_news);
        search_bar = (SearchView) rootView.findViewById(R.id.search_news);
        search_bar.setSubmitButtonEnabled(true);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search_bar.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getActivity(), Search_news.class)));
        search_bar.setOnQueryTextListener(this);
        search_bar.setFocusable(false);
        search_bar.setIconified(false);
        search_bar.clearFocus();
        pg_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_news); //jika tidak mau menggunakan animasi dari swiperefreshlayout pakai yg ini
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.layout_refresh_news);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchnews.php");

            }
        });
        // downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchnews.php");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.news_view);
        mAdapter = new NewsAdapter(news);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchnews.php");
        return rootView;
    }
    //v1 (dari punya kodingan angel)
    void generateberita()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                "http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchnews.php",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {

                        //app-dialog
                    }
                    @Override
                    public void onSuccess(String response) {
                        Type collectionType = new TypeToken<List<News>>(){}.getType();
                        Gson gson = new GsonBuilder().create();
                        //class untuk isi data;
                        news = gson.fromJson(response, collectionType);
                    }
                });
    }

    private void downloadJSON(final String urlWeb) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWeb);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                        @Override
                        public void run() {
                            warning.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                    return sb.toString().trim();
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                        @Override
                        public void run() {
                            warning.setText(R.string.please_check_your_internet_connection);
                            warning.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    });
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                refreshLayout.setRefreshing(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Type collectionType = new TypeToken<List<News>>(){}.getType();
                Gson gson = new GsonBuilder().create();
                //class untuk isi data;
                if (s != null)
                    news = gson.fromJson(s, collectionType);
                mAdapter = new NewsAdapter(news);
                mRecyclerView.setAdapter(mAdapter);
                refreshLayout.setRefreshing(false);
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchnews.php");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
