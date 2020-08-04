package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exa.panha.xcross_help.Asset.DetailHistoryAdapter;
import com.exa.panha.xcross_help.Asset.HistoryAdapter;
import com.exa.panha.xcross_help.Entity.DetailHistory;
import com.exa.panha.xcross_help.Entity.History;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class detail_history extends AppCompatActivity {
    TextView tanggal,status;
    AppCompatImageView code;
    private RecyclerView mRecyclerView;
    private DetailHistoryAdapter mAdapter;
    private List<DetailHistory> dethistories;
    private SwipeRefreshLayout refreshLayout;
    private TextView warning;
    private ProgressBar pg_bar;
    String kodeqr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        dethistories = new ArrayList<DetailHistory>();

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.activity_detail_history,null);

        final History isi = (History) getIntent().getParcelableExtra("hist");
        final int id =isi.getId_donasi();
        code = (AppCompatImageView) findViewById(R.id.qrcode);
        tanggal =(TextView) findViewById(R.id.tanggal_donasi);
        status =(TextView) findViewById(R.id.status_donasi);
        kodeqr = isi.getKode_donasi();
        MultiFormatWriter mfw = new MultiFormatWriter();
        try{
            BitMatrix bm = mfw.encode(kodeqr.trim(), BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder be = new BarcodeEncoder();
            Bitmap b = be.createBitmap(bm);
            code.setImageBitmap(b);
        } catch (WriterException e){
            e.printStackTrace();
        }
        tanggal.setText(isi.getTanggal_donasi());
        status.setText(isi.getStatus_donasi());

        warning = (TextView) rootView.findViewById(R.id.warning_detail_history);
        pg_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_detail_history); //jika tidak mau menggunakan animasi dari swiperefreshlayout pakai yg ini
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.layout_refresh_detail_history);
        // refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchdetailhistory.php?id=" + String.valueOf(id));
            }
        });


        mRecyclerView = (RecyclerView)findViewById(R.id.detail_history);
        mAdapter = new DetailHistoryAdapter(dethistories);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter( mAdapter );


        downloadJSON("http://opensource.petra.ac.id/~m26416042tw/Manpro2/fetchdetailhistory.php?id=" + String.valueOf(id));
        // return rootView;


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
                Type collectionType = new TypeToken<List<DetailHistory>>(){}.getType();
                Gson gson = new GsonBuilder().create();
                //class untuk isi data;
                System.out.println(s);
                if (s != null)
                    dethistories = gson.fromJson(s, collectionType);
                mAdapter = new DetailHistoryAdapter(dethistories);
                mRecyclerView.setAdapter(mAdapter);
                refreshLayout.setRefreshing(false);
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
}
