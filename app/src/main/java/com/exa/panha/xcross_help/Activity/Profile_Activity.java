package com.exa.panha.xcross_help.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.exa.panha.xcross_help.Asset.requestHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.exa.panha.xcross_help.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    public EditText name, address, phone, email, password, confirm;
    public TextView username;
    public CircleImageView picture;

    private int PICK_IMAGE_REQUEST = 1;

    Uri imageUri;
    Bitmap fixBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        username = (TextView) findViewById(R.id.p_username);
        name = (EditText) findViewById(R.id.p_name);
        address = (EditText) findViewById(R.id.p_address);
        phone = (EditText) findViewById(R.id.p_phone);
        email = (EditText) findViewById(R.id.p_email);
        password = (EditText) findViewById(R.id.p_password);
        confirm = (EditText) findViewById(R.id.p_confirm);
        picture = (CircleImageView) findViewById(R.id.p_picture);

        Bundle result = getIntent().getExtras();
        username.setText(result.getString("username"));
        //PHOTO
        requestHandler rh;
        String user = username.getText().toString();
        class getImage extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected void onPostExecute(Bitmap b){
                super.onPostExecute(b);
                fixBitmap = b;
                picture.setImageBitmap(b);
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
        //OTHER
        name.setText(result.getString("name"));
        address.setText(result.getString("address"));
        phone.setText(result.getString("phone"));
        email.setText(result.getString("email"));
        password.setText(result.getString("password"));
        confirm.setText(result.getString("password"));
    }

    public void changePicture(View view) {
        showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Photo Gallery"};
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        choosePhotoFromGallery();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);
        //Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(gallery, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST){
                if (data != null && data.getData() != null){
                    imageUri = data.getData();
                    try{
                        fixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        picture.setImageBitmap(fixBitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else if (requestCode == 2){
                fixBitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(fixBitmap);
            }
        }
    }

    public void choosePhotoFromCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 2);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap , Void, String> {

            ProgressDialog loading;
            requestHandler rh = new requestHandler();
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(Profile_Activity.this, "Uploading image", "Plase wait...", true, true);
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading = ProgressDialog.show(Profile_Activity.this, "Upload finish", "OK", true, true);
                loading.dismiss();
                Intent intent = new Intent(Profile_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            protected String doInBackground(Bitmap... bitmaps) {
                fixBitmap = bitmaps[0];
                String uploadImage = getStringImage(fixBitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put("image", uploadImage);
                data.put("username", username.getText().toString());

                String result = rh.sendPostRequest("http://opensource.petra.ac.id/~m26416042tw/changepicture.php", data);

                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(fixBitmap);
    }
    public void saveData(View view) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        Boolean empty = false, notmatch = false;
        params.put("username", username.getText().toString());
        if (name.getText().toString().isEmpty())
            empty = true;
        else
            params.put("name", name.getText().toString());
        if (address.getText().toString().isEmpty())
            empty = true;
        else
            params.put("address", address.getText().toString());
        if (phone.getText().toString().isEmpty())
            empty = true;
        else
            params.put("phone", phone.getText().toString());
        if (email.getText().toString().isEmpty())
            empty = true;
        else
            params.put("email", email.getText().toString());
        if (password.getText().toString().isEmpty() || confirm.getText().toString().isEmpty())
            empty = true;
        else if (!password.getText().toString().equals(confirm.getText().toString())){
            notmatch = true;
            Toast.makeText(this, "New Password is not match", Toast.LENGTH_SHORT).show();
        }
        else{
            params.put("password", password.getText().toString());
        }

        if (empty || notmatch)
            Toast.makeText(this, "Please fill the empty spaces before you save the changes", Toast.LENGTH_SHORT).show();
        else{
            RequestHandle post = client.post("http://opensource.petra.ac.id/~m26416042tw/changeprofile.php", params, new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String response){
                    if (response.equalsIgnoreCase("gagal")){
                        Toast.makeText(Profile_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Profile_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(int statusCode, Throwable error, String content){
                    Toast.makeText(Profile_Activity.this, "DB Failed" + statusCode, Toast.LENGTH_SHORT).show();
                }
            });
            uploadImage();
        }
    }

    //biar keyboard nya nggak buka terus, malesin soalnya
    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
