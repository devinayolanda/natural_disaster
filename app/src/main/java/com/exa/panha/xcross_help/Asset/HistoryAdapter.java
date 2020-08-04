package com.exa.panha.xcross_help.Asset;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.panha.xcross_help.Activity.detail_history;
import com.exa.panha.xcross_help.Entity.History;
import com.exa.panha.xcross_help.R;

import java.io.ByteArrayInputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> implements View.OnClickListener {
    private List<History> data;View view;
    public HistoryAdapter(List<History> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.history_item,parent,false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryHolder holder, final int position) {
//        byte[] byteArray = Base64.decode(data.get(position).get, Base64.DEFAULT);
//        ByteArrayInputStream imageStream = new ByteArrayInputStream(
//                byteArray);
//        Bitmap theImage = BitmapFactory.decodeStream(new FlushedInputStream(imageStream));


        holder.nama.setText(data.get(position).getNama_bencana());
        holder.nama_posko.setText(data.get(position).getNama_posko());
        holder.tanggal.setText(data.get(position).getTanggal_donasi());
        holder.status.setText(String.valueOf(data.get(position).getStatus_donasi()));
//        holder.status.setBackgroundColor(Integer.parseInt("#26A9E1"));
//        holder.status.setSupportButtonTintList(ContextCompat.getColorStateList(Activity.this, R.color.colorPrimary));
//
//
//        holder.status.setBackgroundTintList(contextInstance.getResources().getColorStateList(R.color.your_xml_name));
        //  holder.img.setBackgroundResource(R.drawable.logo);
//
//        holder.tv_title.setText(data.get(position).getNama_berita());
//        holder.tv_desc.setText(data.get(position).getIsi_berita());
//        holder.img.setBackgroundResource(R.drawable.logo);
        holder.cardView.setOnClickListener(this);
        holder.cardView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        HistoryHolder hold = (HistoryHolder) v.getTag();
        int mPos = hold.getPosition();
        Intent intent = new Intent(v.getContext(),detail_history.class);
        intent.putExtra("hist", data.get(mPos));
        v.getContext().startActivity(intent);
    }
}