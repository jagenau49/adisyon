package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.List;

public class surucununPaketDetaylari extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surucunun_paket_detaylari);

        //intent ile gelen veriler aliniyor
        Intent i= getIntent();
        String surucuNo=i.getStringExtra(surucuHesapDokumu.SORGULANMIS_SURUCU_NO);
        List<SiparisSatiri> listeSurucununSiparisleri = (List<SiparisSatiri>)( i.getBundleExtra(surucuHesapDokumu.KEY_LIST).getSerializable(surucuHesapDokumu.SORGU_SONUCU_LISTESI));

        //surucuno ayarlaniyor
        TextView tvSorguDetaySurucuNo=findViewById(R.id.tvSorguDetaySurucuNo);
        tvSorguDetaySurucuNo.setText(surucuNo);

        //button ayarlaniyor
        Button btSorguyaGeriDon=findViewById(R.id.btSorguyaGeriDon);
        btSorguyaGeriDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //recycleviewer ayarlaniyor
        recyclerView = findViewById(R.id.rvHesapDokumuRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SiparisAdapter siparisAdapter = new SiparisAdapter(this, listeSurucununSiparisleri, getClass().getName());//classname i iki tarafta da duzelt
        recyclerView.setAdapter(siparisAdapter);
    }
}