package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class surucuHesapDokumu extends AppCompatActivity {
    Spinner spSurucuNo;
    EditText etToplamCalismaSaati, etSaatlikUcreti;
    Button btGeriDon, btHesapla;
    TextView tvTeslimEttigiPaketUcreti, tvHakEttigiCalismaUcreti, tvIadeEtmesiGerekenTutar;
    RecyclerView recyclerView;
    LinearLayout linlayHesapSonuclari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surucu_hesap_dokumu);

        //layout nesneleri ataniyor
        spSurucuNo=findViewById(R.id.spSurucuNoHesaplik);
        etToplamCalismaSaati=findViewById(R.id.etToplamCalismaSaati);
        etSaatlikUcreti=findViewById(R.id.etSaatlikUcret);
        btGeriDon=findViewById(R.id.btGeriDon);
        btHesapla=findViewById(R.id.btHesapla);
        linlayHesapSonuclari=findViewById(R.id.linlayHesapSonuclari);
        tvTeslimEttigiPaketUcreti=findViewById(R.id.tvTeslimEttigiPaketUcreti);
        tvHakEttigiCalismaUcreti=findViewById(R.id.tvHakEttigiCalismaUcreti);
        tvIadeEtmesiGerekenTutar=findViewById(R.id.tvIadeEtmesiGerekenTutar);

        //TODO:Recycleviewer detaylarini ekle

        //butonlara fonksiyon veriliyor
        btGeriDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               anaEkranaGit();
            }
        });

        btHesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odemeHesapla(spSurucuNo.getSelectedItem().toString(),Double.parseDouble(etToplamCalismaSaati.getText().toString()),Double.parseDouble(etSaatlikUcreti.getText().toString()));
            }
        })

    }

    private void odemeHesapla(String toString, double parseDouble, double parseDouble1) {//BURADA KALDIM
    }

    public void anaEkranaGit() {
        Intent intent = new Intent(surucuHesapDokumu.this, MainActivity.class);
        startActivity(intent);
    }
}