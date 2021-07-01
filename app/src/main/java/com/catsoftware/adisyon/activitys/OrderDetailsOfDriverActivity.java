package com.catsoftware.adisyon.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.catsoftware.adisyon.adapters.OrderAdapter;
import com.catsoftware.adisyon.R;
import com.catsoftware.adisyon.db.Order;

import java.util.List;

public class OrderDetailsOfDriverActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Order> ordersOfDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_of_driver);


        //get values from intent
        Intent i= getIntent();
        String driver=i.getStringExtra(PaymentDriverActivity.SELECTED_DRIVER);
        ordersOfDriver = (List<Order>)( i.getBundleExtra(PaymentDriverActivity.KEY_LIST).getSerializable(PaymentDriverActivity.LIST_OF_DRIVERS_ORDERS));

        //Layout items are updated
        TextView tvSelectedDriver=findViewById(R.id.tvSelectedDriver);
        tvSelectedDriver.setText(driver);
        Button btBackToPaymentDriverActivity=findViewById(R.id.btBackToPaymentDriverActivity);
        btBackToPaymentDriverActivity.setOnClickListener(v -> finish());
updateRecyclerview();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateRecyclerview() {


        recyclerView = findViewById(R.id.rvPaymentDriver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(this, ordersOfDriver, getClass().getName());
        recyclerView.setAdapter(orderAdapter);

    }
}