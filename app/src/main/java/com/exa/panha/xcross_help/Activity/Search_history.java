package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Asset.HistoryAdapter;
import com.exa.panha.xcross_help.Asset.MySingleton;
import com.exa.panha.xcross_help.Asset.NewsAdapter;
import com.exa.panha.xcross_help.Entity.History;
import com.exa.panha.xcross_help.Entity.News;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_history extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private List<History> histories;
    private SwipeRefreshLayout refreshLayout;
    private TextView warning;
    private TextView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.search_toolbar1);
        myChildToolbar.setTitle("Search");
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        search = (TextView) findViewById(R.id.Res_text1);
        search.setText(String.format("Result for: %s", getIntent().getStringExtra(SearchManager.QUERY)));
        histories = new ArrayList<History>();
        MySingleton.getInstance(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_search1);
        mAdapter = new HistoryAdapter(histories);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        warning = (TextView) findViewById(R.id.warning_search1);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_search1);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,

                android.R.color.holo_blue_dark);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                handleIntent(getIntent());
            }
        });
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = getIntent().getStringExtra(SearchManager.QUERY);
            refresh(query);
        }
    }
    public void refresh(final String query){
        refreshLayout.setRefreshing(true);
        StringRequest search = new StringRequest(Request.Method.POST, "http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchhistory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                if (!response.equals("gagal")) {
                    Log.i("Response", "onResponse: "+response);
                    Type collectionType = new TypeToken<List<History>>(){}.getType();
                    Gson gson = new GsonBuilder().create();
                    //class untuk isi data;
                    histories = gson.fromJson(response, collectionType);
                    mAdapter = new HistoryAdapter(histories);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    //mAdapter.notifyDataSetChanged();
                } else {
                    //kosong entah mau tampilkan textview a
                    warning.setText(R.string.error_search);
                    warning.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // internet nggak aktif atau apapun itu
                refreshLayout.setRefreshing(false);
                warning.setText(R.string.please_check_your_internet_connection);
                warning.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", query);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }};
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(search);
    }

    @Override
    public void onRefresh() {
        warning.setVisibility(View.GONE);
        mRecyclerView.clearAnimation();
        refreshLayout.setRefreshing(true);
        handleIntent(getIntent());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_share:
                //tolong siapa yg bertugas untuk buat uri nya
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }
}
