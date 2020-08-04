package com.exa.panha.xcross_help.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;

public class forgot_pass extends AppCompatActivity {
    public TextInputEditText txtemail;
    private String str_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        MaterialButton submitforgot= (MaterialButton) findViewById(R.id.btn_forgot);

        submitforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout var_email = (TextInputLayout) findViewById(R.id.forgot_email);
                String str_email = var_email.getEditText().getText().toString();
                //Toast.makeText(forgot_pass.this, str_email, Toast.LENGTH_SHORT).show();
                if(str_email.isEmpty()){
                    Toast.makeText(forgot_pass.this, "Data Tidak Lengkap", Toast.LENGTH_SHORT).show();
                }
                else{
                    RequestParams params = new RequestParams();
                    params.put("email", str_email);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://opensource.petra.ac.id/~m26416042tw/lupapassword.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            if (response.equals("gagalemail")) {
                                Toast.makeText(forgot_pass.this, "Data yang dimasukkan tidak valid", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("berhasil")) {
                                Toast.makeText(getApplicationContext(), "Silahkan Cek Email Anda", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(forgot_pass.this, sign_in.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            //Toast.makeText(forgot_pass.this, "Connection Error !", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Silahkan Cek Email Anda", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(forgot_pass.this, sign_in.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
