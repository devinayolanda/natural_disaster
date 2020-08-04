package com.exa.panha.xcross_help.Asset;

import android.view.View;
import android.widget.LinearLayout;

import com.exa.panha.xcross_help.R;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class DetailHistoryHolder extends RecyclerView.ViewHolder {

    AppCompatTextView nama, jumlah, tanggal, status;
    AppCompatImageView img;
    LinearLayout cardView;
    public DetailHistoryHolder(@NonNull View itemView) {

        super(itemView);
        cardView = (LinearLayout) itemView.findViewById(R.id.itemdetail);
        nama = (AppCompatTextView) itemView.findViewById(R.id.nama_barang);
        jumlah = (AppCompatTextView) itemView.findViewById(R.id.jumlah_barang);
        img = (AppCompatImageView) itemView.findViewById(R.id.img_barang);
    }

}
