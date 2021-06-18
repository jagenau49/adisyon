package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

public class SiparisGirmeEkrani extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_girme_ekrani);

        //Layout nesneleri tanimlandi ve atandi
        final EditText etUcret= findViewById(R.id.etUcret);
        Button btSiparisKaydet= findViewById(R.id.btKaydet);
        Button btVazgec=findViewById(R.id.btVazgec);
        TimePicker picker =findViewById(R.id.timePicker1);
        Spinner spSurucuNolari = findViewById(R.id.spSurucuNo);
        Spinner spOdemeYontemi=findViewById(R.id.spOdemeYontemi);


        //Layout nesneleri duzenleniyor
        picker.setIs24HourView(true);//saatler 24 saat duzenine gore ayarlaniyor

        ArrayAdapter<CharSequence> adapterSurucuNo = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.surucu_nolari, R.layout.spinner_item);
        spSurucuNolari.setAdapter(adapterSurucuNo);

        ArrayAdapter<CharSequence> adapterOdemeYontemi = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.odeme_yontemi, R.layout.spinner_item);
        spOdemeYontemi.setAdapter(adapterOdemeYontemi);

          btSiparisKaydet.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  siparisKaydet(pickerSaatCek(picker),pickerDakikaCek(picker),spSurucuNolari.getSelectedItem().toString(),spOdemeYontemi.getSelectedItem().toString(),etUcret.getText().toString());
              }
          });

          btVazgec.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  anaEkranaGit();
              }
          });


    }
    public int pickerSaatCek(TimePicker timePicker){
        if (Build.VERSION.SDK_INT >= 23 ){
            return timePicker.getHour();

        }
        else{
            return timePicker.getCurrentHour();

        }
    }
    public int pickerDakikaCek(TimePicker timePicker){
        if (Build.VERSION.SDK_INT >= 23 ){
            return timePicker.getMinute();

        }
        else{
            return timePicker.getCurrentMinute();

        }
    }





    public void siparisKaydet(int saat,int dakika,String surucuNo,String odemeYontemi, String stUcret ) {


        System.out.println("Butona basildi. Gelen veriler:");//TODO: SIL
        System.out.println("Saat : "+saat);//TODO: SIL
        System.out.println("Dakika : "+dakika);//TODO: SIL
        System.out.println("Surucu No : "+surucuNo);//TODO: SIL
        System.out.println("Odeme Yontemi : "+odemeYontemi);//TODO: SIL
        System.out.println("Tutar : "+stUcret);//TODO: SIL

        if ((surucuNo.equals("") || odemeYontemi.equals("") || (stUcret.equals("")))) {//eksik bilgiler varsa
            //Eksik veriler var
            Toast.makeText(SiparisGirmeEkrani.this,"Siparisiniz kaydedilemedi! Lütfen eksik bilgileri giriniz.",Toast.LENGTH_LONG).show();
        }else{//tüm bilgiler girilmis
            Double ucret=Double.parseDouble(stUcret);//ucret islemlerde kullanilabilmek icin double a cevriliyor

            //veritabani islemleri
            AppDatabase db=AppDatabase.getDbInstance(this.getApplicationContext());
            SiparisSatiri siparis= new SiparisSatiri();
            siparis.setDakika(dakika);
            siparis.setSaat(saat);
            siparis.setSurucu(surucuNo);
            siparis.setOdemeYontemi(odemeYontemi);
            siparis.setUcret(ucret);
            siparis.setSilindiMi(false);
            db.siparisDao().insertSiparis(siparis);

            anaEkranaGit();





        }






    }



    public void anaEkranaGit() {
        Intent intent = new Intent(SiparisGirmeEkrani.this, MainActivity.class);
        startActivity(intent);
    }
}