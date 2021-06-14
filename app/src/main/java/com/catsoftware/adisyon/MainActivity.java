package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Layout nesneleri tanimlandi
    EditText etSiparisSaati, etSurucuNo, etOdemeYontemi, etUcret;
    TextView tvSaat, tvSurucuNo, tvOdemeYontemi, tvUcret;

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
    }
    public void siparisKaydet(View view){
        String saat=etSiparisSaati.getText().toString();
        String surucu=etSurucuNo.getText().toString();
        String odemeYontemi=etOdemeYontemi.getText().toString();
        int ucret=Integer.parseInt( etUcret.getText().toString());
        int kayitOncesiSiparisSayisi=mSharedPreferences.getInt("toplamSiparisSayisi",0);//onceki siparis sayilari kontrol ediliyor
        String siparisOnEki="S"+(kayitOncesiSiparisSayisi+1);//siparis verilerinin essiz key degerleriyle kaydedilmesi icin on ek olusturuldu
        if(!(saat.equals("")&&surucu.equals("")&&odemeYontemi.equals("")&&(ucret==0))){//verilerin bos olup olmadigi kontrol ediliyor
            //Veriler siparis one ekiyle sisteme kaydediliyor
            mSharedPreferences.edit().putString(siparisOnEki+"Saat",saat).apply();
            mSharedPreferences.edit().putString(siparisOnEki+"SurucuNo",surucu).apply();
            mSharedPreferences.edit().putString(siparisOnEki+"OdemeYontemi",odemeYontemi).apply();
            mSharedPreferences.edit().putInt(siparisOnEki+"Ucret",ucret).apply();


            tvSaat.setText(saat);//TODO: test icin yazildi kaldir
            tvSurucuNo.setText(surucu);//TODO: test icin yazildi kaldir
            tvOdemeYontemi.setText(odemeYontemi);//TODO: test icin yazildi kaldir
            tvUcret.setText(ucret);//TODO: test icin yazildi kaldir



        }

    }
}