package com.exa.panha.xcross_help.Asset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exa.panha.xcross_help.Entity.Kebutuhan;
import com.exa.panha.xcross_help.Entity.Posko;
import com.exa.panha.xcross_help.R;

import java.util.List;

import androidx.core.widget.ImageViewCompat;

public class ListBarangAdapter extends BaseAdapter {
    List<Kebutuhan> kebutuhan;
    Context context;
    private static LayoutInflater inflater = null;

    public ListBarangAdapter(Context javalist, List<Kebutuhan> kebutuhan) {
        context = javalist;
        this.kebutuhan = kebutuhan;
//        imageId = gambarlist;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return kebutuhan.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Isi {
        TextView tv;
        TextView tv2;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Isi isi = new Isi();
        Integer demand = kebutuhan.get(position).getStock_barang() - kebutuhan.get(position).getJumlah_barang_min();
        View BarisList;
        BarisList = inflater.inflate(R.layout.itembarang, null);
        isi.tv = (TextView) BarisList.findViewById(R.id.nama_barang);
        isi.tv2 = (TextView) BarisList.findViewById(R.id.dibutuhkan);
        isi.img = (ImageView) BarisList.findViewById(R.id.img_barang);

        isi.tv.setText(kebutuhan.get(position).getNama_barang());
        isi.tv2.setText(String.valueOf(demand));
        //isi.img.setImageResource(imageId[position]);
        return BarisList;
    }
}
