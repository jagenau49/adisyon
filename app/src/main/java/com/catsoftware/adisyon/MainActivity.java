package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Layout nesneleri tanimlandi
    EditText etSiparisSaati, etSurucuNo, etOdemeYontemi, etUcret;
    TextView tvSaat, tvSurucuNo, tvOdemeYontemi, tvUcret;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Layout nesneleri degiskenlere atandi
        etSiparisSaati=findViewById(R.id.etSiparisSaati);
        etSurucuNo=findViewById(R.id.etSurucuNo);
        etOdemeYontemi=findViewById(R.id.etOdemeYontemi);
        etUcret=findViewById(R.id.etUcret);
        tvSaat=findViewById(R.id.tvSaat);
        tvSurucuNo=findViewById(R.id.tvSurucuNo);
        tvOdemeYontemi=findViewById(R.id.tvOdemeYontemi);
        tvUcret=findViewById(R.id.tvUcret);
    }
    public void siparisKaydet(View view){
        String saat=etSiparisSaati.getText().toString();
        String surucu=etSurucuNo.getText().toString();
        String odemeYontemi=etOdemeYontemi.getText().toString();
        String ucret=etUcret.getText().toString();
        if(!(saat.equals("")&&surucu.equals("")&&odemeYontemi.equals("")&&ucret.equals(""))){//verilerin bos olup olmadigi kontrol ediliyor
            tvSaat.setText(saat);
            tvSurucuNo.setText(surucu);
            tvOdemeYontemi.setText(odemeYontemi);
            tvUcret.setText(ucret);

        }

    }
}