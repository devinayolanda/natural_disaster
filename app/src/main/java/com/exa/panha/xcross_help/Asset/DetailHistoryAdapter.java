package com.exa.panha.xcross_help.Asset;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.panha.xcross_help.Entity.DetailHistory;
import com.exa.panha.xcross_help.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryHolder> {
    private List<DetailHistory> data;
    public DetailHistoryAdapter(List<DetailHistory> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public DetailHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.detail_history_item,parent,false);
        return new DetailHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryHolder holder, int position) {

        holder.nama.setText(data.get(position).getNama_barang());
        holder.jumlah.setText(String.valueOf(data.get(position).getJumlah_barang()));
        String imageURL = "http://opensource.petra.ac.id/~m26416042tw/"+data.get(position).getPath();
        Picasso.get().load(imageURL).into(holder.img);
        holder.cardView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
