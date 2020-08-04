package com.exa.panha.xcross_help.Asset;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exa.panha.xcross_help.Activity.MainActivity;
import com.exa.panha.xcross_help.Activity.Profile_Activity;
import com.exa.panha.xcross_help.Activity.sign_in;
import com.exa.panha.xcross_help.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static com.exa.panha.xcross_help.Activity.MainActivity.password_user;
import static com.exa.panha.xcross_help.Activity.MainActivity.email_user;
import static com.exa.panha.xcross_help.Activity.MainActivity.id_donatur;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    SharedPreferences pref;
    private static final String Setting_name = "Log_In";
    SharedPreferences.Editor editor;
    private Toolbar mTopToolBar;
    public String username, password;
    public byte[] byteArray;
    TextView tv_username, tv_name, tv_address, tv_phone, tv_email, tv_shadow;
    public Bitmap fixBitmap;
    CircleImageView iv_picture;
    Button edit,logout;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //username = MainActivity.email_user;
        //password = password_user;

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // ------------ AMBIL USERNAME PASSWORD ID DONATUR -----------------------------
        pref = getActivity().getSharedPreferences(Setting_name, MODE_PRIVATE);
        editor = pref.edit();
        email_user = pref.getString("user", "");
        password_user = pref.getString("password", "");
        id_donatur = pref.getString("id_donatur", "");
        //Toast.makeText(rootView.getContext().getApplicationContext(), "Ini ID Donatur : " + pref.getString("id_donatur", ""), Toast.LENGTH_SHORT).show();
        //editor.commit();
        // -------------------------------------------------------------------
        tv_username = (TextView) rootView.findViewById(R.id.p_username);
        tv_name = (TextView) rootView.findViewById(R.id.p_name);
        tv_address = (TextView) rootView.findViewById(R.id.p_address);
        tv_phone = (TextView) rootView.findViewById(R.id.p_phone);
        tv_email = (TextView) rootView.findViewById(R.id.p_email);
        iv_picture = (CircleImageView) rootView.findViewById(R.id.p_picture);
        tv_shadow = (TextView) rootView.findViewById(R.id.shadow);
        edit = (Button) rootView.findViewById(R.id.editprofile);
        logout = (Button) rootView.findViewById(R.id.btnlogout);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Profile_Activity.class);
                intent.putExtra("username", tv_username.getText().toString());
                intent.putExtra("name", tv_name.getText().toString());
                intent.putExtra("address", tv_address.getText().toString());
                intent.putExtra("phone", tv_phone.getText().toString());
                intent.putExtra("email", tv_email.getText().toString());
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), sign_in.class);
                intent.putExtra("username", "");
                intent.putExtra("name", "");
                username = "";
                password = "";
                pref = getActivity().getSharedPreferences(Setting_name, MODE_PRIVATE);
                editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
                getActivity().finish();
        }
        });
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        //params.put("username", username);
        //params.put("password", password);
        params.put("username", email_user);
        params.put("password", password_user);
        RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/profile.php", params, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                if (response.equalsIgnoreCase("gagal")){
                    Toast.makeText(rootView.getContext().getApplicationContext(), "Data not found.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(rootView.getContext().getApplicationContext(), "Data found.", Toast.LENGTH_SHORT).show();
                    JSONArray obj = null;
                    try{
                        obj = new JSONArray(response);
                        JSONObject node = obj.getJSONObject(0);
                        tv_name.setText(node.getString("nama_donatur"));
                        tv_address.setText(node.getString("alamat_donatur"));
                        tv_phone.setText(node.getString("notlp_donatur"));
                        tv_email.setText(node.getString("email"));
                        tv_username.setText(node.getString("username"));

                        requestHandler rh;
                        String user = tv_username.getText().toString();
                        class getImage extends AsyncTask<String, Void, Bitmap> {
                            @Override
                            protected void onPostExecute(Bitmap b){
                                super.onPostExecute(b);
                                if(b != null){
                                    tv_shadow.setText(b.toString());
                                    iv_picture.setImageBitmap(b);
                                }
                            }
                            @Override
                            protected Bitmap doInBackground(String... strings) {
                                String usr = strings[0];
                                String add = "http://opensource.petra.ac.id/~m26416042tw/loadpicture.php?username="+usr;
                                URL url = null;
                                Bitmap image = null;
                                try {
                                    url = new URL(add);
                                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return image;
                            }
                        }
                        getImage gi = new getImage();
                        gi.execute(user);

                    }
                    catch(JSONException a){
                        Toast.makeText(rootView.getContext().getApplicationContext(), "JSON Failed", Toast.LENGTH_SHORT).show();
                        a.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content){
                Toast.makeText(rootView.getContext().getApplicationContext(), "DB Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

}
