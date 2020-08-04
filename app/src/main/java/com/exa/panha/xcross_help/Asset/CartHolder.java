package com.exa.panha.xcross_help.Asset;

import android.view.View;

import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

public class CartHolder extends RecyclerView.ViewHolder {
    AppCompatCheckBox checkBox;
    RecyclerView recyclerView;
    MaterialButton button;
    public CartHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.posko);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.cart_detail_view);
        //button = (MaterialButton) itemView.findViewById(R.id.submit);
    }
}
