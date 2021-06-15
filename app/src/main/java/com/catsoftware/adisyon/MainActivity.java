package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final String  KEY_SIPARIS_SAYISI="toplamSiparisSayisi";
    int kayitOncesiSiparisSayisi;//onceki siparis sayilari kontrol ediliyor

    //Layout nesneleri tanimlandi
    EditText etSiparisSaati, etSurucuNo, etOdemeYontemi, etUcret;
    TextView tvSaat, tvSurucuNo, tvOdemeYontemi, tvUcret, tvSiparisDokumu;
    Button btSiparisKaydet;

    SharedPreferences mSharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences=this.getSharedPreferences("com.catsoftware.adisyon",MODE_PRIVATE);

        //Layout nesneleri degiskenlere atandi
        etSiparisSaati=findViewById(R.id.etSiparisSaati);
        etSurucuNo=findViewById(R.id.etSurucuNo);
        etOdemeYontemi=findViewById(R.id.etOdemeYontemi);
        etUcret=findViewById(R.id.etUcret);
        tvSaat=findViewById(R.id.tvSaat);
        tvSurucuNo=findViewById(R.id.tvSurucuNo);
        tvOdemeYontemi=findViewById(R.id.tvOdemeYontemi);
        tvUcret=findViewById(R.id.tvUcret);
        tvSiparisDokumu=findViewById(R.id.tvSiparisDokumu);
        btSiparisKaydet=findViewById(R.id.btKaydet);
        btSiparisKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siparisKaydet();
            }
        });

        //Kayitli siparisler ekrana yazdiriliyor
        int kayitliSiparisSayisi=mSharedPreferences.getInt(KEY_SIPARIS_SAYISI,0);
        String SiparisDokumu="";
        if (kayitliSiparisSayisi==0){
            SiparisDokumu="KAYITLI SIPARIS BULUNAMADI!";
        }else{
            for (int i=1;i<=kayitliSiparisSayisi;i++){
                String siparisOnEki="S"+i;
                for(int j=0;j<4;j++){
                    SiparisDokumu+=mSharedPreferences.getString(siparisOnEki+"Saat","Saat bulunamadi!");
                    SiparisDokumu+=" ";
                    SiparisDokumu+=mSharedPreferences.getString(siparisOnEki+"SurucuNo","Surucu No bulunamadi!");
                    SiparisDokumu+=" ";
                    SiparisDokumu+=mSharedPreferences.getString(siparisOnEki+"OdemeYontemi","Odeme Yontemi bulunamadi!");
                    SiparisDokumu+=" ";
                    SiparisDokumu+=mSharedPreferences.getInt(siparisOnEki+"Ucret",-1);
                    SiparisDokumu+="/n";
                }
            }

        }
        tvSiparisDokumu.setText(SiparisDokumu);
    }
    public void siparisKaydet(){
        String saat=etSiparisSaati.getText().toString();
        String surucu=etSurucuNo.getText().toString();
        String odemeYontemi=etOdemeYontemi.getText().toString();
        String ucretGirdisi=etUcret.getText().toString();
        if(!(saat.equals("")&&surucu.equals("")&&odemeYontemi.equals("")&&(ucretGirdisi.equals("")))){//verilerin bos olup olmadigi kontrol ediliyor

        int ucret=Integer.parseInt( ucretGirdisi);
        kayitOncesiSiparisSayisi=mSharedPreferences.getInt(KEY_SIPARIS_SAYISI,0);//onceki siparis sayilari kontrol ediliyor
        System.out.println("Kayit oncesi siparis sayisi= "+kayitOncesiSiparisSayisi); //TODO:test icin yazildi sil
        String siparisOnEki="S"+(kayitOncesiSiparisSayisi+1);//siparis verilerinin essiz key degerleriyle kaydedilmesi icin on ek olusturuldu

            //Veriler siparis one ekiyle sisteme kaydediliyor
            mSharedPreferences.edit().putString(siparisOnEki+"Saat",saat).apply();
            mSharedPreferences.edit().putString(siparisOnEki+"SurucuNo",surucu).apply();
            mSharedPreferences.edit().putString(siparisOnEki+"OdemeYontemi",odemeYontemi).apply();
            mSharedPreferences.edit().putInt(siparisOnEki+"Ucret",ucret).apply();
            mSharedPreferences.edit().putInt(KEY_SIPARIS_SAYISI,kayitOncesiSiparisSayisi+1).apply();


            /*
            tvSaat.setText(saat);//TODO: test icin yazildi kaldir
            tvSurucuNo.setText(surucu);//TODO: test icin yazildi kaldir
            tvOdemeYontemi.setText(odemeYontemi);//TODO: test icin yazildi kaldir
            tvUcret.setText(ucret);//TODO: test icin yazildi kaldir


             */


        }

    }
}