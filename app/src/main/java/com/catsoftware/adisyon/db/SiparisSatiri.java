package com.catsoftware.adisyon.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "siparislerTablosu")
public class SiparisSatiri implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    public int sId;

    @ColumnInfo(name = "siparisNo")
    public String siparisNo;

    @ColumnInfo(name = "saat")
    public int saat;

    @ColumnInfo(name = "dakika")
    public int dakika;

    @ColumnInfo(name = "surucu")
    public String surucu;

    @ColumnInfo(name = "odemeYontemi")
    public String odemeYontemi;

    @ColumnInfo(name = "ucret")
    public Double ucret;

    @ColumnInfo(name="silindiMi")
    public boolean silindiMi;

    @ColumnInfo(name="kayitGunu")
    public int kayitGunu;

    @ColumnInfo(name="kayitAyi")
    public int kayitAyi;

    @ColumnInfo(name="kayitYili")
    public int kayitYili;

    public void setKayitYili(int kayitYili) {
        this.kayitYili = kayitYili;
    }

    public void setKayitGunu(int kayitGunu) {
        this.kayitGunu = kayitGunu;
    }

    public void setKayitAyi(int kayitAyi) {
        this.kayitAyi = kayitAyi;
    }

    public int getsId() {
        return sId;
    }

    public int getSaat() {
        return saat;
    }

    public void setSaat(int saat) {
        this.saat = saat;
    }

    public int getDakika() {
        return dakika;
    }

    public void setDakika(int dakika) {
        this.dakika = dakika;
    }

    public String getSurucu() {
        return surucu;
    }

    public void setSurucu(String surucu) {
        this.surucu = surucu;
    }

    public String getOdemeYontemi() {
        return odemeYontemi;
    }

    public void setOdemeYontemi(String odemeYontemi) {
        this.odemeYontemi = odemeYontemi;
    }

    public Double getUcret() {
        return ucret;
    }

    public void setUcret(Double ucret) {
        this.ucret = ucret;
    }

    public void setSilindiMi(boolean silindiMi) {
        this.silindiMi = silindiMi;
    }
    public String getSiparisNo() {
        return siparisNo;
    }

    public void setSiparisNo(String siparisNo) {
        this.siparisNo = siparisNo;
    }
}
