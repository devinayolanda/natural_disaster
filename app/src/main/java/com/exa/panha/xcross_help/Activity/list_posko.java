package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Asset.ListPoskoAdapter;
import com.exa.panha.xcross_help.Asset.MySingleton;
import com.exa.panha.xcross_help.Entity.Posko;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class list_posko extends AppCompatActivity {
    RecyclerView rv;
    public static ListPoskoAdapter ra;
    public static LinkedList<String> Nama_posko = new LinkedList<>();
    public static LinkedList<String> Alamat_posko = new LinkedList<>();
    public static Integer[] id = {};
    static public SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySingleton.getInstance(this.getApplicationContext());
        setContentView(R.layout.activity_list_posko);
        Bundle isiExtra = getIntent().getExtras();
        String id1 = isiExtra.getString("id_posko");

        RequestParams params = new RequestParams();
        params.put("id", id1);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(0);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/posko.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                if (response.equalsIgnoreCase("gagal")) {
                    Toast.makeText(getApplicationContext(), "Username & Password salah", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        System.out.println(obj.length());

                        id = new Integer[obj.length()];
                        for(int i=0;i<obj.length();i++)
                        {
                            JSONObject node = obj.getJSONObject(i);
                            Nama_posko.addLast(node.getString("nama_posko"));;
                            Alamat_posko.addLast(node.getString("alamat_posko"));;
                            id[i]= node.getInt("id_posko");

                            //System.out.println(node.getString("nama_berita"));
                        }

                        rv = findViewById(R.id.list_posko);
                        ra = new ListPoskoAdapter(list_posko.this, Nama_posko,Alamat_posko,id);
                        rv.setAdapter(ra);
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(new LinearLayoutManager(list_posko.this));

                        //String nama = node.getString("name");
                        // String kode = node.getString("pass");
                        //Toast.makeText(getApplicationContext(), "sukses login " + nama, Toast.LENGTH_SHORT).show();
                        editor = getSharedPreferences("masuk", MODE_PRIVATE).edit();
                        //editor.putString("name", nama);
                        // editor.putString("pass", kode);
                        editor.apply();

                        //startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //JSONArray gagal = new JSONArray("gagal");

            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "gagal insert kategori", Toast.LENGTH_SHORT).show();
            }
        });
        Nama_posko.clear();
        Alamat_posko.clear();
    }
}
