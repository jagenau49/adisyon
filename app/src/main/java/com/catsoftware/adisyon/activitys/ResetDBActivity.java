package com.catsoftware.adisyon.activitys;

//TODO: continue to translate

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.R;
import com.catsoftware.adisyon.db.AppDatabase;

import java.util.Random;

public class ResetDBActivity extends AppCompatActivity {
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
        int rdSayi1= rd2haneliSayi();
        int rdSayi2= rd2haneliSayi();
        int dogruCevap=rdSayi1+rdSayi2;
        tvSayi1.setText(""+rdSayi1);
        tvSayi2.setText(""+rdSayi2);
        btSilmeyiOnayla.setOnClickListener(v -> {
            String answerOfQuestion=etIslemSonucu.getText().toString();
            if(answerOfQuestion.equals("")){ //user sent empty answer
                Toast.makeText(ResetDBActivity.this, "Bitte korrigieren Sie Ihre Antwort!", Toast.LENGTH_LONG).show();

            }else{ //user sent an answer
                int kullanicininCevabi=Integer.parseInt(answerOfQuestion);
                if(kullanicininCevabi==dogruCevap){ //user entered right answer
                    verileriSifirla();

                }else {//falsh or empty answer
                    Toast.makeText(ResetDBActivity.this, "Bitte korrigieren Sie Ihre Antwort!", Toast.LENGTH_LONG).show();

                }

            }


        });
        btSilmektenVazgec.setOnClickListener(v -> finish());
    }
    public void verileriSifirla() {

        //kullaniciya emin olup olmadigi soruluyor
        AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
        mAlert.setTitle("Alle Daten werden gelöscht?");
        mAlert.setMessage("Sind Sie sicher? Diese Transaktion kann nicht rückgängig gemacht werden.");
        mAlert.setPositiveButton("Ja", (dialog, which) -> {

            AppDatabase db = AppDatabase.getDbInstance(this);
            db.clearAllTables();
            Intent intent=new Intent(ResetDBActivity.this, MainActivity.class);
            startActivity(intent);


            Toast.makeText(this, "Alle daten wurden gelöscht.", Toast.LENGTH_LONG).show();
            finish();
        });
        mAlert.setNegativeButton("Abbruch", (dialog, which) -> {
            //vazgecildigi icin hicbir islem yapilmiyor

            finish();
        });
        mAlert.show();


    }
    private int rd2haneliSayi() {
        Random random=new Random();
        return random.nextInt(90)+10;

    }
}