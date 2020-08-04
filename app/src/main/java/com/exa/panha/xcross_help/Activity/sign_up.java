package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Asset.MySingleton;
import com.exa.panha.xcross_help.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    Button b1, b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        b1 = (Button) findViewById(R.id.btn_reg);
        b2 = (Button) findViewById(R.id.btn_back);
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        MySingleton.getInstance(this.getApplicationContext());
        //CardView insertRegister = (CardView) findViewById(R.id.registerBtn);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout var_nama = (TextInputLayout) findViewById(R.id.nameText);
                TextInputLayout var_alamat = (TextInputLayout) findViewById(R.id.alamatText);
                TextInputLayout var_telepon = (TextInputLayout) findViewById(R.id.teleponText);
                TextInputLayout var_email = (TextInputLayout) findViewById(R.id.emailText);
                TextInputLayout var_password = (TextInputLayout) findViewById(R.id.passwordText);
                TextInputLayout var_password2 = (TextInputLayout) findViewById(R.id.passwordText2);
                TextInputLayout var_username = (TextInputLayout) findViewById(R.id.usernameText);


                final String str_nama = var_nama.getEditText().getText().toString();
                final String str_alamat = var_alamat.getEditText().getText().toString();
                final String str_telepon = var_telepon.getEditText().getText().toString();
                final String str_email = var_email.getEditText().getText().toString();
                final String str_password = var_password.getEditText().getText().toString();
                final String str_password2 = var_password2.getEditText().getText().toString();
                final String str_username = var_username.getEditText().getText().toString();

                // ------ COBA CONNECT DB ------------
                if(str_username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Masukan username anda !", Toast.LENGTH_SHORT).show();
                }
                else if(str_nama.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Masukan nama lengkap anda !", Toast.LENGTH_SHORT).show();
                }
                else if(str_alamat.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Masukan alamat anda !", Toast.LENGTH_SHORT).show();
                }
                else if(str_telepon.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Masukan nomor telepon anda !", Toast.LENGTH_SHORT).show();
                }
                else if(str_email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Masukan email anda !", Toast.LENGTH_SHORT).show();
                }
                else if(str_password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Masukan password anda !", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.PHONE.matcher(str_telepon).matches()){
                    Toast.makeText(getApplicationContext(), "Format telepon tidak sesuai !", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
                    Toast.makeText(getApplicationContext(), "Format email tidak sesuai !", Toast.LENGTH_SHORT).show();
                }
                else if(str_password.equals(str_password2)) {
                    RequestParams params = new RequestParams();
                    params.put("username", str_username);
                    params.put("email", str_email);
                    params.put("password", str_password);
                    params.put("nama_donatur", str_nama);
                    params.put("notlp_donatur", str_telepon);
                    params.put("alamat_donatur", str_alamat);

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setTimeout(5000);
                    RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/register.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            if (response.equalsIgnoreCase("usernamesama")) {
                                Toast.makeText(getApplicationContext(), "Username telah terpakai !", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.equalsIgnoreCase("emailsama")){
                                Toast.makeText(getApplicationContext(), "Email telah terpakai !", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.equalsIgnoreCase("benar")){
                                final ProgressDialog mDialog = new ProgressDialog(sign_up.this);
                                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                mDialog.setMessage("Loading... ");
                                mDialog.show();
                                Toast.makeText(getApplicationContext(), "Sukses Daftar !", Toast.LENGTH_SHORT).show();
                                //SharedPreferences.Editor editor = getSharedPreferences("masuk", MODE_PRIVATE).edit();
                                //editor.apply();
                                Intent intent = new Intent(sign_up.this, sign_in.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            Toast.makeText(getApplicationContext(), "Gagal Mendaftar !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Konfirmasi password tidak sesuai !" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(sign_up.this, sign_in.class); //Sementara -------------------
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(sign_up.this, sign_in.class); //Sementara -------------------
        startActivity(i);
    }
}
