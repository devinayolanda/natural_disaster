package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exa.panha.xcross_help.R;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.exa.panha.xcross_help.Activity.MainActivity.email_user;
import static com.exa.panha.xcross_help.Activity.MainActivity.id_donatur;
import static com.exa.panha.xcross_help.Activity.MainActivity.password_user;

public class donasi_barang extends AppCompatActivity {
    public static Integer[] id={};
    public static String[] nama={};
    public static Integer[] idposko={};
    public static String[] posko={};
    public static Integer id_bencana;
    Integer id_barang, id_posko;
    String alamat, kodeqr, id_donatur, idp;
    EditText jb;
    SharedPreferences pref;
    TextInputEditText al, dt, nabar;
    Spinner s, sp;
    Calendar c;
    CheckBox cb;
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf;
    LinearLayout linearLayout;
    AsyncHttpClient client;
    private static final String Setting_name = "Log_In";
    SharedPreferences.Editor editor;
    TextView hidden;
    Spinner Spinner_satuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donasibarang);

        id_bencana = getIntent().getIntExtra("id_bencana_alam", -1);

        pref = getSharedPreferences(Setting_name, MODE_PRIVATE);
        editor = pref.edit();
        id_donatur = pref.getString("id_donatur", "");
        idp = pref.getString("id_posko", "");

        random();
        generate();
        generatePosko();
        generateAlamat();
        generateKode();
        generateIDP();

        s = (Spinner) findViewById(R.id.spinner_goods);
        sp = (Spinner) findViewById(R.id.spinner_dest);
        jb = (EditText) findViewById(R.id.size);
        al = (TextInputEditText) findViewById(R.id.pickup_address);
        cb = (CheckBox) findViewById(R.id.pickup);
        hidden = (TextView) findViewById(R.id.hidden);
        Spinner_satuan = (Spinner) findViewById(R.id.sat);
        //satuan = (EditText) findViewById(R.id.sat);
        nabar = (TextInputEditText) findViewById(R.id.nama_barang1);
        linearLayout = (LinearLayout) findViewById(R.id.pickup_layout);
        nabar.setEnabled(false);
        al.setEnabled(false);
        jb.setText("0");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.satuan_barang));
        Spinner_satuan.setAdapter(adapter);

        Runnable runnable = new Runnable() {
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(donasi_barang.this, android.R.layout.simple_list_item_1, nama);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);

                adapter = new ArrayAdapter<String>(donasi_barang.this, android.R.layout.simple_list_item_1, posko);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adapter);

                al.setText(alamat);

                //hidden.setText(idp);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1500);

        dt = (TextInputEditText) findViewById(R.id.estimation);
        dt.setEnabled(false);
        c = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String format = "yyyy-MM-dd";
                sdf = new SimpleDateFormat(format, Locale.KOREA);
                dt.setText(sdf.format(c.getTime()));
            }
        };

        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(donasi_barang.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()) {
                    al.setEnabled(true);
                    linearLayout.setVisibility(View.VISIBLE);
                    dt.setEnabled(true);
                } else {
                    al.setEnabled(false);
                    linearLayout.setVisibility(View.GONE);
                    dt.setEnabled(false);
                }
            }
        });

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Others")){
                    nabar.setEnabled(true);
                    nabar.setHint("Nama Barang");
                } else {
                    nabar.setEnabled(false);
                    nabar.setHint("");
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void random(){
        Random n = new Random();
        kodeqr = String.valueOf(n.nextInt(1000));
    }

//    public void submit(View view) {
//        String nama_barang = "";
//        for(int i=0; i<nama.length; i++){
//            if((s.getSelectedItem().toString()).matches(nama[i])){
//                id_barang=id[i];
//                nama_barang = nama[i];
//            }
//        }
//        for(int i=0; i<posko.length; i++){
//            if((sp.getSelectedItem().toString()).matches(posko[i])){
//                id_posko=idposko[i];
//            }
//        }
//
//        final int pickup;
//        if(cb.isChecked()){
//            pickup=0;
//            alamat = al.getText().toString();
//            dt.setText(sdf.format(c.getTime()));
//        } else{
//            pickup=1;
//            alamat = "";
//            Date asd = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd");
//            dt.setText(df.format(asd));
//        }
//
//        if(!nama_barang.matches("Others") && (Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==1 && !satuan.getText().toString().matches("")) || (!nama_barang.matches("Others") && Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==0 && !dt.getText().toString().matches("") && !satuan.getText().toString().matches(""))) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setCancelable(true);
//            builder.setTitle("Konfirmasi");
//
//            String message;
//            if (pickup == 0) {
//                message = "Nama Barang : " + nama_barang + "\nJumlah Barang : " + jb.getText().toString() + " " + satuan.getText() + "\nAlamat Pickup : " + alamat + "\nTanggal Pickup : " + dt.getText().toString();
//            } else {
//                message = "Nama Barang : " + nama_barang + "\nJumlah Barang : " + jb.getText().toString() + satuan.getText();
//            }
//
//            builder.setMessage(message);
//            builder.setPositiveButton("Confirm",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            RequestParams params = new RequestParams();
//                            params.put("id_donatur", id_donatur.toString());
//                            params.put("id_posko", id_posko.toString());
//                            params.put("id_barang", id_barang.toString());
//                            params.put("jumlah_barang", jb.getText().toString());
//                            params.put("datee", dt.getText().toString());
//                            params.put("pickup", String.valueOf(pickup));
//                            params.put("alamat_pickup", alamat);
//                            params.put("kodeqr", kodeqr);
//                            params.put("satuan", satuan.getText().toString());
//
//                            client = new AsyncHttpClient();
//                            client.setTimeout(3000);
//
//                            RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/insertdonasi.php", params, new AsyncHttpResponseHandler() {
//                                @Override
//                                public void onSuccess(String response) {
//                                    Toast.makeText(getApplicationContext(), "Berhasil !", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//
//                                @Override
//                                public void onFailure(int statusCode, Throwable error, String content) {
//                                    Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });
//            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//        } else if (nama_barang.matches("Others") && !nabar.getText().toString().matches("") && (Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==1 && !satuan.getText().toString().matches("")) || (nama_barang.matches("Others") && Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==0 && !dt.getText().toString().matches("") && !satuan.getText().toString().matches(""))){
//            nama_barang = nabar.getText().toString();
//            id_barang = 0;
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setCancelable(true);
//            builder.setTitle("Konfirmasi");
//
//            String message;
//            if (pickup == 0) {
//                message = "Nama Barang : " + nama_barang + "\nJumlah Barang : " + jb.getText().toString() + " " + satuan.getText() + "\nAlamat Pickup : " + alamat + "\nTanggal Pickup : " + dt.getText().toString();
//            } else {
//                message = "Nama Barang : " + nama_barang + "\nJumlah Barang : " + jb.getText().toString() + satuan.getText();
//            }
//
//            builder.setMessage(message);
//            builder.setPositiveButton("Confirm",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            RequestParams params = new RequestParams();
//                            params.put("id_donatur", id_donatur.toString());
//                            params.put("id_posko", id_posko.toString());
//                            params.put("id_barang", id_barang.toString());
//                            params.put("jumlah_barang", jb.getText().toString());
//                            params.put("datee", dt.getText().toString());
//                            params.put("pickup", String.valueOf(pickup));
//                            params.put("alamat_pickup", alamat);
//                            params.put("kodeqr", kodeqr);
//                            params.put("satuan", satuan.getText().toString());
//
//                            client = new AsyncHttpClient();
//                            client.setTimeout(3000);
//
//                            RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/insertdonasi.php", params, new AsyncHttpResponseHandler() {
//                                @Override
//                                public void onSuccess(String response) {
//                                    Toast.makeText(getApplicationContext(), "Berhasil !", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//
//                                @Override
//                                public void onFailure(int statusCode, Throwable error, String content) {
//                                    Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });
//            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        } else {
//            Toast.makeText(getApplicationContext(), "Pengisian kurang lengkap!", Toast.LENGTH_SHORT).show();
//        }
//    }

    void generate (){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5000);

        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/inventaris.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                if (response.equalsIgnoreCase("gagal")) {
                    Toast.makeText(getApplicationContext(), "Username & Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        nama = new String[obj.length()+1];
                        id = new Integer[obj.length()+1];
                        int co=0;
                        for(int i=0; i<obj.length(); i++) {
                            JSONObject node = obj.getJSONObject(i);
                            if(!node.getString("nama_barang").matches("uang")){
                                nama[co] = node.getString("nama_barang");
                                id[co] = node.getInt("id_barang");
                                co++;
                            }
                        }
                        nama[co] = "Others";
                        id[co] = 0;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Toast.makeText(getApplicationContext(), "Gagal insert barang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void generatePosko(){
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
                        idposko = new Integer[obj.length()+1];
                        int co=1;
                        posko[0]="Posko Pusat";
                        idposko[0]=0;
                        for(int i=0; i<obj.length(); i++) {
                            JSONObject node = obj.getJSONObject(i);
                            posko[co] = node.getString("nama_posko");
                            idposko[co] = node.getInt("id_posko");
                            co++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Toast.makeText(getApplicationContext(), "Gagal insert posko", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void generateAlamat (){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5000);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/alamat.php", params, new AsyncHttpResponseHandler() {
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
                            if(node.getString("id_donatur").matches(id_donatur)){
                                alamat = node.getString("alamat_donatur");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Toast.makeText(getApplicationContext(), "Gagal insert barang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateKode(){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5000);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/selectkode.php", params, new AsyncHttpResponseHandler() {
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
                            if(node.getString("kodeqr").matches(kodeqr)){
                                random();
                            } else {
                                continue;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Toast.makeText(getApplicationContext(), "Gagal insert barang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambah(View view) {
        int temp=Integer.parseInt(jb.getText().toString()) + 1;
        jb.setText(String.valueOf(temp));
    }

    public void kurang(View view) {
        if(Integer.parseInt(jb.getText().toString()) > 0){
            int temp=Integer.parseInt(jb.getText().toString()) - 1;
            jb.setText(String.valueOf(temp));
        } else {
            jb.setText("0");
        }
    }

    public void submit(View view) {
        final String[] nama_barang = {""};
        for(int i=0; i<nama.length; i++){
            if((s.getSelectedItem().toString()).matches(nama[i])){
                id_barang = id[i];
                nama_barang[0] = nama[i];
            }
        }

        for(int i=0; i<posko.length; i++){
            if((sp.getSelectedItem().toString()).matches(posko[i])){
                id_posko=idposko[i];
            }
        }

        final int pickup;
        if(cb.isChecked()){
            alamat = al.getText().toString();
            dt.setText(sdf.format(c.getTime()));
        } else{
            alamat = "";
            Date asd = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd");
            dt.setText(df.format(asd));
        }

        pickup = 3;
        kodeqr = "";

        if(!nama_barang[0].matches("Others") && (Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==3 ) || (!nama_barang[0].matches("Others") && Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==3 && !dt.getText().toString().matches(""))){
            RequestParams params = new RequestParams();
            params.put("id_donatur", id_donatur);
            params.put("id_posko", id_posko.toString());
            params.put("id_barang", id_barang.toString());
            params.put("jumlah_barang", jb.getText().toString());
            params.put("datee", dt.getText().toString());
            params.put("pickup", String.valueOf(pickup));
            params.put("alamat_pickup", alamat);
            params.put("kodeqr", kodeqr);
            params.put("satuan", Spinner_satuan.getSelectedItem().toString());

            client = new AsyncHttpClient();
            client.setTimeout(3000);

            RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/insertdonasi.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    pref.edit().putString("id_posko", id_posko.toString()).apply();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    //Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (nama_barang[0].matches("Others") && !nabar.getText().toString().matches("") && (Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==3) || (nama_barang[0].matches("Others") && Integer.parseInt(jb.getText().toString())!=0 && !s.getSelectedItem().toString().matches("") && pickup==3 && !dt.getText().toString().matches(""))){
            nama_barang[0] = nabar.getText().toString();
            id_barang = 0;

            final int finalPickup = pickup;
            RequestParams params = new RequestParams();
            params.put("id_donatur", id_donatur);
            params.put("id_posko", id_posko.toString());
            params.put("id_barang", id_barang.toString());
            params.put("jumlah_barang", jb.getText().toString());
            params.put("datee", dt.getText().toString());
            params.put("pickup", String.valueOf(finalPickup));
            params.put("alamat_pickup", alamat);
            params.put("kodeqr", kodeqr);
            params.put("satuan", Spinner_satuan.getSelectedItem().toString());

            client = new AsyncHttpClient();
            client.setTimeout(3000);

            RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/insertdonasi.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(getApplicationContext(), "Berhasil !", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    //Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Pengisian kurang lengkap!", Toast.LENGTH_SHORT).show();
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

//    public void deletecart(){
//        RequestParams params = new RequestParams();
//        params.put("id", id_donatur);
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(5000);
//        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/deletecart.php", params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Throwable error, String content) {
//                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
