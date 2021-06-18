package com.catsoftware.adisyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        SiparisAdapter siparisAdapter= new SiparisAdapter(this,loadSiparisList());//TODO: verileriCekListele() metodu veritabanini Arraylist<Siparis> olarak dondursun
recyclerView.setAdapter(siparisAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);





        






//FAB ayrintilari
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Siparis ekleme hazirlaniyor...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(MainActivity.this, SiparisGirmeEkrani.class);
                startActivity(intent);
            }
        });








    }





    private List<SiparisSatiri> loadSiparisList() {
        AppDatabase db=AppDatabase.getDbInstance(this.getApplicationContext());
        List<SiparisSatiri> listSiparis= db.siparisDao().getAll();

        return  listSiparis;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.ayarlar_menusu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnSifirla){
            //Tum siparisleri sifirliyoruz
            //verileriSifirla(); //TODO: güncelle
        }
        return super.onOptionsItemSelected(item);
    }



    public void verileriSifirla(){

        //kullaniciya emin olup olmadigi soruluyor
        AlertDialog.Builder mAlert=new AlertDialog.Builder(this);
        mAlert.setTitle("TUM SIPARISLER SILINECEK");
        mAlert.setMessage("Tüm siparislerin silinmesini onayliyor musunuz? Bu islem geri alinamaz.");
        mAlert.setPositiveButton("Onayliyorum", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO:güncelle

                System.out.println("veriler sifirlandi");//TODO:test icin yazildi sil
                Toast.makeText(MainActivity.this,"Tüm siparisler silindi.",Toast.LENGTH_LONG).show();

            }
        });
        mAlert.setNegativeButton("Vazgec", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //vazgecildigi icin hicbir islem yapilmiyor
                Toast.makeText(MainActivity.this,"Hicbir veri silinmedi.",Toast.LENGTH_SHORT).show();
            }
        });
        mAlert.show();

    }



}