package com.exa.panha.xcross_help.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.exa.panha.xcross_help.Entity.News;
import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class detail_news extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.detail_news_toolbar);
        myChildToolbar.setTitle("NEWS");
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        final News isi = (News) getIntent().getParcelableExtra("berita");
        AppCompatTextView tv_title = (AppCompatTextView) findViewById(R.id.title_card),
                tv_content = (AppCompatTextView)findViewById(R.id.desc_card);
        AppCompatImageView img = (AppCompatImageView) findViewById(R.id.img_detail_news);
        MaterialButton btn_donate = (MaterialButton) findViewById(R.id.btn_donate),
                btn_location = (MaterialButton) findViewById(R.id.btn_loc),
                btn_detail = (MaterialButton) findViewById(R.id.btn_detail_keb);
        tv_title.setText(isi.getNama_bencana_alam());
        tv_content.setText(isi.getDetail_bencana_alam());

        String imageURL = "http://opensource.petra.ac.id/~m26416042tw/"+isi.getPath();
        System.out.println(imageURL);
        try {
            URL url = new URL(imageURL);
            Picasso.get().load(imageURL).into(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_news.this, maps_activity.class);
                intent.putExtra("id_berita", isi.getId_bencana_alam());
                intent.putExtra("area_bencana", isi.getKota_bencana_alam());
                startActivity(intent);
            }
        });
        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_news.this, donasi.class);
                // tolong tambahkan extra untuk ngambil data dari id_berita/id_bencana
                intent.putExtra("id_bencana_alam", isi.getId_bencana_alam());
                startActivity(intent);
            }
        });
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_news.this, list_posko.class);
                // tolong tambahkan extra untuk ngambil data dari id_berita/id_bencana
                intent.putExtra("id_posko",String.valueOf(isi.getId_bencana_alam()));
                startActivity(intent);
            }
        });
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
