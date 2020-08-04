package com.exa.panha.xcross_help.Asset;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exa.panha.xcross_help.Entity.Cart;
import com.exa.panha.xcross_help.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private Toolbar mTopToolBar;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        String url_php = "http://opensource.petra.ac.id/~m26416042tw/fetchcart.php";
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.cart_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        StringRequest request = new StringRequest(Request.Method.POST, url_php, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("DB_Cart", response);
                if (!response.equals("gagal")) {
                    Type collectionType = new TypeToken<List<Cart>>() {
                    }.getType();
                    Gson gson = new GsonBuilder().create();
                    //class untuk isi data;
                    List<Cart> Carts = gson.fromJson(response, collectionType);
                    CartAdapter adapter = new CartAdapter(Carts, container.getContext());
                    recyclerView.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //tolong buat error handle jika isinya kosong;

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //user
                params.put("id",String.valueOf(getArguments().getString("user")));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }};
        MySingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(request);
//        mTopToolBar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
//        mTopToolBar.setTitle("Donation Box");
        //set adapter buat cartadapter
        return rootView;
    }

    public void download_data(){

    }
}
