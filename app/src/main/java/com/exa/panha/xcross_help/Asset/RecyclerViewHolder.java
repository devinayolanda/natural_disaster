package com.exa.panha.xcross_help.Asset;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.exa.panha.xcross_help.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    TextView nama_barang;
    TextView jumlah_barang;
    ImageView gambar;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        nama_barang = itemView.findViewById(R.id.nama_barang);
        jumlah_barang = itemView.findViewById(R.id.dibutuhkan);
        gambar = itemView.findViewById(R.id.img_barang);
    }
}
