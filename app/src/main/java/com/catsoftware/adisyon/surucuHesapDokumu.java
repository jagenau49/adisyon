package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.List;

public class surucuHesapDokumu extends AppCompatActivity {
    public static String statikSurucuNo;
    Spinner spSurucuNo;
    EditText etToplamCalismaSaati, etSaatlikUcreti;
    Button btGeriDon, btHesapla;
    TextView tvTeslimEttigiPaketNakitUcreti,tvTeslimEttigiPaketKartUcreti, tvHakEttigiCalismaUcreti, tvIadeEtmesiGerekenTutar;
    RecyclerView recyclerView;
    LinearLayout linlayHesapSonuclari;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surucu_hesap_dokumu);

        db= AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor

        //layout nesneleri ataniyor
        spSurucuNo=findViewById(R.id.spSurucuNoHesaplik);
        etToplamCalismaSaati=findViewById(R.id.etToplamCalismaSaati);
        etSaatlikUcreti=findViewById(R.id.etSaatlikUcret);
        btGeriDon=findViewById(R.id.btGeriDon);
        btHesapla=findViewById(R.id.btHesapla);
        linlayHesapSonuclari=findViewById(R.id.linlayHesapSonuclari);
        tvTeslimEttigiPaketNakitUcreti =findViewById(R.id.tvTeslimEttigiPaketNakitUcreti);
        tvTeslimEttigiPaketKartUcreti=findViewById(R.id.tvTeslimEttigiPaketKartUcreti);
        tvHakEttigiCalismaUcreti=findViewById(R.id.tvHakEttigiCalismaUcreti);
        tvIadeEtmesiGerekenTutar=findViewById(R.id.tvIadeEtmesiGerekenTutar);

        //recycleviewer ayarlaniyor
        recyclerView=findViewById(R.id.rvHesapDokumuRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //spinnera detaylar veriliyor
        ArrayAdapter<CharSequence> adapterSurucuNo = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.surucu_nolari, R.layout.spinner_item);
        spSurucuNo.setAdapter(adapterSurucuNo);

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
                System.out.println("gelenSurucuNo: "+spSurucuNo.getSelectedItem().toString());
                System.out.println("gelenCalismaSaati bos mu "+etToplamCalismaSaati.getText().toString().equals(""));
                System.out.println("gelenSaatlikUcret: "+etSaatlikUcreti.getText().toString().equals(""));
                if (etToplamCalismaSaati.getText().toString().equals("") || etSaatlikUcreti.getText().toString().equals("")) {//eksik bilgiler var
                    Toast.makeText(surucuHesapDokumu.this,"Sorgulama yapilamadi! Lütfen eksik bilgileri giriniz.",Toast.LENGTH_LONG).show();



                    }else{//bilgiler tam girilmis


statikSurucuNo =spSurucuNo.getSelectedItem().toString();
                    odemeHesapla(spSurucuNo.getSelectedItem().toString(),Double.parseDouble(etToplamCalismaSaati.getText().toString()),Double.parseDouble(etSaatlikUcreti.getText().toString()));

                //klavye gizleniyor
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
            }
        }
        });

    }

    private void odemeHesapla(String surucuNo, double surucuToplamCalismaSaati, double surucuSaatlikUcret) {//
        List<SiparisSatiri> listeSurucununSiparisleri= db.siparisDao().surucununSiparisleriniGetir(surucuNo,false);
        int surucununSiparisSayisi=listeSurucununSiparisleri.size();
        System.out.println(""+surucuNo+".surucunun siparis sayisi : "+surucununSiparisSayisi);
        int surucudeToplananSiparisNakitUcreti=0;
        int surucudeToplananSiparisKartUcreti=0;
        double surucununCalismaUcreti=surucuToplamCalismaSaati*surucuSaatlikUcret;

        for (SiparisSatiri siparisSatir:listeSurucununSiparisleri) {
            if(siparisSatir.getOdemeYontemi().equals("Kart")) {
                surucudeToplananSiparisKartUcreti += siparisSatir.getUcret();
            }else if(siparisSatir.getOdemeYontemi().equals("Nakit")){
                surucudeToplananSiparisNakitUcreti+=siparisSatir.getUcret();
            }
            
        }
        double surucununIadeEtmesiGerekenTutar=surucudeToplananSiparisNakitUcreti-surucununCalismaUcreti;
        tvTeslimEttigiPaketNakitUcreti.setText(surucudeToplananSiparisNakitUcreti+"€");
        tvTeslimEttigiPaketKartUcreti.setText(surucudeToplananSiparisKartUcreti+"€");
        tvHakEttigiCalismaUcreti.setText(surucununCalismaUcreti+" €");
        tvIadeEtmesiGerekenTutar.setText(surucununIadeEtmesiGerekenTutar+" €");

        //recycleviewer uyarlaniyor
        SiparisAdapter siparisAdapter= new SiparisAdapter(this,listeSurucununSiparisleri,"surucuHesapDokumu");
        recyclerView.setAdapter(siparisAdapter);

        //sonuclar gorunur yapiliyor
        linlayHesapSonuclari.setVisibility(View.VISIBLE);


    }

    public void anaEkranaGit() {
        Intent intent = new Intent(surucuHesapDokumu.this, MainActivity.class);
        startActivity(intent);
    }
}