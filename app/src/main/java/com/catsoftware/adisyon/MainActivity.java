package com.catsoftware.adisyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.Date;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    AppDatabase db;
    private int toplamSiparisAdedi;
    private TextView tvToplamSiparisSayisi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor
        tvToplamSiparisSayisi=findViewById(R.id.tvToplamSiparisSayisi);
        //sharedPreferences ayarlari yapiliyor
        SharedPreferences sharedPref = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();



        //recyclerView ayarlaniyor
        recyclerView = findViewById(R.id.recyclerView);

        updateRecyclerView();


    }
    public void updateRecyclerView(){
        SiparisAdapter siparisAdapter = new SiparisAdapter(this, loadSiparisList(), getClass().getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(siparisAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        toplamSiparisAdedi=loadSiparisList().size();
        tvToplamSiparisSayisi.setText(""+toplamSiparisAdedi);

    }








    private List<SiparisSatiri> loadSiparisList() {

        return db.siparisDao().siparisleriGetir(false);

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

            verileriSifirla();
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
        mAlert.setPositiveButton("Onayliyorum", (dialog, which) -> {

            AppDatabase db = AppDatabase.getDbInstance(MainActivity.this);
            db.clearAllTables();
            updateRecyclerView();

            Toast.makeText(MainActivity.this, "Tüm siparisler silindi.", Toast.LENGTH_LONG).show();

        });
        mAlert.setNegativeButton("Vazgec", (dialog, which) -> {
            //vazgecildigi icin hicbir islem yapilmiyor
            Toast.makeText(MainActivity.this, "Hicbir veri silinmedi.", Toast.LENGTH_SHORT).show();
        });
        mAlert.show();

    }


}