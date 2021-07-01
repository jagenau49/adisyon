package com.catsoftware.adisyon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.catsoftware.adisyon.adapters.OrderAdapter;
import com.catsoftware.adisyon.R;
import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.Order;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.catsoftware.adisyon.activities.MainActivity.deleteOldOrders;

public class OrderAddActivity extends AppCompatActivity {
    AppDatabase db;
    Boolean isEdit;
    int editingOrderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);


        db=AppDatabase.getDbInstance(this.getApplicationContext());//db assigned

        //getting values from intent
        Intent intent=getIntent();
        isEdit =intent.getBooleanExtra(OrderAdapter.IS_EDIT,false);
         editingOrderId =intent.getIntExtra(OrderAdapter.ID,-1);

        //Layout item are assigned
        final EditText etPrice= findViewById(R.id.etPrice);
        final EditText etOrderNo=findViewById(R.id.etOrderNo);
        Button btAddOrder= findViewById(R.id.btAddOrder);
        Button btCancel=findViewById(R.id.btCancel);
        TimePicker picker =findViewById(R.id.timePicker1);
        Spinner spDrivers = findViewById(R.id.spDrivers);
        Spinner spPaymentMethods=findViewById(R.id.spPaymentMethods);
        picker.setIs24HourView(true);//clock is set to 24H view

        // Adapters for spinners assigned
        ArrayAdapter<CharSequence> adapterDriver =
                ArrayAdapter.createFromResource(this, R.array.drivers, R.layout.spinner_item);
        spDrivers.setAdapter(adapterDriver);
        ArrayAdapter<CharSequence> adapterPaymentMethods =
                ArrayAdapter.createFromResource(this, R.array.payment_methods, R.layout.spinner_item);
        spPaymentMethods.setAdapter(adapterPaymentMethods);

        if (isEdit) {//order is edited
           deleteOldOrders(OrderAddActivity.this);
        //get values for editing
            List<Order> listOrderDetails= db.orderDao().getDetailsOfOrder(editingOrderId);
            int editingHour=listOrderDetails.get(0).getHour();
            int editingMinute=listOrderDetails.get(0).getMinute();
            String editingDriver=listOrderDetails.get(0).getDriver();
            String editingPaymentMethods=listOrderDetails.get(0).getPaymentMethod();
            int idPaymentMethod;
            if (editingPaymentMethods.equals("Bar")){
                idPaymentMethod=0;
            }else{
                idPaymentMethod=1;
            }
            double editingPrice=listOrderDetails.get(0).getPrice();
            String editingOrderNo=listOrderDetails.get(0).getOrderNo();

            //Layout items shows editing values
            btAddOrder.setText("Korrigieren");
            pickerSetMinute(picker,editingMinute);
            pickerSetHour(picker,editingHour);
            spDrivers.setSelection(Integer.parseInt(editingDriver)-1);

            spPaymentMethods.setSelection(idPaymentMethod);
            etPrice.setText(""+editingPrice);
            etOrderNo.setText(editingOrderNo);



        }





          btAddOrder.setOnClickListener(v -> {

                  addOrder(isEdit, pickerGetHour(picker), pickerGetMinute(picker),
                          spDrivers.getSelectedItem().toString(), spPaymentMethods.getSelectedItem().toString(),
                          etPrice.getText().toString(),etOrderNo.getText().toString());
              //hides keyboard
              InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
              inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
              });

          btCancel.setOnClickListener(v -> goToMainActivity());


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public int pickerGetHour(TimePicker timePicker){
        if (Build.VERSION.SDK_INT >= 23 ){
            return timePicker.getHour();

        }
        else{
            //noinspection deprecation
            return timePicker.getCurrentHour();

        }
    }
    public int pickerGetMinute(TimePicker timePicker){
        if (Build.VERSION.SDK_INT >= 23 ){
            return timePicker.getMinute();

        }
        else{
            //noinspection deprecation
            return timePicker.getCurrentMinute();

        }
    }
    public void pickerSetHour(TimePicker timePicker, int hour){
        if (Build.VERSION.SDK_INT >= 23 ){
            timePicker.setHour(hour);

        }
        else{
            timePicker.setCurrentHour(hour);

        }

    }
    public void pickerSetMinute(TimePicker timePicker, int minute){
        if (Build.VERSION.SDK_INT >= 23 ){
            timePicker.setMinute(minute);

        }
        else{
            timePicker.setCurrentMinute(minute);

        }
    }





    public void addOrder(boolean isEdit, int hour, int minute, String driver,
                         String paymentMethod, String stringPrice, String orderNo) {




        if ((driver.equals("") || paymentMethod.equals("") || (stringPrice.equals(""))||(orderNo.equals("")))) {
            //missed values
            Toast.makeText(OrderAddActivity.this,"Bitte den Formular ausfüllen!",Toast.LENGTH_LONG).show();
        }else{// all values are ready
            double price=Double.parseDouble(stringPrice);//it makes available to calculate

            //get current time values
            Calendar c = Calendar.getInstance();
            Date date=new Date();
            c.setTime(date);
            int currentDay= c.get(Calendar.DAY_OF_MONTH);
            int currentMonth=c.get(Calendar.MONTH)+1;
            int currentYear=c.get(Calendar.YEAR);

            db = AppDatabase.getDbInstance(this.getApplicationContext());


            Order order = new Order();
            order.setMinute(minute);
            order.setHour(hour);
            order.setDriver(driver);
            order.setPaymentMethod(paymentMethod);
            order.setPrice(price);
            order.setDeleted(false);
            order.setOrderNo(orderNo);
            order.setRegistrationDay(currentDay);
            order.setRegistrationMonth(currentMonth);
            order.setRegistrationYear(currentYear);

if(isEdit){//edits the order
    db.orderDao().updateOrder(editingOrderId,hour,minute,driver,paymentMethod,price,orderNo);
}else {//add new order
    db.orderDao().insertOrder(order);

}


            goToMainActivity();





        }






    }



    public void goToMainActivity() {
        Intent intent = new Intent(OrderAddActivity.this, MainActivity.class);
        startActivity(intent);
    }
}