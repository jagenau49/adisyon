package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.List;

public class SiparisGirmeEkrani extends AppCompatActivity {
    AppDatabase db;
    Boolean duzenlemeMi;
    int duzenlenecekSiparisId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_girme_ekrani);

        db=AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor

        //intent verileri cekiliyor
        Intent intent=getIntent();
        duzenlemeMi=intent.getBooleanExtra(SiparisAdapter.DUZENLEME_MI,false);
         duzenlenecekSiparisId=intent.getIntExtra(SiparisAdapter.SIPARIS_ID,-1);

        //Layout nesneleri tanimlandi ve atandi
        final EditText etUcret= findViewById(R.id.etUcret);
        Button btSiparisKaydet= findViewById(R.id.btKaydet);
        Button btVazgec=findViewById(R.id.btVazgec);
        TimePicker picker =findViewById(R.id.timePicker1);
        Spinner spSurucuNolari = findViewById(R.id.spSurucuNo);
        Spinner spOdemeYontemi=findViewById(R.id.spOdemeYontemi);

        if (duzenlemeMi) {//duzenleme yapmak icin acildiysa
        //duzenlenecek siparisin verileri cekiliyor
            List<SiparisSatiri> listIdDetay= db.siparisDao().siparisDetayGetir(duzenlenecekSiparisId);
            int duzenlenecekSaat=listIdDetay.get(0).getSaat();
            int duzenlenecekDakika=listIdDetay.get(0).getDakika();
            String duzenlenecekSurucu=listIdDetay.get(0).getSurucu();
            String duzenlenecekOdemeYontemi=listIdDetay.get(0).getOdemeYontemi();
            int idOdemeYontemi=-1;
            if (duzenlenecekOdemeYontemi.equals("Nakit")){
                idOdemeYontemi=0;
            }else{
                idOdemeYontemi=1;
            }
            double duzenlenecekUcret=listIdDetay.get(0).getUcret();

            //layout nesneleri duzenlenecek verilere gore guncelleniyor
            btSiparisKaydet.setText("Degisiklikleri Kaydet");
            pickerSetDakika(picker,duzenlenecekDakika);
            pickerSetSaat(picker,duzenlenecekSaat);
            spSurucuNolari.setSelection(Integer.parseInt(duzenlenecekSurucu)-1);//TODO: bu satir dogru calismiyor
            spOdemeYontemi.setSelection(idOdemeYontemi);//TODO: bu satir dogru calismiyor
            etUcret.setText(""+duzenlenecekUcret);



        }


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

                      siparisKaydet(duzenlemeMi,pickerGetSaat(picker), pickerGetDakika(picker), spSurucuNolari.getSelectedItem().toString(), spOdemeYontemi.getSelectedItem().toString(), etUcret.getText().toString());
                  //klavye gizleniyor
                  InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                  inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                  }
          });

          btVazgec.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  anaEkranaGit();
              }
          });


    }
    public int pickerGetSaat(TimePicker timePicker){
        if (Build.VERSION.SDK_INT >= 23 ){
            return timePicker.getHour();

        }
        else{
            return timePicker.getCurrentHour();

        }
    }
    public int pickerGetDakika(TimePicker timePicker){
        if (Build.VERSION.SDK_INT >= 23 ){
            return timePicker.getMinute();

        }
        else{
            return timePicker.getCurrentMinute();

        }
    }
    public void pickerSetSaat(TimePicker timePicker, int saat){
        if (Build.VERSION.SDK_INT >= 23 ){
            timePicker.setHour(saat);

        }
        else{
            timePicker.setCurrentHour(saat);

        }

    }
    public void pickerSetDakika(TimePicker timePicker,int dakika){
        if (Build.VERSION.SDK_INT >= 23 ){
            timePicker.setMinute(dakika);

        }
        else{
            timePicker.setCurrentMinute(dakika);

        }
    }





    public void siparisKaydet(boolean duzenlemeMi, int saat, int dakika, String surucuNo, String odemeYontemi, String stUcret) {




        if ((surucuNo.equals("") || odemeYontemi.equals("") || (stUcret.equals("")))) {//eksik bilgiler varsa
            //Eksik veriler var
            Toast.makeText(SiparisGirmeEkrani.this,"Siparisiniz kaydedilemedi! Lütfen eksik bilgileri giriniz.",Toast.LENGTH_LONG).show();
        }else{//tüm bilgiler girilmis
            Double ucret=Double.parseDouble(stUcret);//ucret islemlerde kullanilabilmek icin double a cevriliyor
            db = AppDatabase.getDbInstance(this.getApplicationContext());
            SiparisSatiri siparis = new SiparisSatiri();
            siparis.setDakika(dakika);
            siparis.setSaat(saat);
            siparis.setSurucu(surucuNo);
            siparis.setOdemeYontemi(odemeYontemi);
            siparis.setUcret(ucret);
            siparis.setSilindiMi(false);
if(duzenlemeMi){//tablodaki veri duzeltilecek
    db.siparisDao().guncelleSiparis(duzenlenecekSiparisId,saat,dakika,surucuNo,odemeYontemi,ucret);
}else {//tabloya yeni veri eklenecek
    db.siparisDao().insertSiparis(siparis);

}


            anaEkranaGit();





        }






    }



    public void anaEkranaGit() {
        Intent intent = new Intent(SiparisGirmeEkrani.this, MainActivity.class);
        startActivity(intent);
    }
}