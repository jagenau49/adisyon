package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.List;

public class silinmisSiparisler extends AppCompatActivity {
    AppDatabase db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silinmis_siparisler);

        db= AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor

        //layout nesneleri ataniyor
        Button btSiparislereGeriDon = findViewById(R.id.btSparislereDon);
        btSiparislereGeriDon.setOnClickListener(v -> anaEkranaGit());

        //recycleviewer ayarlaniyor
        recyclerView=findViewById(R.id.rvSilinmisSiparisler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        AdapterSilinmisSiparisler siparisAdapter= new AdapterSilinmisSiparisler(this,getSilinmisSiparislerListesi());//TODO:diger sinif isimlerine de uygula
        recyclerView.setAdapter(siparisAdapter);

    }

    private List<SiparisSatiri> getSilinmisSiparislerListesi() {

        return db.siparisDao().siparisleriGetir(true);

    }


    public void anaEkranaGit() {
        Intent intent = new Intent(silinmisSiparisler.this, MainActivity.class);
        startActivity(intent);
    }
}
