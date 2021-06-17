package com.catsoftware.adisyon;

import java.util.ArrayList;

public class Siparis {
    private int ucret,saat,dakika;
    private String odemeYontemi,surucu;

    public int getUcret() {
        return ucret;
    }

    public int getSaat() {
        return saat;
    }

    public int getDakika() {
        return dakika;
    }

    public String getOdemeYontemi() {
        return odemeYontemi;
    }

    public String getSurucu() {
        return surucu;
    }

    public void setUcret(int ucret) {
        this.ucret = ucret;
    }

    public void setSaat(int saat) {
        this.saat = saat;
    }

    public void setDakika(int dakika) {
        this.dakika = dakika;
    }

    public void setOdemeYontemi(String odemeYontemi) {
        this.odemeYontemi = odemeYontemi;
    }

    public void setSurucu(String surucu) {
        this.surucu = surucu;

    }

    //TODO:siparis nesneleriyle dolu liste donduren metodu yaz
}
