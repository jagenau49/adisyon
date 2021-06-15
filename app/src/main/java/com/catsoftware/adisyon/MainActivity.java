package com.catsoftware.adisyon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mSharedPreferences;
    public final String  KEY_SIPARIS_SAYISI="toplamSiparisSayisi";
    int kayitOncesiSiparisSayisi;//onceki siparis sayilari kontrol ediliyor
    ArrayAdapter mArrayAdapter;

    //Layout nesneleri tanimlandi
    EditText etSiparisSaati, etSurucuNo, etOdemeYontemi, etUcret;
    Button btSiparisKaydet, btSifirla;
    ListView lvSiparisListesi;





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


        btSifirla=findViewById(R.id.btSifirla);
        btSiparisKaydet=findViewById(R.id.btKaydet);
        lvSiparisListesi=findViewById(R.id.lvSiparisler);





        //siparisler listview seklinde yazdiriliyor
       mArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,siparisleriListele());
        lvSiparisListesi.setAdapter(mArrayAdapter);








    }

    private ArrayList siparisleriListele() {
        int kayitliSiparisSayisi=mSharedPreferences.getInt(KEY_SIPARIS_SAYISI,0);
        ArrayList<String> siparisListesi=new ArrayList<String>();
        if (kayitliSiparisSayisi==0){
            Toast.makeText(MainActivity.this,"KAYITLI SIPARIS BULUNAMADI!",Toast.LENGTH_LONG).show();

        }else{
            for (int i=kayitliSiparisSayisi;i>0;i--){
                String siparisOnEki="S"+i;
                String siparisSatiri="";

                siparisSatiri+=mSharedPreferences.getString(siparisOnEki+"Saat","Saat bulunamadi!");
                siparisSatiri+=" ";
                siparisSatiri+=mSharedPreferences.getString(siparisOnEki+"SurucuNo","Surucu No bulunamadi!");
                siparisSatiri+=" ";
                siparisSatiri+=mSharedPreferences.getString(siparisOnEki+"OdemeYontemi","Odeme Yontemi bulunamadi!");
                siparisSatiri+=" ";
                siparisSatiri+=mSharedPreferences.getInt(siparisOnEki+"Ucret",-1);
                siparisListesi.add(siparisSatiri);


            }

        }



        return siparisListesi;
    }

    public void verileriSifirla(View mView){

        //kullaniciya emin olup olmadigi soruluyor
        AlertDialog.Builder mAlert=new AlertDialog.Builder(this);
        mAlert.setTitle("TUM SIPARISLER SILINECEK");
        mAlert.setMessage("Tüm siparislerin silinmesini onayliyor musunuz? Bu islem geri alinamaz.");
        mAlert.setPositiveButton("Onayliyorum", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSharedPreferences.edit().clear().apply();
                //TODO:sifirlama sonrasi mutlaka sharedpreferences da olmasi gerekenleri tekrar ekle
                mArrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,siparisleriListele());
                lvSiparisListesi.setAdapter(mArrayAdapter);
                System.out.println("veriler sifirlandi");//TODO:test icin yazildi sil
                Toast.makeText(MainActivity.this,"Tüm siparisler silindi.",Toast.LENGTH_LONG).show();

            }
        });
        mAlert.setNegativeButton("Vazgec", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //vazgecildigi icin hicbir islem yapilmiyor
                Toast.makeText(MainActivity.this,"Herhangibir degisiklik yapilmadi.",Toast.LENGTH_SHORT).show();
            }
        });
        mAlert.show();

    }


    public void siparisKaydet(View mView1){
        //TODO: saati verinin girildigi saat olarak kaydet
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





        }

        mArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,siparisleriListele());
        lvSiparisListesi.setAdapter(mArrayAdapter);
    }
}