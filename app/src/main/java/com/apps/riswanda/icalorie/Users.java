package com.apps.riswanda.icalorie;

public class Users {
    String nama;
    String email;
    String katasandi;
    String usia;
    String jeniskelamin;
    String tanggallahir;

    public Users(){

    }

    public Users(String nama, String email, String katasandi, String usia, String jeniskelamin, String tanggallahir) {
        this.nama = nama;
        this.email = email;
        this.katasandi = katasandi;
        this.usia = usia;
        this.jeniskelamin = jeniskelamin;
        this.tanggallahir = tanggallahir;
    }

    public String getNama() {
        return nama;
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
