package com.catsoftware.adisyon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.catsoftware.adisyon.MainActivity.SON_ISLEM_SAATI;

public class ZamanAsimi extends AppCompatActivity {
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaman_asimi);

        //sharedPreferences ayarlari yapiliyor
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getClass().getName(), Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        long sonDbIslemiSaati=sharedPref.getLong(SON_ISLEM_SAATI,-1);
        String sonIslemZamani=getDate(sonDbIslemiSaati, "dd/MM/yyyy HH:mm");

        //Layout nesneleri ataniyor
        TextView tvSonIslemZamani = findViewById(R.id.tvSonIslemZamani);
        tvSonIslemZamani.setText(sonIslemZamani);
        Button btYeniMesaiBaslat = findViewById(R.id.btYeniMesaiBaslat);
        Button btMesaiyeDevamEt = findViewById(R.id.btMesaiyeDevamEt);

        //butonlara fonksiyon kazandiriliyor
        btYeniMesaiBaslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kullaniciya emin olup olmadigi soruluyor
                AlertDialog.Builder mAlert = new AlertDialog.Builder(ZamanAsimi.this);
                mAlert.setTitle("TÜM SIPARISLER SILINECEK");
                mAlert.setMessage("Tüm siparisleriniz silinecek ve yeni mesai baslatilacak.Bu islem geri alinamaz. Opnayliyor musunuz?");
                mAlert.setPositiveButton("Onayliyorum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        veritabaniSifirla();//TODO: icini doldur
                        editor.putLong(SON_ISLEM_SAATI, MainActivity.guncelZaman());//TODO: tüm islem sayfalarina ekle
                        editor.apply();
                        anaEkranaGit();
                    }
                });
                mAlert.setNegativeButton("Vazgec", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //vazgecildigi icin hicbir islem yapilmiyor

                    }
                });
                mAlert.show();
            }
        });

        btMesaiyeDevamEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putLong(SON_ISLEM_SAATI, MainActivity.guncelZaman());//TODO: tüm islem sayfalarina ekle
                editor.apply();
                anaEkranaGit();
            }
        });


    }

    private void veritabaniSifirla() {

        AppDatabase db = AppDatabase.getDbInstance(this);
        db.clearAllTables();
    }


    private void anaEkranaGit() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
