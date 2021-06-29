package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.List;

public class surucununPaketDetaylari extends AppCompatActivity {
    RecyclerView recyclerView;
    List<SiparisSatiri> listeSurucununSiparisleri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surucunun_paket_detaylari);


        //intent ile gelen veriler aliniyor
        Intent i= getIntent();
        String surucuNo=i.getStringExtra(PaymentDriver.SORGULANMIS_SURUCU_NO);
        listeSurucununSiparisleri = (List<SiparisSatiri>)( i.getBundleExtra(PaymentDriver.KEY_LIST).getSerializable(PaymentDriver.SORGU_SONUCU_LISTESI));

        //surucuno ayarlaniyor
        TextView tvSorguDetaySurucuNo=findViewById(R.id.tvSorguDetaySurucuNo);
        tvSorguDetaySurucuNo.setText(surucuNo);

        //button ayarlaniyor
        Button btSorguyaGeriDon=findViewById(R.id.btSorguyaGeriDon);
        btSorguyaGeriDon.setOnClickListener(v -> finish());
updateRecyclerview();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void updateRecyclerview() {

        //recycleviewer ayarlaniyor
        recyclerView = findViewById(R.id.rvHesapDokumuRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(this, listeSurucununSiparisleri, getClass().getName());//classname i iki tarafta da duzelt
        recyclerView.setAdapter(orderAdapter);

    }
}