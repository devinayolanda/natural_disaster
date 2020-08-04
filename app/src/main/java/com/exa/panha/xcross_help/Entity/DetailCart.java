package com.exa.panha.xcross_help.Entity;

public class DetailCart {
    int id_detail_donasi;
    int jumlah_barang;
    String nama_barang, file_path_barang, satuan;

    public int getId_detail_donasi() {
        return id_detail_donasi;
    }

    public void setId_detail_donasi(int id_detail_donasi) {
        this.id_detail_donasi = id_detail_donasi;
    }

    public int getJumlah_barang() {
        return jumlah_barang;
    }

    public void setJumlah_barang(int jumlah_barang) {
        this.jumlah_barang = jumlah_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getFile_path_barang() {
        return file_path_barang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setFile_path_barang(String file_path_barang) {
        this.file_path_barang = file_path_barang;
    }
}
