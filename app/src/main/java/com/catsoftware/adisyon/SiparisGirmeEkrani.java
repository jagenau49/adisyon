package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;

public class SiparisGirmeEkrani extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    public final String KEY_SIPARIS_SAYISI = "toplamSiparisSayisi";
    public final Integer SURUCU_SAYISI=10;
    int kayitOncesiSiparisSayisi;//onceki siparis sayilari kontrol ediliyor


    //Layout nesneleri tanimlandi
    EditText etSiparisSaati, etSurucuNo, etOdemeYontemi, etUcret;
    Button btSiparisKaydet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_girme_ekrani);
        mSharedPreferences = this.getSharedPreferences("com.catsoftware.adisyon", MODE_PRIVATE);

        //Layout nesneleri degiskenlere atandi

        etUcret = findViewById(R.id.etUcret);
        btSiparisKaydet = findViewById(R.id.btKaydet);

        /*
        * Time picker olusturuluyor
        */
        TimePicker picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);

        /*
        SurucuNo icin spinner verileri olusturuluyor
         */
        Spinner spSurucuNolari = (Spinner) findViewById(R.id.spSurucuNo);
        ArrayAdapter<CharSequence> adapterSurucuNo = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.surucu_nolari, R.layout.spinner_item);

        // Apply the adapter to the spinner
        spSurucuNolari.setAdapter(adapterSurucuNo);

        /*
        OdemeYontemi icin spinner verileri olusturuluyor

         */

        Spinner spOdemeYontemi = (Spinner) findViewById(R.id.spOdemeYontemi);
        ArrayAdapter<CharSequence> adapterOdemeYontemi = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.odeme_yontemi, R.layout.spinner_item);

        // Apply the adapter to the spinner
        spOdemeYontemi.setAdapter(adapterOdemeYontemi);






    }

    public void siparisKaydet(View mView) {
        //TODO: saati verinin girildigi saat olarak kaydet
        String saat = etSiparisSaati.getText().toString();
        String surucu = etSurucuNo.getText().toString();
        String odemeYontemi = etOdemeYontemi.getText().toString();
        String ucretGirdisi = etUcret.getText().toString();
        if (!(saat.equals("") && surucu.equals("") && odemeYontemi.equals("") && (ucretGirdisi.equals("")))) {//verilerin bos olup olmadigi kontrol ediliyor

            int ucret = Integer.parseInt(ucretGirdisi);
            kayitOncesiSiparisSayisi = mSharedPreferences.getInt(KEY_SIPARIS_SAYISI, 0);//onceki siparis sayilari kontrol ediliyor
            System.out.println("Kayit oncesi siparis sayisi= " + kayitOncesiSiparisSayisi); //TODO:test icin yazildi sil
            String siparisOnEki = "S" + (kayitOncesiSiparisSayisi + 1);//siparis verilerinin essiz key degerleriyle kaydedilmesi icin on ek olusturuldu

            //Veriler siparis one ekiyle sisteme kaydediliyor
            mSharedPreferences.edit().putString(siparisOnEki + "Saat", saat).apply();
            mSharedPreferences.edit().putString(siparisOnEki + "SurucuNo", surucu).apply();
            mSharedPreferences.edit().putString(siparisOnEki + "OdemeYontemi", odemeYontemi).apply();
            mSharedPreferences.edit().putInt(siparisOnEki + "Ucret", ucret).apply();
            mSharedPreferences.edit().putInt(KEY_SIPARIS_SAYISI, kayitOncesiSiparisSayisi + 1).apply();


        }
        Intent intent = new Intent(SiparisGirmeEkrani.this, MainActivity.class);
        startActivity(intent);


    }

    public void anaEkranaGit(View view) {
        Intent intent = new Intent(SiparisGirmeEkrani.this, MainActivity.class);
        startActivity(intent);
    }
}