package com.exa.panha.xcross_help.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.exa.panha.xcross_help.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import androidx.appcompat.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {
    //Splash screen untuk loading aset & cek koneksi ke db
    private static final String Setting_name = "Log_In";
    private SharedPreferences pref;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getSharedPreferences(Setting_name, MODE_PRIVATE);
        progressBar = (ProgressBar) findViewById(R.id.progress_btn_splash);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        downloadJSON("http://opensource.petra.ac.id/~m26416134/cgi-bin/test_con.php");
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
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    test_con(s);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
    private void test_con(String json) throws JSONException{
        /*JSONArray jsonArray= new JSONArray(json);
        String[] stocks = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("name") + " " + obj.getString("price");
        }*/;
        json = json.replaceAll(String.valueOf('\"'), "");
        if (json.equals("Success")) {
            Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
            if (!pref.getBoolean("Login", false)) {
                Intent intent = new Intent(this, sign_in.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                //Intent intent = new Intent(this, sign_in.class);
                startActivity(intent);
            }

        }
        else{
            //entah toast atau dialog kalau mau
            Toast.makeText(this, "Can't connect to server",Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
