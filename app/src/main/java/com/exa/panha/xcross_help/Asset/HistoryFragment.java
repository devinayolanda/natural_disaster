package com.exa.panha.xcross_help.Asset;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exa.panha.xcross_help.Activity.Search_history;
import com.exa.panha.xcross_help.Entity.History;
import com.exa.panha.xcross_help.Entity.News;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private List<History> histories;
    private SwipeRefreshLayout refreshLayout;
    private TextView warning;
    private ProgressBar pg_bar;
    private SearchView search_bar;
    public HistoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        histories = new ArrayList<History>();
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        // my_child_toolbar is defined in the layout file
//        Toolbar myChildToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_history);
//        myChildToolbar.setTitle("HISTORY");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();

        // Enable the Up button
  //           ab.setDisplayHomeAsUpEnabled(true);

        warning = (TextView) rootView.findViewById(R.id.warning_history);
        pg_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_history); //jika tidak mau menggunakan animasi dari swiperefreshlayout pakai yg ini
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.layout_refresh_history);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchhistory.php");
            }
        });
        warning = (TextView) rootView.findViewById(R.id.warning_history);
        search_bar = (SearchView) rootView.findViewById(R.id.search_history);
        search_bar.setSubmitButtonEnabled(true);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search_bar.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getActivity(), Search_history.class)));
        //search_bar.setOnQueryTextListener(this);
        search_bar.setFocusable(false);
        search_bar.setIconified(false);
        search_bar.clearFocus();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.history_view);
        mAdapter = new HistoryAdapter(histories);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchhistory.php");
        return rootView;



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_history, container, false);
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
                Type collectionType = new TypeToken<List<History>>(){}.getType();
                Gson gson = new GsonBuilder().create();
                //class untuk isi data;
               // System.out.println(s);
                if (s != null)
                    histories = gson.fromJson(s, collectionType);
                mAdapter = new HistoryAdapter(histories);
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
        downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchhistory.php");
    }
}
