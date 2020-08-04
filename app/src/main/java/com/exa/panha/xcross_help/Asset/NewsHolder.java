package com.exa.panha.xcross_help.Asset;

import android.view.View;
import android.widget.LinearLayout;

import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class NewsHolder extends RecyclerView.ViewHolder{
    AppCompatTextView tv_title, tv_desc;
    AppCompatImageView img;
    MaterialCardView cardView;
    public NewsHolder(@NonNull View itemView) {
        super(itemView);
        cardView = (MaterialCardView) itemView.findViewById(R.id.card_news_adapter);
        tv_title = (AppCompatTextView) itemView.findViewById(R.id.title_card_adapter);
        tv_desc = (AppCompatTextView) itemView.findViewById(R.id.content_card_adapter);
        img = (AppCompatImageView) itemView.findViewById(R.id.img_card_adapter);
    }
}
