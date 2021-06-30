package com.catsoftware.adisyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.Order;

import java.util.List;

import static com.catsoftware.adisyon.MainActivity.deleteOldOrders;

public class DeletedOrdersActivity extends AppCompatActivity {
    AppDatabase db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silinmis_siparisler);


        db= AppDatabase.getDbInstance(this.getApplicationContext());//db is assigned

        //layout items are assigned
        Button btBackToMainActivity = findViewById(R.id.btBackToMainActivity);
        btBackToMainActivity.setOnClickListener(v -> returnToMainActivity());

        //recycleviewer is assigned
        recyclerView=findViewById(R.id.rvDeletedOrders);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        AdapterDeletedOrders adapterDeletedOrders= new AdapterDeletedOrders(this, getDeletedOrdersList());
        recyclerView.setAdapter(adapterDeletedOrders);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private List<Order> getDeletedOrdersList() {
        deleteOldOrders(DeletedOrdersActivity.this);
        return db.orderDao().getOrders(true);

    }


    public void returnToMainActivity() {
        Intent intent = new Intent(DeletedOrdersActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
