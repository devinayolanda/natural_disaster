package com.exa.panha.xcross_help.Asset;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.exa.panha.xcross_help.Activity.detail_barang;
import com.exa.panha.xcross_help.Entity.Kebutuhan;
import com.exa.panha.xcross_help.Entity.Posko;
import com.exa.panha.xcross_help.R;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListPoskoAdapter extends RecyclerView.Adapter<Posko_Holder> implements View.OnClickListener {
    LayoutInflater mInflater_posko;
    public static LinkedList<String> nama_posko;
    public static LinkedList<String> isi;
    public static Context context;
    public static Integer[] id;

    public ListPoskoAdapter(Context context,LinkedList<String> nama_posko, LinkedList<String> isi, Integer[] ID) {
        mInflater_posko = LayoutInflater.from(context);
        id = ID;
        this.nama_posko = nama_posko;
        this.isi = isi;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Posko_Holder hold= (Posko_Holder) v.getTag();
        int mpos=hold.getPosition();
        System.out.println(id[mpos]);
        Intent intent = new Intent(v.getContext(),detail_barang.class);
        intent.putExtra("id",id[mpos]);
        v.getContext().startActivity(intent);
    }

    @NonNull
    @Override
    public Posko_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=mInflater_posko.inflate(R.layout.list_posko,parent,false);
        Posko_Holder viewHolder = new Posko_Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Posko_Holder holder, int position) {
        String mCurrent_nama = nama_posko.get(position);
        String mCurrent_isi = isi.get(position);
        holder.bid.setText(mCurrent_nama);
        holder.alamat_posko.setText(mCurrent_isi);
        holder.cr.setOnClickListener(this);
        holder.cr.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return nama_posko.size();
    }
}
