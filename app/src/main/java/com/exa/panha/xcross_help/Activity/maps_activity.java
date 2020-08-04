package com.exa.panha.xcross_help.Activity;
/*
import android.support.v4.app.FragmentActivity;*/
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Entity.Kebutuhan;
import com.exa.panha.xcross_help.Entity.News;
import com.exa.panha.xcross_help.Entity.Posko;
import com.exa.panha.xcross_help.Asset.MySingleton;
import com.exa.panha.xcross_help.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;


public class maps_activity extends AppCompatActivity implements OnMapReadyCallback {
    //ini pakai android Volley (semacam AsyncTask)
    private GoogleMap mMap;
    RequestQueue queue;
    List<Posko> posko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        myChildToolbar.setTitle("MAP");
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        posko = new ArrayList<Posko>();
        queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setArea();
        setPosko();

    }
    public void setArea(){
        //diubah jadi yg ini kalau misalnya sudah jadi transisi dari bencana ke detail_bencana
        String area = null;
        try {
            area = URLEncoder.encode(getIntent().getStringExtra("area_bencana"),"utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url_area="https://geocoder.api.here.com/6.2/geocode.json?searchtext="+area+"&additionaldata=IncludeShapeLevel,default&app_id=vQcDwYTbVaU5I8QOW8DY&app_code=7e-JHr_gfdpnH_fwpuoYcQ&gen=9";
        //String url_area="https://geocoder.api.here.com/6.2/geocode.json?searchtext=surabaya&additionaldata=IncludeShapeLevel,default&app_id=vQcDwYTbVaU5I8QOW8DY&app_code=7e-JHr_gfdpnH_fwpuoYcQ&gen=9";
        JsonObjectRequest area_req = new JsonObjectRequest(Request.Method.GET, url_area, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String res = null;

                try {
                    res = response.getJSONObject("Response").getJSONArray("View")
                            .getJSONObject(0).getJSONArray("Result").getJSONObject(0)
                            .getJSONObject("Location").getJSONObject("Shape").getString("Value");
                    GeometryFactory geometryFactory = new GeometryFactory();
                    WKTReader reader=new WKTReader(geometryFactory);
                    PolygonOptions polyOptions = null;
                    ArrayList<String> featureList = new ArrayList<String>();
                    MultiPolygon multipolygon = null;
                    Polygon polygon = null;
                    Coordinate[] outerCoordinates = null;
                    Coordinate[] innerCoordinates = null;
                    ArrayList<LatLng> outer = null;
                    ArrayList<LatLng> inner = null;
                    try {
                        multipolygon = (MultiPolygon) reader.read(res);
                        for(int i = 0; i < multipolygon.getNumGeometries(); i++) {
                            outer = new ArrayList<LatLng>();
                            polyOptions = new PolygonOptions();
                            polygon = (Polygon) multipolygon.getGeometryN(i);

                            //Gets each polygon outer coordinates
                            outerCoordinates = polygon.getExteriorRing().getCoordinates();
                            for (Coordinate outerCoordinate : outerCoordinates) {
                                outer.add(new LatLng(outerCoordinate.y, outerCoordinate.x));
                            }
                            polyOptions.addAll(outer);
                            mMap.addPolygon(polyOptions);
                        }
                    }
                    catch (ClassCastException e){
                        try {
                            polygon = (Polygon) reader.read(res);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        outer = new ArrayList<LatLng>();
                        polyOptions = new PolygonOptions();

                        //Gets each polygon outer coordinates
                        outerCoordinates = polygon.getExteriorRing().getCoordinates();
                        for (Coordinate outerCoordinate : outerCoordinates) {
                            outer.add(new LatLng(outerCoordinate.y, outerCoordinate.x));
                        }
                        polyOptions.addAll(outer);
                        mMap.addPolygon(polyOptions);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(area_req);
    }
    public void setPosko(){
        final int id = getIntent().getIntExtra("id_berita", -1);
        System.out.println(id);
        final String url_geocode = "https://geocoder.api.here.com/6.2/geocode.json?app_id=vQcDwYTbVaU5I8QOW8DY%20&app_code=7e-JHr_gfdpnH_fwpuoYcQ%20&searchtext=",
                url_post ="http://opensource.petra.ac.id/~m26416134/cgi-bin/fetchnews_posko.php";
        StringRequest stringRequest_posko = new StringRequest(Request.Method.POST, url_post,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("gagal")) {
                            Log.d("Dunno", response);
                            Type collectionType = new TypeToken<List<Posko>>() {
                            }.getType();
                            Gson gson = new GsonBuilder().create();
                            //class untuk isi data;
                            posko = gson.fromJson(response, collectionType);
                            for (int i=0;i<posko.size();i++){
                                final int id_posko = posko.get(i).getId_posko();
                                String encoded_url = null;

                                try {
                                    encoded_url = url_geocode+URLEncoder.encode(posko.get(i).getAlamat_posko(),"utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                get_address(i, encoded_url);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error Happens somehow. Check if DB data exist or maybe the internet is not working", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(id));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }};
        MySingleton.getInstance(this).addToRequestQueue(stringRequest_posko);
    }
    void get_address(final int i, String encoded_url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, encoded_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        double latitude = 0;
                        double longitude = 0;
                        try {
                            JSONObject res = response.getJSONObject("Response").getJSONArray("View")
                                    .getJSONObject(0).getJSONArray("Result").getJSONObject(0)
                                    .getJSONObject("Location").getJSONObject("DisplayPosition");
                            latitude = res.getDouble("Latitude");
                            longitude = res.getDouble("Longitude");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                        final double finalLongitude = longitude;
                        final double finalLatitude = latitude;
                        mMap.addMarker(new MarkerOptions().position(new LatLng(finalLatitude, finalLongitude)).title(posko.get(i).getNama_posko()).snippet(posko.get(i).getAlamat_posko()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(finalLatitude, finalLongitude), 15));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get menu inflater.
        MenuInflater menuInflater = getMenuInflater();
        // Inflate the menu with custom menu items.
        menuInflater.inflate(R.menu.toolbar1, menu);
        return super.onCreateOptionsMenu(menu);
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
