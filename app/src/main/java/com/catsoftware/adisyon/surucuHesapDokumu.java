package com.catsoftware.adisyon;
//TODO:surucunun saatten gelen kazanimini ayrica goster
//TODO: saatlik ucretin yerini kisalt sonuna € isareti ekle

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.util.Calendar;
import java.util.List;

public class surucuHesapDokumu extends AppCompatActivity {
    public static String statikSurucuNo;
    public static int saatIseBaslama = -1;
    public static int saatIsiBitirme = -1;
    public static int dakikaIseBaslama = -1;
    public static int dakikaIsiBitirme = -1;
    Spinner spSurucuNo;
    EditText etSaatlikUcreti;
    Button btGeriDon, btHesapla;
    TextView tvTeslimEttigiPaketNakitUcreti, tvTeslimEttigiPaketKartUcreti, tvHakEttigiCalismaUcreti, tvIadeEtmesiGerekenTutar, tvIsiBitirmeSaati, tvIseBaslamaSaati,tvSurucununToplamCalismaSaati,tvSurucununTeslimEttigiPaketSayisi;
    RecyclerView recyclerView;
    LinearLayout linlayHesapSonuclari;
    AppDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surucu_hesap_dokumu);

        db = AppDatabase.getDbInstance(this.getApplicationContext());//veritabani baglaniyor

        //layout nesneleri ataniyor
        spSurucuNo = findViewById(R.id.spSurucuNoHesaplik);

        etSaatlikUcreti = findViewById(R.id.etSaatlikUcret);
        btGeriDon = findViewById(R.id.btGeriDon);
        btHesapla = findViewById(R.id.btHesapla);
        linlayHesapSonuclari = findViewById(R.id.linlayHesapSonuclari);
        tvTeslimEttigiPaketNakitUcreti = findViewById(R.id.tvTeslimEttigiPaketNakitUcreti);
        tvTeslimEttigiPaketKartUcreti = findViewById(R.id.tvTeslimEttigiPaketKartUcreti);
        tvHakEttigiCalismaUcreti = findViewById(R.id.tvSurcununCalismaUcreti);
        tvIadeEtmesiGerekenTutar = findViewById(R.id.tvIadeEtmesiGerekenTutar);
        tvIseBaslamaSaati = findViewById(R.id.tvIseBaslamaSaati);
        tvIsiBitirmeSaati = findViewById(R.id.tvIsiBitirmeSaati);
        tvSurucununToplamCalismaSaati=findViewById(R.id.tvSurucununToplamCalismaSaati);
        tvSurucununTeslimEttigiPaketSayisi=findViewById(R.id.tvSurucununTeslimEttigiPaketSayisi);


        //recycleviewer ayarlaniyor
        recyclerView = findViewById(R.id.rvHesapDokumuRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //spinnera detaylar veriliyor
        ArrayAdapter<CharSequence> adapterSurucuNo = ArrayAdapter.createFromResource(this,// Create an ArrayAdapter using the string array and a default spinner layout
                R.array.surucu_nolari, R.layout.spinner_item);
        spSurucuNo.setAdapter(adapterSurucuNo);

        //butonlara fonksiyon veriliyor
        btGeriDon.setOnClickListener(v -> anaEkranaGit());

        btHesapla.setOnClickListener(v -> {
            System.out.println("gelenSurucuNo: " + spSurucuNo.getSelectedItem().toString());//TODO sil

            System.out.println("gelenSaatlikUcret: " + etSaatlikUcreti.getText().toString().equals(""));//TODO sil
            if (saatIseBaslama == 0 || saatIsiBitirme == 0  || etSaatlikUcreti.getText().toString().equals("")) {//eksik bilgiler var
                Toast.makeText(surucuHesapDokumu.this, "Sorgulama yapilamadi! Lütfen eksik bilgileri giriniz.", Toast.LENGTH_LONG).show();


            } else {//bilgiler tam girilmis


                statikSurucuNo = spSurucuNo.getSelectedItem().toString();
                odemeHesapla(spSurucuNo.getSelectedItem().toString(), saatIseBaslama,
                dakikaIseBaslama,  saatIsiBitirme, dakikaIsiBitirme, Double.
                parseDouble(etSaatlikUcreti.getText().toString()));

                //klavye gizleniyor
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        });

    }

    private void odemeHesapla(String surucuNo, int saatIseBaslama, int dakikaIseBaslama, int saatIsiBitirme, int dakikaIsiBitirme, double surucuSaatlikUcret) {//
        List<SiparisSatiri> listeSurucununSiparisleri = db.siparisDao().surucununSiparisleriniGetir(surucuNo, false);
        int surucununSiparisSayisi = listeSurucununSiparisleri.size();
        System.out.println("" + surucuNo + ".surucunun siparis sayisi : " + surucununSiparisSayisi);
        int surucudeToplananSiparisNakitUcreti = 0;
        int surucudeToplananSiparisKartUcreti = 0;
        int surucuToplamCalisma10min=sure10MinHesapla(saatIseBaslama,dakikaIseBaslama,saatIsiBitirme,dakikaIsiBitirme);
        int surucununTeslimEttigiPaketSayisi=listeSurucununSiparisleri.size();
        double surucununCalismaUcreti = ((surucuSaatlikUcret/6.0) * surucuToplamCalisma10min)+ surucununTeslimEttigiPaketSayisi;

        for (SiparisSatiri siparisSatir : listeSurucununSiparisleri) {
            if (siparisSatir.getOdemeYontemi().equals("Kart")) {
                surucudeToplananSiparisKartUcreti += siparisSatir.getUcret();
            } else if (siparisSatir.getOdemeYontemi().equals("Nakit")) {
                surucudeToplananSiparisNakitUcreti += siparisSatir.getUcret();
            }

        }
        double surucununIadeEtmesiGerekenTutar = surucudeToplananSiparisNakitUcreti - surucununCalismaUcreti;
        tvTeslimEttigiPaketNakitUcreti.setText(surucudeToplananSiparisNakitUcreti + "€");
        tvTeslimEttigiPaketKartUcreti.setText(surucudeToplananSiparisKartUcreti + "€");
        tvHakEttigiCalismaUcreti.setText(surucununCalismaUcreti + " €");
        tvIadeEtmesiGerekenTutar.setText(surucununIadeEtmesiGerekenTutar + " €");
        tvSurucununToplamCalismaSaati.setText(calismaSaatiHesapla(surucuToplamCalisma10min));
        tvSurucununTeslimEttigiPaketSayisi.setText(""+surucununSiparisSayisi);


        //recycleviewer uyarlaniyor
        SiparisAdapter siparisAdapter = new SiparisAdapter(this, listeSurucununSiparisleri, "surucuHesapDokumu");
        recyclerView.setAdapter(siparisAdapter);

        //sonuclar gorunur yapiliyor
        linlayHesapSonuclari.setVisibility(View.VISIBLE);


    }

    private String calismaSaatiHesapla(int toplam10MinAdedi) {
        int toplamSaat=toplam10MinAdedi/6;
        int toplamDakika=(toplam10MinAdedi%6)*10;
        return (toplamSaat+" saat "+toplamDakika+" dakika");

    }

    private int sure10MinHesapla(int saatIseBaslama, int dakikaIseBaslama, int saatIsiBitirme, int dakikaIsiBitirme) {
        return ((60-dakikaIseBaslama)+((saatIsiBitirme-saatIseBaslama-1)*60)+dakikaIsiBitirme)/10;
    }

    public void anaEkranaGit() {
        Intent intent = new Intent(surucuHesapDokumu.this, MainActivity.class);
        startActivity(intent);
    }

    public void saatSec(View view) {
        // Get Current Time
        int mHour = simdiSaatKac();
        int mMinute = simdiDakikaKac();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay,
                                          int minute) {
                        if (view.getId() == R.id.btIseBaslamaSaatiSec) {
                            ((TextView) tvIseBaslamaSaati).setText(SiparisAdapter.ikiHaneliOlsun(hourOfDay) + ":" + SiparisAdapter.ikiHaneliOlsun(minute));
                            ((TextView) tvIseBaslamaSaati).setVisibility(View.VISIBLE);
                            saatIseBaslama = hourOfDay;
                            dakikaIseBaslama = minute;
                        } else if (view.getId() == R.id.btIsiBitirmeSaatiSec) {
                            ((TextView) tvIsiBitirmeSaati).setText(SiparisAdapter.ikiHaneliOlsun(hourOfDay) + ":" + SiparisAdapter.ikiHaneliOlsun(minute));
                            ((TextView) tvIsiBitirmeSaati).setVisibility(View.VISIBLE);
                            saatIsiBitirme = hourOfDay;
                            dakikaIsiBitirme = minute;
                        }


                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private int simdiDakikaKac() {
        final Calendar c = Calendar.getInstance();


        return c.get(Calendar.MINUTE);
    }

    private int simdiSaatKac() {
        final Calendar c = Calendar.getInstance();

        return c.get(Calendar.HOUR_OF_DAY);
    }
}