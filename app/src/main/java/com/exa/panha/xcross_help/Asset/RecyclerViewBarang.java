package com.exa.panha.xcross_help.Asset;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exa.panha.xcross_help.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class RecyclerViewBarang extends RecyclerView.Adapter<RecyclerViewHolder>{
    LayoutInflater mInflater;
    public static LinkedList<String> Barang;
    public static LinkedList<Integer> Total_Kebutuhan;
    public static  LinkedList<String> Path_Image;
    private final Context context;


    public RecyclerViewBarang(Context context,LinkedList<String> Barang,LinkedList<Integer> Total_Kebutuhan,LinkedList<String> Path_Image){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.Barang = Barang;
        this.Total_Kebutuhan = Total_Kebutuhan;
        this.Path_Image = Path_Image;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=mInflater.inflate(R.layout.itembarang,parent,false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String mCurrent = Barang.get(position);
        Integer mCurrent_total = Total_Kebutuhan.get(position);
        holder.nama_barang.setText(mCurrent);
        holder.jumlah_barang.setText(String.valueOf(mCurrent_total));

        String image = "http://opensource.petra.ac.id/~m26416042tw/"+Path_Image.get(position);
        Log.e("goblok",image);
        Picasso.get().load(image).into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return Barang.size();
    }
}

