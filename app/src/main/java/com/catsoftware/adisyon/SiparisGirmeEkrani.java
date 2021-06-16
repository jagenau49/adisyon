package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SiparisGirmeEkrani extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    public final String KEY_SIPARIS_SAYISI = "toplamSiparisSayisi";
    public final int NAKIT=0;
    public final int KART=1;
    public final Integer SURUCU_SAYISI=10;
    int kayitOncesiSiparisSayisi;//onceki siparis sayilari kontrol ediliyor



    //Layout nesneleri tanimlandi
    EditText etSiparisSaati, etSurucuNo, etOdemeYontemi, etUcret;
    Button btSiparisKaydet;
    TimePicker picker;
    Spinner spSurucuNolari, spOdemeYontemi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_girme_ekrani);
        mSharedPreferences = this.getSharedPreferences("com.catsoftware.adisyon", MODE_PRIVATE);//TODO:dqlite sonrasi kaldir

        /*
        Veritabani hazirlaniyor
         */








        /*
        Veritabani hazir
         */

        //Layout nesneleri degiskenlere atandi

        etUcret = findViewById(R.id.etUcret);
        btSiparisKaydet = findViewById(R.id.btKaydet);


        /*
        * Time picker olusturuluyor
        */
        picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);

        /*
        SurucuNo icin spinner verileri olusturuluyor
         */
         spSurucuNolari = (Spinner) findViewById(R.id.spSurucuNo);
        ArrayAdapter<CharSequence> adapterSurucuNo = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.surucu_nolari, R.layout.spinner_item);

        // Apply the adapter to the spinner
        spSurucuNolari.setAdapter(adapterSurucuNo);

        /*
        OdemeYontemi icin spinner verileri olusturuluyor

         */

         spOdemeYontemi = (Spinner) findViewById(R.id.spOdemeYontemi);
        ArrayAdapter<CharSequence> adapterOdemeYontemi = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.odeme_yontemi, R.layout.spinner_item);

        // Apply the adapter to the spinner
        spOdemeYontemi.setAdapter(adapterOdemeYontemi);






    }

    public void siparisKaydet(View mView) {
        int saat, dakika;
        double ucret=0.0;
        String surucuNo, odemeYontemi;
/*
kullanicinin girdigi veriler cekiliyor
 */

        if (Build.VERSION.SDK_INT >= 23 ){
            saat = picker.getHour();
            dakika = picker.getMinute();
        }
        else{
            saat = picker.getCurrentHour();
            dakika = picker.getCurrentMinute();
        }

        surucuNo= spSurucuNolari.getSelectedItem().toString();
        odemeYontemi=spOdemeYontemi.getSelectedItem().toString();
        if (!etUcret.getText().toString().equals("")){
            ucret=Double.parseDouble(etUcret.getText().toString());
        }
        System.out.println("Saat : "+saat);//TODO: SIL
        System.out.println("Dakika : "+dakika);//TODO: SIL
        System.out.println("Surucu No : "+surucuNo);//TODO: SIL
        System.out.println("Odeme Yontemi : "+odemeYontemi);//TODO: SIL
        System.out.println("Tutar : "+ucret);//TODO: SIL

        if ((surucuNo.equals("") || odemeYontemi.equals("") || (ucret==0.0))) {//verilerin bos olup olmadigi kontrol ediliyor
            //Eksik veriler var
            Toast.makeText(SiparisGirmeEkrani.this,"Siparisiniz kaydedilemedi! Lütfen eksik bilgileri giriniz.",Toast.LENGTH_LONG).show();
        }else{//TODO: verilerin bos oldugunda gosterilecek toasti ekle
            verileriVeritabaninaYaz(saat,dakika,surucuNo,odemeYontemi,ucret);

            Intent intent = new Intent(SiparisGirmeEkrani.this, MainActivity.class);
            startActivity(intent);

        }






    }

    private void verileriVeritabaninaYaz(int saat, int dakika, String surucuNo, String odemeYontemi, double ucret) {
        try {

            SQLiteDatabase veritabani=this.openOrCreateDatabase("SiparislerVeritabani",MODE_PRIVATE,null);
            veritabani.execSQL("CREATE TABLE IF NOT EXISTS siparisDetaylari (id INTEGER PRIMARY KEY, saat INT, dakika INT, surucu VARCHAR, odemeYontemi VARCHAR, ucret REAL)");
            System.out.println("153.satir calisti");
            veritabani.execSQL("INSERT INTO siparisDetaylari (saat, dakika, surucu, odemeYontemi, ucret) " +
        "VALUES ("+saat+","+dakika+","+surucuNo+","+odemeYontemi+","+ucret+")");

            //veri sorgulaniyor
            Cursor cursor=veritabani.rawQuery("SELECT * FROM siparisDetaylari",null);
            int idIx=cursor.getColumnIndex("id");
            int saatIx=cursor.getColumnIndex("saat");
            int dakikaIx=cursor.getColumnIndex("dakika");
            int surucuIx=cursor.getColumnIndex("surucu");
            int odemeYontemiIx=cursor.getColumnIndex("odemeYontemi");
            int ucretIx=cursor.getColumnIndex("ucret");
            while (cursor.moveToNext()){//TODO:test icin yazmistim
                System.out.println("Id: "+cursor.getString(idIx));
                System.out.println("Saat: "+cursor.getString(saatIx));
                System.out.println("Dakika: "+cursor.getString(dakikaIx));
                System.out.println("Surucu: "+cursor.getString(surucuIx));
                System.out.println("Odeme Yontemi: "+cursor.getInt(odemeYontemiIx));
                System.out.println("Ucret: "+cursor.getDouble(ucretIx));
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void anaEkranaGit(View view) {
        Intent intent = new Intent(SiparisGirmeEkrani.this, MainActivity.class);
        startActivity(intent);
    }
}