package com.exa.panha.xcross_help.Entity;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Text;

public class News implements Parcelable {
    private int id_bencana_alam;
    private String nama_bencana_alam;
    private String kota_bencana_alam;
    private String detail_bencana_alam;
    private String foto_bencana_alam;
    private String filepath;
    byte[] image;
    public String getPath() {
        return filepath;
    }
    public String getDetail_bencana_alam() {
        return detail_bencana_alam;
    }

    public int getId_bencana_alam() { return id_bencana_alam; }

    public String getNama_bencana_alam() {
        return nama_bencana_alam;
    }

    public String getKota_bencana_alam() {
        return kota_bencana_alam;
    }

    public String getFoto_bencana_alam() {
        return foto_bencana_alam;
    }

    public static final Creator CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    public News(Parcel in){

        this.id_bencana_alam = in.readInt() ;
        this.nama_bencana_alam= in.readString();
        this.kota_bencana_alam= in.readString();
        this.detail_bencana_alam = in.readString() ;
        this.foto_bencana_alam = in.readString() ;
        this.filepath = in.readString();
        //          this.image= Base64.decode(in.readString(), Base64.DEFAULT);;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id_bencana_alam);
        dest.writeString(this.nama_bencana_alam);
        dest.writeString(this.kota_bencana_alam);
        dest.writeString(this.detail_bencana_alam);
        dest.writeString(this.foto_bencana_alam);
        dest.writeString(this.filepath);
        //dest.writeString(Base64.encodeToString(this.foto_bencana_alam, Base64.DEFAULT));
    }
}
