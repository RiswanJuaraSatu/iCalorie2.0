package com.apps.riswanda.icalorie;

public class Users {
    String nama, email, katasandi, usia, jeniskelamin, tanggallahir, tinggi, berat, aktiv, lPinggang;

    public Users() {

    }

    public Users(String nama, String email, String katasandi, String usia, String jeniskelamin, String tanggallahir, String tinggi,String berat, String lPinggang,String aktiv) {
        this.nama = nama;
        this.email = email;
        this.katasandi = katasandi;
        this.usia = usia;
        this.jeniskelamin = jeniskelamin;
        this.tanggallahir = tanggallahir;
        this.tinggi = tinggi;
        this.berat = berat;
        this.lPinggang = lPinggang;
        this.aktiv = aktiv;
    }

    public String getNama() {
        return nama;
    }

    public String getTinggi() {
        return tinggi;
    }

    public String getlPinggang() {
        return lPinggang;
    }

    public String getAktiv() {
        return aktiv;
    }

    public String getBerat() {
        return berat;
    }

    public String getEmail() {
        return email;
    }

    public String getKatasandi() {
        return katasandi;
    }

    public String getUsia() {
        return usia;
    }

    public String getJeniskelamin() {
        return jeniskelamin;
    }

    public String getTanggallahir() {
        return tanggallahir;
    }
}
