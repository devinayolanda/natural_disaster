package com.exa.panha.xcross_help.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class History implements Parcelable {
    private int id_donasi;
    private int id_donatur;
    private String tanggal_donasi;
    private int id_posko;
    private int status_donasi;
    private String kode_donasi;
    private int status_pickup;
    private String nama_bencana_alam;
    private String nama_posko;
    private String kota_bencana_alam;

    public String getNama_bencana() {
        return nama_bencana_alam+ " "+ kota_bencana_alam;
    }
    public String getNama_posko() {
        return nama_posko;
    }
    public int getId_donasi() {
        return id_donasi;
    }

    public int getId_donatur() {
        return id_donatur;
    }

    public String getTanggal_donasi() {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(tanggal_donasi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        String dateTime = dateFormat.format(date);
        return dateTime;
    }
    private Date stringToDate(String aDate, String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
    public int getId_posko() {
        return id_posko;
    }

    public String getwarnastatus(){
        if(status_donasi==0)
            return "#f3f556";
        else if(status_donasi==1)
            return "#26A9E1";
        else if(status_donasi==2)
            return "#26A9E1";
        else
            return "#26A9E1";
    }
    public String getStatus_donasi() {
        if(status_donasi==0)
        return "Pick up";
        else if(status_donasi==1)
            return "Delivering";
        else if(status_donasi==2)
            return "Delivered";
        else
           return "Not Valid";
    }

    public String getKode_donasi() {
        return kode_donasi;
    }

    public int getStatus_pickup() {
        return status_pickup;
    }

    public static final Creator CREATOR = new Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new News[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    public History(Parcel in){
        this.kode_donasi = in.readString();
        this.tanggal_donasi=in.readString();
        this.id_donasi=in.readInt();
        this.status_donasi=in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode_donasi);
        dest.writeString(this.tanggal_donasi);
        dest.writeInt(this.id_donasi);
        dest.writeInt(this.status_donasi);
    }


}

