package com.exa.panha.xcross_help.Entity;

import android.media.Image;

public class Posko {
    int id_posko, id_bencana_alam, jenis ; /* jenis ini apa? */
    String nama_posko, alamat_posko, keterangan_posko, no_telp_posko;
    //Image foto_posko;

    public String getNama_posko() {
        return nama_posko;
    }

    public int getId_posko() {
        return id_posko;
    }

    public String getAlamat_posko() {
        return alamat_posko;
    }
}
