package com.catsoftware.adisyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mSharedPreferences;
    public final String  KEY_SIPARIS_SAYISI="toplamSiparisSayisi";



    //Layout nesneleri tanimlandi


    ListView lvSiparisListesi;
    ArrayAdapter mArrayAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences=this.getSharedPreferences("com.catsoftware.adisyon",MODE_PRIVATE);


        //Layout nesneleri degiskenlere atandi



        lvSiparisListesi=findViewById(R.id.lvSiparisler);





        //siparisler listview seklinde yazdiriliyor
       mArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,siparisleriListele());
        lvSiparisListesi.setAdapter(mArrayAdapter);


//FAB ayrintilari
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Siparis ekleme hazirlaniyor...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(MainActivity.this, SiparisGirmeEkrani.class);
                startActivity(intent);
            }
        });








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.ayarlar_menusu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnSifirla){
            //Tum siparisleri sifirliyoruz
            verileriSifirla();
        }
        return super.onOptionsItemSelected(item);
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
                siparisSatiri+=" -> Surucu: ";
                siparisSatiri+=mSharedPreferences.getString(siparisOnEki+"SurucuNo","Surucu No bulunamadi!");
                siparisSatiri+=" -> ";
                siparisSatiri+=mSharedPreferences.getInt(siparisOnEki+"Ucret",-1);
                siparisSatiri+="€(";
                siparisSatiri+=mSharedPreferences.getString(siparisOnEki+"OdemeYontemi","Odeme Yontemi bulunamadi!");
                siparisSatiri+=")";
                siparisListesi.add(siparisSatiri);


            }

        }



        return siparisListesi;
    }

    public void verileriSifirla(){

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
                Toast.makeText(MainActivity.this,"Hicbir veri silinmedi.",Toast.LENGTH_SHORT).show();
            }
        });
        mAlert.show();

    }



}