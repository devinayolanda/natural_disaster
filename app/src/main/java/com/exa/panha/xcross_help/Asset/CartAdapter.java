package com.exa.panha.xcross_help.Asset;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Entity.Cart;
import com.exa.panha.xcross_help.Entity.DetailCart;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class CartAdapter extends RecyclerView.Adapter<CartHolder> implements View.OnClickListener {
    List<Cart> mdata;
    Context mcontext;
    LayoutInflater inflater;
    public CartAdapter(List<Cart> data, Context context){
        mdata = data;
        mcontext = context;
    }
    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cart_item,parent,false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {
        //android volley
        String url_php = "http://opensource.petra.ac.id/~m26416042tw/fetchdetailcart.php";
        StringRequest request = new StringRequest(Request.Method.POST, url_php, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("gagal")) {
                    Log.i("DB_Detail_Cart", response);
                    Type collectionType = new TypeToken<List<DetailCart>>() {
                    }.getType();
                    Gson gson = new GsonBuilder().create();
                    //class untuk isi data;
                    List<DetailCart> detailCarts = gson.fromJson(response, collectionType);
                    CartDetailAdapter adapter = new CartDetailAdapter(detailCarts, inflater);
                    holder.recyclerView.setAdapter(adapter);
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(mdata.get(position).getId_donasi()));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }};
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
        holder.checkBox.setText(mdata.get(position).getNama_posko());
        //holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //tolong diisikan sendiri
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    @Override
    public void onClick(View view) {

    }

}
