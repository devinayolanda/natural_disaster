package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.exa.panha.xcross_help.Asset.requestHandler;
import com.exa.panha.xcross_help.R;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class donasi_uang extends AppCompatActivity {
    public static Integer[] id={};
    public static String[] posko={};
    TextInputEditText nominal;
    Spinner s;
    Integer id_bencana, id_posko;
    TextView hidden;
    String idp, id_donatur;
    private static final String Setting_name = "Log_In";
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    //dave
    ImageButton uploadCamera, uploadImage;
    ImageView buktiTransfer;
    Bitmap fixBitmap;
    Uri imageUri;
    Integer idDonasi2;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donasiuang);

        //dave
        uploadCamera = (ImageButton) findViewById(R.id.camera);
        uploadImage = (ImageButton) findViewById(R.id.image);
        buktiTransfer = (ImageView) findViewById(R.id.buktiTransfer);
        //

        pref = getSharedPreferences(Setting_name, MODE_PRIVATE);
        editor = pref.edit();
        id_donatur = pref.getString("id_donatur", "");

        id_bencana = getIntent().getIntExtra("id_bencana_alam", -1);

        generate();
        generateIDP();;

        nominal = (TextInputEditText)findViewById(R.id.nominal);
        hidden = (TextView) findViewById(R.id.hidden);
        s = (Spinner) findViewById(R.id.spinner_dest);

        Runnable runnable = new Runnable() {
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(donasi_uang.this, android.R.layout.simple_list_item_1, posko);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);

                hidden.setText(idp);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1500);
    }

    void generate(){
        RequestParams params = new RequestParams();
        params.put("id_bencana_alam", id_bencana.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5000);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/posko2.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                if (response.equalsIgnoreCase("gagal")) {
                    Toast.makeText(getApplicationContext(), "Username & Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        posko = new String[obj.length()+1];
                        id = new Integer[obj.length()+1];
                        int co=1;
                        posko[0]="Posko Pusat";
                        id[0]=0;
                        for(int i=0; i<obj.length(); i++) {
                            JSONObject node = obj.getJSONObject(i);
                            posko[co] = node.getString("nama_posko");
                            id[co] = node.getInt("id_posko");
                            co++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Gagal insert posko", Toast.LENGTH_SHORT).show();
            }
        });

        //dave
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallery();
            }
        });

        uploadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromCamera();
            }
        });
        //
    }

    //dave
    public void choosePhotoFromGallery(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), 1);
    }
    public void choosePhotoFromCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                if (data != null && data.getData() != null){
                    imageUri = data.getData();
                    try {
                        fixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        buktiTransfer.setImageBitmap(fixBitmap);
                        buktiTransfer.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else if (requestCode == 2){
                fixBitmap = (Bitmap) data.getExtras().get("data");
                buktiTransfer.setImageBitmap(fixBitmap);
                buktiTransfer.setVisibility(View.VISIBLE);
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(Integer idd){
        class UploadImage extends AsyncTask<Bitmap, Void, String>{
            requestHandler rh = new requestHandler();
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String a){
                super.onPostExecute(a);
                finish();
            }
            @Override
            protected String doInBackground(Bitmap... bitmaps){
                fixBitmap = bitmaps[0];
                String uploadImage = getStringImage(fixBitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put("image", uploadImage);
                data.put("id_donasi", idDonasi2.toString());

                String result = rh.sendPostRequest("http://opensource.petra.ac.id/~m26416042tw/uploadtransfer.php", data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(fixBitmap);
    }
    //
    public void donate(View view) {
        for(int i=0; i<posko.length; i++){
            if((s.getSelectedItem().toString()).matches(posko[i])){
                id_posko=id[i];
            }
        }

        if(!nominal.getText().toString().matches("") && !s.getSelectedItem().toString().matches("")) {
            Date asd = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd");

            RequestParams params = new RequestParams();
            params.put("id_donatur", id_donatur);
            params.put("id_posko", id_posko.toString());
            params.put("jumlah_barang", nominal.getText().toString());
            params.put("id_barang", "5");
            params.put("datee", (df.format(asd).toString()));
            params.put("pickup", "0");
            params.put("alamat_pickup", "");
            params.put("kodeqr", "-");
            params.put("satuan", "rupiah");

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(3000);

            RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/insertdonasi.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    idDonasi2 = Integer.parseInt(response.trim());
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    //Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
            uploadImage(idDonasi2);
        }
    }

    public void generateIDP(){
        RequestParams params = new RequestParams();
        params.put("id", id_donatur);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5000);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/generateposko.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                if (response.equalsIgnoreCase("gagal")) {
                    Toast.makeText(getApplicationContext(), "Username & Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        for(int i=0; i<obj.length(); i++) {
                            JSONObject node = obj.getJSONObject(i);
                            int idep = node.getInt("id_posko");
                            idp = String.valueOf(idep);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
