package com.catsoftware.adisyon.activities;

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

import com.catsoftware.adisyon.adapters.OrderAdapter;
import com.catsoftware.adisyon.R;
import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.Order;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.initialization.InitializationStatus;


import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //Details for Admob
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
    private TextView tvCountTotalOrders;
    SharedPreferences sharedPref;
    public static boolean isFirstRun =true;
    public final String IS_FIRST_RUN = "IS_FIRST_RUN";
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getDbInstance(this.getApplicationContext());//database assigned
        tvCountTotalOrders =findViewById(R.id.tvCountTotalOrders);

        //sharedPreferences assigned
         sharedPref = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPref = sharedPref.edit();



        //Check: if it is first usage

        boolean isFirstRun = sharedPref.getBoolean(IS_FIRST_RUN, true);
        if(!isFirstRun){//not first usage
            deleteOldOrders(this);

        }else {//first usage

            isFirstRun=false;
            editorSharedPref.putBoolean(IS_FIRST_RUN, isFirstRun);
            editorSharedPref.apply();//isFirst changed as false


        }






        //recyclerView assigned and refreshed
        recyclerView = findViewById(R.id.recyclerView);

        updateRecyclerView();

        //AdMob
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
       updateRecyclerView();// check for updates
    }

    public static void deleteOldOrders(Context context) {
        //get the current time values

        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int currentToday = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentYear = c.get(Calendar.YEAR);

        //database processes
        AppDatabase db = AppDatabase.getDbInstance(context);
        int beforeDeleteCountOfOldOrders = db.orderDao().getCountOldOrders(currentYear, currentMonth, currentToday);// get the counts before delete
        db.orderDao().deleteOldYear(currentYear);
        db.orderDao().deleteOldMonth(currentMonth);
        db.orderDao().deleteOldDay(currentToday);
        int afterDeleteCountOfOldOrders = db.orderDao().getCountOldOrders(currentYear, currentYear, currentToday);// get the counts after delete

        if (afterDeleteCountOfOldOrders < beforeDeleteCountOfOldOrders) {
            System.out.println("Deleted orders: " +(beforeDeleteCountOfOldOrders-afterDeleteCountOfOldOrders) );

        }
    }


    public void updateRecyclerView(){
        OrderAdapter orderAdapter = new OrderAdapter(this, loadOrderList(), getClass().getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(orderAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        int countOrders = loadOrderList().size();

        tvCountTotalOrders.setText(""+ countOrders);

    }








    private List<Order> loadOrderList() {
        deleteOldOrders(this);
        return db.orderDao().getOrders(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_details, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if(item.getItemId()==R.id.mnResetDb) {
            intent = new Intent(MainActivity.this, ResetDBActivity.class);
            startActivity(intent);


        }else if(item.getItemId()==R.id.mnAddOrder) {

            intent = new Intent(MainActivity.this, OrderAddActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnCalculatePayment) {

            intent = new Intent(MainActivity.this, PaymentDriverActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnDeletedOrders){

                intent = new Intent(MainActivity.this, DeletedOrdersActivity.class);
                startActivity(intent);
        }
        /* //created for testing and should be updated for new class names TODO: Update for new class names

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