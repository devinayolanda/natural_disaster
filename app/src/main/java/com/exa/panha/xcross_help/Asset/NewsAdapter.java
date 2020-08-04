package com.exa.panha.xcross_help.Asset;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.panha.xcross_help.Activity.detail_news;
import com.exa.panha.xcross_help.Entity.News;
import com.exa.panha.xcross_help.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> implements View.OnClickListener {
    private List<News> data;
    public NewsAdapter(List<News> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_news,parent,false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsHolder holder, final int position) {

        holder.tv_title.setText(data.get(position).getNama_bencana_alam()+" "+data.get(position).getKota_bencana_alam());
       // holder.tv_desc.setText(data.get(position).getIsi_berita());
        //System.out.println(data.get(position).getPath());
        String imageURL = "http://opensource.petra.ac.id/~m26416042tw/"+data.get(position).getPath();
        try {
            URL url = new URL(imageURL);
            Picasso.get().load(imageURL).into(holder.img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        holder.cardView.setOnClickListener(this);
        holder.cardView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        NewsHolder view_holder = (NewsHolder) v.getTag();
        int mPosition = view_holder.getPosition();;
        Intent intent = new Intent(v.getContext(),detail_news.class);
        intent.putExtra("berita",data.get(mPosition));
        v.getContext().startActivity(intent);
    }
}
