package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Asset.ListBarangAdapter;
import com.exa.panha.xcross_help.Asset.MySingleton;
import com.exa.panha.xcross_help.Entity.Kebutuhan;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class list_kebutuhan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kebutuhan);
        final int id_posko = getIntent().getIntExtra("id",-1);
        String nama = getIntent().getStringExtra("nama");
        String url_kebutuhan = "http://opensource.petra.ac.id/~m26416134/cgi-bin/fetchnews_kebutuhan.php";
        TextView tv_posko = (TextView) findViewById(R.id.nama_Posko);
        tv_posko.setText(nama);
        final StringRequest get_keb = new StringRequest
                (Request.Method.POST, url_kebutuhan, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type collectionType = new TypeToken<List<Kebutuhan>>() {
                        }.getType();
                        Gson gson = new GsonBuilder().create();
                        List<Kebutuhan> kebutuhan = gson.fromJson(response, collectionType);
                        ListView listView = (ListView) findViewById(R.id.list_kebutuhan);
                        ListBarangAdapter barangAdapter = new ListBarangAdapter(getApplicationContext(), kebutuhan);
                        listView.setAdapter(barangAdapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(id_posko));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }};
        MySingleton.getInstance(this).addToRequestQueue(get_keb);
    }
}
