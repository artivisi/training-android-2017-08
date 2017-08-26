package com.artivisi.pelatihan.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by opaw on 8/26/17.
 */

public class Peserta implements Serializable{
    private String id;
    private String nama;
    private String email;
    private Date tanggalLahir;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    @Override
    public String toString() {
        return nama + " - " + email;
    }
}
