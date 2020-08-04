package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;

public class donasi extends AppCompatActivity {
    private MaterialButton buttonUang, buttonBarang;
    //private Toolbar mTopToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi);
        buttonBarang = (MaterialButton) findViewById(R.id.buttonBarang);
        buttonUang = (MaterialButton) findViewById(R.id.buttonUang);
//        mTopToolBar = (Toolbar) findViewById(R.id.my_toolbar);
//        mTopToolBar.setTitle("Donation");
    }


    public void barang(View view) {
        Intent i = new Intent(donasi.this, donasi_barang.class);
        int id_bencana = getIntent().getIntExtra("id_bencana_alam", -1);
        i.putExtra("id_bencana_alam", id_bencana);
        startActivity(i);
        finish();
    }

    public void uang(View view) {
        Intent i = new Intent(donasi.this, donasi_uang.class);
        int id_bencana = getIntent().getIntExtra("id_bencana_alam", -1);
        i.putExtra("id_bencana_alam", id_bencana);
        startActivity(i);
        finish();
    }
}
