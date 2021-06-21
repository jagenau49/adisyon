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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.catsoftware.adisyon.ZamanAsimi.getDate;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    AppDatabase db;
    public static final String SON_ISLEM_SAATI = "sonIslemSaati";


    @Override
    protected void onCreate(Bundle savedInstanceState) {//TODO:sharedpreferences sistemiyle gun sonu takibi yap
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor

        //sharedPreferences ayarlari yapiliyor
        SharedPreferences sharedPref = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        long sonDbIslemiSaati = sharedPref.getLong(SON_ISLEM_SAATI, -1);//TODO: DUZELT test icin kapatildi
        System.out.println("MA sp son islem saati: " + sonDbIslemiSaati);
        System.out.println("Test edilen zaman: " + getDate(sonDbIslemiSaati, "dd/MM/yyyy HH:mm"));//TODO: sil

        if (sonDbIslemiSaati == -1) {//uygulama ilk defa aciliyor
            System.out.println("uygulama ilk defa aciliyor.");//TODO:sil
            editor.putLong(SON_ISLEM_SAATI, guncelZaman());//TODO: tüm islem sayfalarina ekle
            editor.apply();

        } else if (zamanFarki(sonDbIslemiSaati) > 5) {//uygulama daha önce acilmis
            System.out.println("5 saatten fazladir islem yapilmamis");//TODO: sil
            //TODO: 5saatten fazladir islem yapilmadi sayfasina gonder
            zamanAsimiSayfasinaGit();
            System.out.println("MainActivity.java satir 59 calisti. zaman farki: " + zamanFarki(sonDbIslemiSaati) + " son islem saati: " + sonDbIslemiSaati);
        }


        //recyclerView ayarlaniyor
        recyclerView = findViewById(R.id.recyclerView);

        updateRecyclerView();


    }
    public void updateRecyclerView(){
        SiparisAdapter siparisAdapter = new SiparisAdapter(this, loadSiparisList(), "MainActivity");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(siparisAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void zamanAsimiSayfasinaGit() {
        Intent intent = new Intent(this, ZamanAsimi.class);
        this.startActivity(intent);
    }

    public static long guncelZaman() {
        //getting the current time in milliseconds, and creating a Date object from it:
        Date date = new Date(System.currentTimeMillis()); //or simply new Date();

//converting it back to a milliseconds representation:
        return date.getTime();
    }

    private int zamanFarki(long kayitliZaman) {
        Date dateKayitliZaman = new Date(kayitliZaman);
        Date dateSuAn = new Date(System.currentTimeMillis());

        long diff = dateSuAn.getTime() - dateKayitliZaman.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        System.out.println("Zaman farki " + hours + " saat");
        return (int) hours;

    }


    private List<SiparisSatiri> loadSiparisList() {

        List<SiparisSatiri> listSiparis = db.siparisDao().siparisleriGetir(false);

        return listSiparis;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ayarlar_menusu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if(item.getItemId()==R.id.mnSifirla) {

            verileriSifirla(); //TODO: güncelle
        }else if(item.getItemId()==R.id.mnSiparisEkle) {

            intent = new Intent(MainActivity.this, SiparisGirmeEkrani.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnHesapDokumu) {

            intent = new Intent(MainActivity.this, surucuHesapDokumu.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnSilinmisSiparisler){

                intent = new Intent(MainActivity.this, silinmisSiparisler.class);
                startActivity(intent);
        }
        /* //TEST ICIN YAZILDI

        else if(item.getItemId()==R.id.mnVeriTabaniniSisir){

        }
        */


        return super.onOptionsItemSelected(item);
    }

    /*private void veritabaniniSisir() {//test icin yazildi
        db.clearAllTables();
        Random random = new Random();
        



        db = AppDatabase.getDbInstance(this.getApplicationContext());
        double surucu3nakitToplami = 0;
        double surucu5kartToplami=0;
        for (int i = 0; i < 100; i++) {

            String rasgeleOdemeYontemi;
            if (random.nextInt(2) == 0) {
                rasgeleOdemeYontemi = "Nakit";
            }else{
                rasgeleOdemeYontemi="Kart";
            }
            int randomDakika=random.nextInt(60);
            int randomSaat=random.nextInt(24) + 1;
            String randomSurucu=""+(random.nextInt(10)+1);
            double randomUcret = random.nextInt(100)+1;

            SiparisSatiri siparis = new SiparisSatiri();
            siparis.setDakika(randomDakika);
            siparis.setSaat(randomSaat);
            siparis.setSurucu(randomSurucu);
            siparis.setOdemeYontemi(rasgeleOdemeYontemi);
            siparis.setUcret(randomUcret);
            siparis.setSilindiMi(false);
            db.siparisDao().insertSiparis(siparis);//siparisler db ye girdi
            if ((randomSurucu.equals("3")) && (rasgeleOdemeYontemi.equals("Nakit"))) {
                surucu3nakitToplami += randomUcret;
            }
            if ((randomSurucu.equals("5")) && (rasgeleOdemeYontemi.equals("Kart"))) {
                surucu5kartToplami += randomUcret;
            }


            System.out.println(randomSaat+":"+randomDakika+" -> "+randomSurucu+" nolu surucu = "+randomUcret+" "+rasgeleOdemeYontemi);



        }
        System.out.println("3.surucu nakit toplami: " + surucu3nakitToplami);
        System.out.println("5.surucu kart toplami: " + surucu5kartToplami);
        updateRecyclerView();
    }*/


    public void verileriSifirla() {

        //kullaniciya emin olup olmadigi soruluyor
        AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
        mAlert.setTitle("TUM SIPARISLER SILINECEK");
        mAlert.setMessage("Tüm siparislerin silinmesini onayliyor musunuz? Bu islem geri alinamaz.");
        mAlert.setPositiveButton("Onayliyorum", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AppDatabase db = AppDatabase.getDbInstance(MainActivity.this);
                db.clearAllTables();
                updateRecyclerView();

                Toast.makeText(MainActivity.this, "Tüm siparisler silindi.", Toast.LENGTH_LONG).show();

            }
        });
        mAlert.setNegativeButton("Vazgec", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //vazgecildigi icin hicbir islem yapilmiyor
                Toast.makeText(MainActivity.this, "Hicbir veri silinmedi.", Toast.LENGTH_SHORT).show();
            }
        });
        mAlert.show();

    }


}