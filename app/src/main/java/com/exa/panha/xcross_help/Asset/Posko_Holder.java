package com.exa.panha.xcross_help.Asset;

import android.view.View;
import android.widget.TextView;

import com.exa.panha.xcross_help.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Posko_Holder extends RecyclerView.ViewHolder {
    TextView bid;
    TextView alamat_posko;
    CardView cr;
    public Posko_Holder(@NonNull View itemView) {
        super(itemView);
        bid = itemView.findViewById(R.id.nama_posko);
        alamat_posko = itemView.findViewById(R.id.alamat_posko);
        cr = itemView.findViewById(R.id.card_posko);
    }
}
