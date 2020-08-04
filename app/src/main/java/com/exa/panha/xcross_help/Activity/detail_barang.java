package com.exa.panha.xcross_help.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.exa.panha.xcross_help.Asset.RecyclerViewBarang;
import com.exa.panha.xcross_help.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class detail_barang extends AppCompatActivity {
    private Object a = new Object();

    public static Integer[] id_posko = {};
    private int count = 0;
    public int length = 0;

    ProgressDialog progressDialog;

    public static RecyclerView recview;


    public static LinkedList<String> mBarang = new LinkedList<>();
    public static LinkedList<Integer> mTotal = new LinkedList<>();
    public static LinkedList<String> mPath = new LinkedList<>();
    public static RecyclerView mRecyclerView;
    public static RecyclerViewBarang mAdapter;
    static public SharedPreferences.Editor editor1;
    public int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kebutuhan);
        Log.e("msg", "BISA");
        Bundle isiExtra = getIntent().getExtras();
        id = isiExtra.getInt("id");
        String nama = isiExtra.getString("nama");


        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("id",id);
        client.setTimeout(0);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/posko1.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                if (response.equalsIgnoreCase("gagal")) {
                    Toast.makeText(getApplicationContext(), "Username & Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray obj = null;
                    try {

                        obj = new JSONArray(response);
                        id_posko = new Integer[obj.length()];
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject node = obj.getJSONObject(i);
                            id_posko[i] = node.getInt("id_posko");
                            if (id + 1 == id_posko[i]) {
                                if(node.getString("nama_barang").equals("") && node.getString("jumlah_barang_min").equals("") && node.getString("stock_barang").equals("")) {

                                }
                                else{
                                    mBarang.addLast(node.getString("nama_barang"));
                                    mPath.addLast(node.getString("file_path_barang"));
                                    if(node.getInt("jumlah_barang_min")-node.getInt("stock_barang") > 0)
                                        mTotal.addLast(node.getInt("jumlah_barang_min")-node.getInt("stock_barang"));
                                    else
                                        mTotal.addLast(0);
                                }
                            }
                        }

                        mRecyclerView = (RecyclerView) findViewById(R.id.list_kebutuhan);
                        mAdapter = new RecyclerViewBarang(detail_barang.this, mBarang,mTotal,mPath);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(detail_barang.this));

                        editor1 = getSharedPreferences("coba", MODE_PRIVATE).edit();

                        editor1.apply();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "gagal insert kategori", Toast.LENGTH_SHORT).show();
            }
        });

        mBarang.clear();
        mTotal.clear();
        mPath.clear();
        //LoadData a = new LoadData();
        //a.execute();



//    public static Integer[] id_posko = {};
//    public static Integer[] jumlah_barang_min={};
//    public static Integer[] stock={};
//    public static String[] nama_barang={};
//    public static ArrayList<String> nama_barang_temp = new ArrayList<>();
//    public static ArrayList<Integer> jumlah_barang_min_temp = new ArrayList<>();
//    public static ArrayList<Integer> stock_temp = new ArrayList<>();
//    private int count = 0;
//    public int length = 0;
//
//    public static LinkedList<String> mBarang = new LinkedList<>();
//    public static LinkedList<Integer> mTotal = new LinkedList<>();
//    private RecyclerView mRecyclerView;
//    private RecyclerAdapter mAdapter;
//
//    public String id;
//    ListView lv;
//    TextView judul;
//    static public SharedPreferences.Editor editor1;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//        Bundle isiExtra = getIntent().getExtras();
//        id = isiExtra.getString("id");
//        String nama = isiExtra.getString("nama");
//        Log.e("aaa",nama);
//        //judul = findViewById(R.id.judul);
//        //judul.setText(nama);
//        nama_barang_temp.clear();
//        jumlah_barang_min_temp.clear();
//        stock_temp.clear();
//
//
//
//        //-----------------------------
//        Button button = findViewById(R.id.tombol);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                Runnable runnable = new Runnable() {
//                    public void run() {
////                        generatebarang(v);
//
//                    }
//                };
//                new Handler().postDelayed(runnable, 2000);
//            }
//        });
//        button.performClick();
//
//    }
//
//    public void generatebarang(View view) {
////        Toast.makeText(this, ""+count++, Toast.LENGTH_SHORT).show();
//        RequestParams params = new RequestParams();
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(0);
//        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/posko1.php", params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                if (response.equalsIgnoreCase("gagal")) {
//                    Toast.makeText(getApplicationContext(), "Username & Password salah", Toast.LENGTH_SHORT).show();
//                } else {
//                    JSONArray obj = null;
//                    try {
//
//                        obj = new JSONArray(response);
//                        id_posko = new Integer[obj.length()];
//                        for (int i = 0; i < obj.length(); i++) {
//                            JSONObject node = obj.getJSONObject(i);
//                            id_posko[i] = node.getInt("id_posko");
//                            if (Integer.parseInt(id) + 1 == id_posko[i]) {
//                                if(node.getString("nama_barang").equals("") && node.getString("jumlah_barang_min").equals("") && node.getString("stock_barang").equals("")) {
//
//                                }
//                                else{
//                                    mBarang.addLast(node.getString("nama_barang"));
//                                    mTotal.addLast(node.getInt("jumlah_barang_min")-node.getInt("stock_barang"));
//                                    length++;
//                                    Log.e("coba",node.getString("jumlah_barang_min"));
//                                    nama_barang_temp.add(node.getString("nama_barang"));
//                                    jumlah_barang_min_temp.add(node.getInt("jumlah_barang_min"));
//                                    stock_temp.add(node.getInt("stock_barang"));
//                                }
//                            }
//                        }
//                        nama_barang = new String[length];
//                        jumlah_barang_min = new Integer[length];
//                        stock = new Integer[length];
//                        id_posko = new Integer[length];
//                        for(int i = 0; i < length; i++){
//                            nama_barang[i] = nama_barang_temp.get(i);
//                            jumlah_barang_min[i] = jumlah_barang_min_temp.get(i);
//                            stock[i] = stock_temp.get(i);
//                            id_posko[i] = i;
//
//                        }
//
//
////                        int posisi=0;
////                        for (int i = 0; i < obj.length(); i++) {
////                            JSONObject node = obj.getJSONObject(i);
////                            id_posko[i] = node.getInt("id_posko");
////                            if(Integer.parseInt(id)+1 == id_posko[i]){
////
////                                if(node.getString("nama_barang").equals("") && node.getString("jumlah_barang_min").equals("") && node.getString("stock_barang").equals("")) {
////
////                                }
////                                else{
////                                    nama_barang[posisi] = node.getString("nama_barang");
////                                    jumlah_barang_min[posisi] = node.getInt("jumlah_barang_min");
////                                    stock[posisi] = node.getInt("stock_barang");
////                                    posisi++;
////                                }
////                            }
//
//                            //System.out.println(node.getString("nama_berita"));
////                        }
//                        //String nama = node.getString("name");
//                        // String kode = node.getString("pass");
//                        //Toast.makeText(getApplicationContext(), "sukses login " + nama, Toast.LENGTH_SHORT).show();
//                        editor1 = getSharedPreferences("coba", MODE_PRIVATE).edit();
//                        //editor.putString("name", nama);
//                        // editor.putString("pass", kode);
//                        editor1.apply();
//
//                        //startActivity(intent);
//
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                //JSONArray gagal = new JSONArray("gagal");
//
//            }
//            @Override
//            public void onFailure(int statusCode, Throwable error, String content) {
//                Toast.makeText(getApplicationContext(), "gagal insert kategori", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mRecyclerView = findViewById(R.id.recyclerview);
//        mAdapter = new RecyclerAdapter(this, mBarang,mTotal);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        mBarang.clear();
//        mTotal.clear();
//
////        lv = findViewById(R.id.list);
////        ListBarangAdapter cal = new ListBarangAdapter(detail.this,nama_barang,jumlah_barang_min,stock,id_posko);
////
////        lv.setAdapter(cal);
////        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                AlertDialog.Builder myBuilder = new AlertDialog.Builder(view.getContext());
//////                myBuilder.setMessage(Nama_posko[position] + " dihapus?");
//////                AlertDialog dialog = myBuilder.create();
//////                dialog.show();
////                Log.e("asd",String.valueOf(id));
////
////            }
////
////        });
//    }
    }

//    class LoadData extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(detail.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
//        }
//    }

}
