package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class sign_in extends AppCompatActivity {
    private static final String Setting_name = "Log_In";
    private Intent intent;
    private MaterialButton btn_log;
    private TextView sign_up, forgot_pass;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String str_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        intent = new Intent();
        btn_log = (MaterialButton) findViewById(R.id.btn_log);
        sign_up = (TextView) findViewById(R.id.signup);
        forgot_pass = (TextView) findViewById(R.id.forgotpass);

        pref = getSharedPreferences(Setting_name, MODE_PRIVATE);
        editor = pref.edit();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_in.this, com.exa.panha.xcross_help.Activity.sign_up.class);
                startActivity(intent);
                finish();
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_in.this, com.exa.panha.xcross_help.Activity.forgot_pass.class);
                startActivity(intent);
                finish();
            }
        });
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadJSON("http://opensource.petra.ac.id/~m26416042tw/login2.php");
            }
        });
    }
    private void downloadJSON(final String urlWeb) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                TextInputLayout var_email = (TextInputLayout) findViewById(R.id.input_email);
                TextInputLayout var_password = (TextInputLayout) findViewById(R.id.input_password);

                str_email = var_email.getEditText().getText().toString();
                final String str_password = var_password.getEditText().getText().toString();
                try {
                    URL url = new URL(urlWeb);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username",str_email));
                    params.add(new BasicNameValuePair("password",str_password));
                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(params));
                    writer.flush();
                    writer.close();
                    os.close();
                    con.connect();
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
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    login(s);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
    private void login(String json) throws JSONException{
        if(str_email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Wrong Email or Password !", Toast.LENGTH_SHORT).show();
        }
        else{
            if (json.equals("Success")) {
                Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                final TextInputLayout var_email = (TextInputLayout) findViewById(R.id.input_email);
                TextInputLayout var_password = (TextInputLayout)  findViewById(R.id.input_password);

                RequestParams params = new RequestParams();
                params.put("email", var_email.getEditText().getText().toString());
                params.put("password", var_password.getEditText().getText().toString());
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(5000);
                RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/login.php", params, new AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        if (response.equalsIgnoreCase("gagal")) {
                            Toast.makeText(getApplicationContext(), "Username / Password salah", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONArray obj = null;
                            try {
                                obj = new JSONArray(response);
                                for (int i = 0; i < obj.length(); i++) {
                                    JSONObject node = obj.getJSONObject(i);
                                    String id_donatur = node.getString("id_donatur");
                                    editor.putString("id_donatur", id_donatur).commit();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
                    }
                });

                editor.putString("user", var_email.getEditText().getText().toString()).commit();
                editor.putBoolean("Login", true).commit();
                editor.putString("password", var_password.getEditText().getText().toString()).commit();
                editor.commit();
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Email or Password !", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}
