package com.catsoftware.adisyon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;

import java.util.Random;

public class VeritabaniSifirlamaEkrani extends AppCompatActivity {
    TextView  tvSayi1,tvSayi2;
    EditText etIslemSonucu;
    Button btSilmeyiOnayla, btSilmektenVazgec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veritabani_sifirlama_ekrani);

        //Layout views assigned
        tvSayi1=findViewById(R.id.tvSayi1);
        tvSayi2=findViewById(R.id.tvSayi2);
        etIslemSonucu=findViewById(R.id.etIslemSonucu);
        btSilmeyiOnayla=findViewById(R.id.btSilmeyiOnayla);
        btSilmektenVazgec=findViewById(R.id.btSilmektenVazgec);

        //views updated
        int rdSayi1=rd3haneliSayi();
        int rdSayi2=rd3haneliSayi();
        int dogruCevap=rdSayi1+rdSayi2;
        tvSayi1.setText(""+rdSayi1);
        tvSayi2.setText(""+rdSayi2);
        btSilmeyiOnayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kullanicininCevabi=Integer.parseInt(etIslemSonucu.getText().toString());
                if(kullanicininCevabi==dogruCevap){ //user entered right answer
                    verileriSifirla();

                }else {//falsh or empty answer
                    Toast.makeText(VeritabaniSifirlamaEkrani.this, "Lütfen cevabinizi kontrol edin.", Toast.LENGTH_LONG).show();

                }
            }
        });
        btSilmektenVazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void verileriSifirla() {

        //kullaniciya emin olup olmadigi soruluyor
        AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
        mAlert.setTitle("TUM SIPARISLER SILINECEK");
        mAlert.setMessage("Tüm siparislerin silinmesini onayliyor musunuz? Bu islem geri alinamaz.");
        mAlert.setPositiveButton("Onayliyorum", (dialog, which) -> {

            AppDatabase db = AppDatabase.getDbInstance(this);
            db.clearAllTables();
            Intent intent=new Intent(VeritabaniSifirlamaEkrani.this,MainActivity.class);
            startActivity(intent);


            Toast.makeText(this, "Tüm siparisler silindi.", Toast.LENGTH_LONG).show();
            finish();
        });
        mAlert.setNegativeButton("Vazgec", (dialog, which) -> {
            //vazgecildigi icin hicbir islem yapilmiyor
            Toast.makeText(this, "Hicbir veri silinmedi.", Toast.LENGTH_SHORT).show();
            finish();
        });
        mAlert.show();


    }
    private int rd3haneliSayi() {
        Random random=new Random();
        return random.nextInt(900)+100;

    }
}