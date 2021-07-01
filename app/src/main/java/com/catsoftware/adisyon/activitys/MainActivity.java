package com.catsoftware.adisyon;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //google.com, pub-5791953598568781, DIRECT, f08c47fec0942fa0

    //App Admob ID
    //ca-app-pub-5791953598568781~8354400803

    //Banner Ad ID
    //ca-app-pub-5791953598568781/9782391918

    //Banner Test ID
    //ca-app-pub-3940256099942544/6300978111

    //Interstitial ID
    //ca-app-pub-3940256099942544/1033173712

    RecyclerView recyclerView;

    AppDatabase db;
    private TextView tvToplamSiparisSayisi;
    SharedPreferences sharedPref;
    public static boolean isFirstRun =true;
    public final String IS_FIRST_RUN = "IS_FIRST_RUN";

    private AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor
        tvToplamSiparisSayisi=findViewById(R.id.tvToplamSiparisSayisi);

        //sharedPreferences ayarlari yapiliyor
         sharedPref = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPref = sharedPref.edit();

        //ilk kullanim olup olmadigi kontrol ediliyor

        boolean isFirstRun = sharedPref.getBoolean(IS_FIRST_RUN, true);
        if(isFirstRun){//ilk kullanim
            System.out.println("uygulama ilk defa kullaniliyor");
            isFirstRun=false;
            editorSharedPref.putBoolean(IS_FIRST_RUN, false);
            editorSharedPref.apply();//artik ilk kullanim degil

        }else {//daha once kullanilmis
        deleteOldOrders(this);


        }


        //son kullanim bilgileri kontrol ediliyor



        //recyclerView ayarlaniyor
        recyclerView = findViewById(R.id.recyclerView);

        updateRecyclerView();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(getApplicationContext(),"Ad Loaded",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
       updateRecyclerView();
    }

    public static void deleteOldOrders(Context context) {
        //Zaman bilgileri aliniyor

        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int bugun = c.get(Calendar.DAY_OF_MONTH);
        int buAy = c.get(Calendar.MONTH) + 1;
        int buYil = c.get(Calendar.YEAR);

        //database processes
        AppDatabase db = AppDatabase.getDbInstance(context);
        int beforeDeleteCountOfOldOrders = db.siparisDao().getCountOldOrders(buYil, buAy, bugun);// get the counts before delete
        db.siparisDao().deleteOldYear(buYil);
        db.siparisDao().deleteOldMonth(buAy);
        db.siparisDao().deleteOldDay(bugun);
        int afterDeleteCountOfOldOrders = db.siparisDao().getCountOldOrders(buYil, buAy, bugun);// get the counts after delete

        if (afterDeleteCountOfOldOrders < beforeDeleteCountOfOldOrders) {
            System.out.println("Silinmis toplam kayit sayisi : " +(beforeDeleteCountOfOldOrders-afterDeleteCountOfOldOrders) );

        }
    }


    public void updateRecyclerView(){
        SiparisAdapter siparisAdapter = new SiparisAdapter(this, loadSiparisList(), getClass().getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(siparisAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        int toplamSiparisAdedi = loadSiparisList().size();
        tvToplamSiparisSayisi.setText(""+ toplamSiparisAdedi);

    }








    private List<SiparisSatiri> loadSiparisList() {
        deleteOldOrders(this);
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
            intent = new Intent(MainActivity.this, VeritabaniSifirlamaEkrani.class);
            startActivity(intent);

           //TODO sil//
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
            veritabaniniSisir();
        }
        */


        return super.onOptionsItemSelected(item);
    }

    /*TEST ICIN YAZILDI




    private void veritabaniniSisir() {//test icin yazildi
        db.clearAllTables();

        //Zaman bilgileri aliniyor

        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int bugun = c.get(Calendar.DAY_OF_MONTH);
        int buAy = c.get(Calendar.MONTH) + 1;
        int buYil = c.get(Calendar.YEAR);

        Random random = new Random();
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        double surucu3nakitToplami = 0;
        double surucu5kartToplami=0;
        for (int i = 0; i < 100; i++) {// güncel tarihli veriler

            String rasgeleOdemeYontemi;
            if (random.nextInt(2) == 0) {
                rasgeleOdemeYontemi = "Bar";
            }else{
                rasgeleOdemeYontemi="Online";
            }
            int randomDakika=random.nextInt(60);
            int randomSaat=random.nextInt(24) + 1;
            String randomSurucu=""+(random.nextInt(10)+1);
            double randomUcret = random.nextInt(100)+1;

            int rdSiparisNo=random.nextInt(100)+1;


            SiparisSatiri siparis = new SiparisSatiri();
            siparis.setDakika(randomDakika);
            siparis.setSaat(randomSaat);
            siparis.setSurucu(randomSurucu);
            siparis.setOdemeYontemi(rasgeleOdemeYontemi);
            siparis.setUcret(randomUcret);
            siparis.setSilindiMi(false);
            siparis.setSiparisNo(""+rdSiparisNo);
            siparis.setKayitGunu(bugun);
            siparis.setKayitAyi(buAy);
            siparis.setKayitYili(buYil);
            System.out.println("siparis olusturulma tarihi yil-ay-gun: "+bugun+"-"+buAy+"-"+buYil);
            db.siparisDao().insertSiparis(siparis);//siparisler db ye girdi
            if ((randomSurucu.equals("3")) && (rasgeleOdemeYontemi.equals("Bar"))) {
                surucu3nakitToplami += randomUcret;
            }
            if ((randomSurucu.equals("5")) && (rasgeleOdemeYontemi.equals("Online"))) {
                surucu5kartToplami += randomUcret;
            }






        }
        System.out.println("3.surucu nakit toplami: " + surucu3nakitToplami);
        System.out.println("5.surucu kart toplami: " + surucu5kartToplami);

        for (int i = 0; i < 100; i++) {//rasgele tarihli veriler

            String rasgeleOdemeYontemi;
            if (random.nextInt(2) == 0) {
                rasgeleOdemeYontemi = "Bar";
            }else{
                rasgeleOdemeYontemi="Online";
            }
            int randomDakika=random.nextInt(60);
            int randomSaat=random.nextInt(24) + 1;
            String randomSurucu=""+(random.nextInt(10)+1);
            double randomUcret = random.nextInt(100)+1;
            int rdBugun=random.nextInt(2)+23;
            int rdBuAy=random.nextInt(12)+1;
            int rdBuYil=random.nextInt(7)+2015;
            int rdSiparisNo=random.nextInt(100)+1;


            SiparisSatiri siparis = new SiparisSatiri();
            siparis.setDakika(randomDakika);
            siparis.setSaat(randomSaat);
            siparis.setSurucu(randomSurucu);
            siparis.setOdemeYontemi(rasgeleOdemeYontemi);
            siparis.setUcret(randomUcret);
            siparis.setSilindiMi(false);
            siparis.setSiparisNo(""+rdSiparisNo);
            siparis.setKayitGunu(rdBugun);
            siparis.setKayitAyi(6);//rdBuAy);
            siparis.setKayitYili(2021);//rdBuYil);
            System.out.println("siparis olusturulma tarihi yil-ay-gun: "+rdBuYil+"-"+rdBuAy+"-"+rdBugun);
            db.siparisDao().insertSiparis(siparis);//siparisler db ye girdi

            if ((randomSurucu.equals("3")) && (rasgeleOdemeYontemi.equals("Bar"))) {
                surucu3nakitToplami += randomUcret;
            }
            if ((randomSurucu.equals("5")) && (rasgeleOdemeYontemi.equals("Online"))) {
                surucu5kartToplami += randomUcret;
            }


            System.out.println(randomSaat+":"+randomDakika+" -> "+randomSurucu+" nolu surucu = "+randomUcret+" "+rasgeleOdemeYontemi);



        }

        updateRecyclerView();
    }
    */





}