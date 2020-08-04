package com.exa.panha.xcross_help.Asset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exa.panha.xcross_help.Entity.DetailCart;
import com.exa.panha.xcross_help.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class CartDetailAdapter extends RecyclerView.Adapter<CartDetailHolder> {
    private List<DetailCart> mdata;
    private Context mcontext;
    LayoutInflater inflater;
    public CartDetailAdapter(List<DetailCart> data , LayoutInflater context){
        mdata = data;
        inflater = context;
    }
    @NonNull
    @Override
    public CartDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_detail_item, parent,false);
        return new CartDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartDetailHolder holder, int position) {
        //foto belum diurus
        String imageURL = "http://opensource.petra.ac.id/~m26416042tw/"+mdata.get(position).getFile_path_barang();
        try {
            URL url = new URL(imageURL);
            Picasso.get().load(imageURL).into(holder.imageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        holder.textView.setText(mdata.get(position).getNama_barang());
        holder.textView1.setText(mdata.get(position).getSatuan());
        holder.editText.setText(String.valueOf(mdata.get(position).getJumlah_barang()));
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }


}

class CartDetailHolder extends RecyclerView.ViewHolder{
    AppCompatCheckBox checkBox;
    AppCompatTextView textView, textView1;
    EditText editText;
    AppCompatImageView imageView;
    MaterialButton /*AppCompatImageButton*/ btn_plus, btn_min;
    public CartDetailHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.item);
        textView = (AppCompatTextView) itemView.findViewById(R.id.nama_barang);
        editText = (EditText) itemView.findViewById(R.id.size);
        imageView = (AppCompatImageView) itemView.findViewById(R.id.img_barang_donasi);
        btn_min = (MaterialButton/*AppCompatImageButton*/) itemView.findViewById(R.id.minus);
        btn_plus = (MaterialButton/*AppCompatImageButton*/) itemView.findViewById(R.id.plus);
        textView1 = (AppCompatTextView) itemView.findViewById(R.id.satuan);
    }
}
