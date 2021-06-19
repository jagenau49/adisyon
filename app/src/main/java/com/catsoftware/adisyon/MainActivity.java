package com.catsoftware.adisyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    AppDatabase db;









    @Override
    protected void onCreate(Bundle savedInstanceState) {//TODO:sharedpreferences sistemiyle gun sonu takibi yap
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor

        //sharedPreferences ayarlari yapiliyor
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getClass().getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int sonDbIslemiSaati=sharedPref.getString(get(R.string.sonAcilmaSaati),-1);//TODO: BURADAN DEVAM ET
        switch (sonDbIslemiSaati){
            case -1:
                System.out.println("uygulama ilk defa aciliyor");//TODO:sil
                editor.putInt(getString(String.valueOf(R.string.sonAcilmaSaati), 0);
                editor.apply();
        }

        //recyclerView ayarlaniyor
        recyclerView=findViewById(R.id.recyclerView);
        SiparisAdapter siparisAdapter= new SiparisAdapter(this,loadSiparisList(),"MainActivity");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(siparisAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


    }




    private List<SiparisSatiri> loadSiparisList() {

        List<SiparisSatiri> listSiparis= db.siparisDao().siparisleriGetir(false);

        return  listSiparis;

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
            //verileriSifirla(); //TODO: güncelle
        }else if (item.getItemId()==R.id.mnSiparisEkle){
            //siparis ekleme ekranina gidilecek
            Intent intent=new Intent(MainActivity.this, SiparisGirmeEkrani.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnHesapDokumu){
            Intent intent=new Intent(MainActivity.this, surucuHesapDokumu.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnSilinmisSiparisler){
            Intent intent=new Intent(MainActivity.this, silinmisSiparisler.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    public void verileriSifirla(){

        //kullaniciya emin olup olmadigi soruluyor
        AlertDialog.Builder mAlert=new AlertDialog.Builder(this);
        mAlert.setTitle("TUM SIPARISLER SILINECEK");
        mAlert.setMessage("Tüm siparislerin silinmesini onayliyor musunuz? Bu islem geri alinamaz.");
        mAlert.setPositiveButton("Onayliyorum", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO:güncelle


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