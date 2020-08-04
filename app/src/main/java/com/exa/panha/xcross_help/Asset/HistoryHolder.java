package com.exa.panha.xcross_help.Asset;

import android.view.View;

import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryHolder extends RecyclerView.ViewHolder {
    AppCompatTextView nama, nama_posko, tanggal, bstatus;
    MaterialCardView cardView;
    MaterialButton status;

    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        cardView = (MaterialCardView) itemView.findViewById(R.id.card_history_adapter);
        nama = (AppCompatTextView) itemView.findViewById(R.id.nama_bencana);
        nama_posko = (AppCompatTextView) itemView.findViewById(R.id.nama_posko);
        tanggal = (AppCompatTextView) itemView.findViewById(R.id.tanggal_donasi);
        status = (MaterialButton) itemView.findViewById(R.id.bstatus);
    }
}
