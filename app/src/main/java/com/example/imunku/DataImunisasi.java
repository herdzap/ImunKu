package com.example.imunku;

import android.os.Parcelable;
import android.os.Parcel;

import androidx.annotation.NonNull;

public class DataImunisasi implements Parcelable {
    String uuid;
    String nama;
    int umur;
    int hariLahir;
    int bulanLahir;
    int tahunLahir;
    int hariVaksin;
    int bulanVaksin;
    int tahunVaksin;
    String jenisVaksin;
    int vaksinKe;
    String idGambar;

    protected DataImunisasi(Parcel in) {
        uuid = in.readString();
        nama = in.readString();
        umur = in.readInt();
        hariLahir = in.readInt();
        bulanLahir = in.readInt();
        tahunLahir = in.readInt();
        hariVaksin = in.readInt();
        bulanVaksin = in.readInt();
        tahunVaksin = in.readInt();
        jenisVaksin = in.readString();
        vaksinKe = in.readInt();
        idGambar = in.readString();
    }
    public DataImunisasi() {}


    public DataImunisasi(String namaAnak, int umur, int hariLahir, int bulanLahir, int tahunLahir,
                     int hariVaksin, int bulanVaksin, int tahunVaksin, String jenisVaksin, int vaksinKe, String idGambar) {
        this.nama = namaAnak;
        this.umur = umur;
        this.hariLahir = hariLahir;
        this.bulanLahir = bulanLahir;
        this.tahunLahir = tahunLahir;
        this.hariVaksin = hariVaksin;
        this.bulanVaksin = bulanVaksin;
        this.tahunVaksin = tahunVaksin;
        this.jenisVaksin = jenisVaksin;
        this.vaksinKe = vaksinKe;
        this.idGambar = idGambar;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNamaAnak() {
        return nama;
    }

    public void setNamaAnak(String nama) {
        this.nama = nama;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public int getHariLahir() {
        return hariLahir;
    }

    public void setHariLahir(int hariLahir) {
        this.hariLahir = hariLahir;
    }

    public int getHariVaksin() {
        return hariVaksin;
    }

    public void setHariVaksin(int hariVaksin) {
        this.hariVaksin = hariVaksin;
    }

    public String getJenisVaksin() {
        return jenisVaksin;
    }

    public void setJenisVaksin(String jenisVaksin) {
        this.jenisVaksin = jenisVaksin;
    }

    public int getVaksinKe() {
        return vaksinKe;
    }

    public void setVaksinKe(int vaksinKe) {
        this.vaksinKe = vaksinKe;
    }

    public int getBulanLahir() {
        return bulanLahir;
    }

    public void setBulanLahir(int bulanLahir) {
        this.bulanLahir = bulanLahir;
    }

    public int getTahunLahir() {
        return tahunLahir;
    }

    public void setTahunLahir(int tahunLahir) {
        this.tahunLahir = tahunLahir;
    }

    public int getBulanVaksin() {
        return bulanVaksin;
    }

    public void setBulanVaksin(int bulanVaksin) {
        this.bulanVaksin = bulanVaksin;
    }

    public int getTahunVaksin() {
        return tahunVaksin;
    }

    public void setTahunVaksin(int tahunVaksin) {
        this.tahunVaksin = tahunVaksin;
    }

    public String getIdGambar() {
        return idGambar;
    }

    public void setIdGambar(String idGambar) {
        this.idGambar = idGambar;
    }

    public static final Creator<DataImunisasi> CREATOR = new Creator<DataImunisasi>() {
        @Override
        public DataImunisasi createFromParcel(Parcel in) {
            return new DataImunisasi(in);
        }

        @Override
        public DataImunisasi[] newArray(int size) {
            return new DataImunisasi[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.uuid);
        parcel.writeString(this.nama);
        parcel.writeInt(this.umur);
        parcel.writeInt(this.hariLahir);
        parcel.writeInt(this.bulanLahir);
        parcel.writeInt(this.tahunLahir);
        parcel.writeInt(this.hariVaksin);
        parcel.writeInt(this.bulanVaksin);
        parcel.writeInt(this.tahunVaksin);
        parcel.writeString(this.jenisVaksin);
        parcel.writeInt(this.vaksinKe);
        parcel.writeString(this.idGambar);
    }
}
