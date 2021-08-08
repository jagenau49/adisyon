package com.catsoftware.adisyon.activities;



import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.catsoftware.adisyon.adapters.OrderAdapter;
import com.catsoftware.adisyon.R;
import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.Order;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static com.catsoftware.adisyon.activities.MainActivity.deleteOldOrders;

public class PaymentDriverActivity extends AppCompatActivity {
    public static final String LIST_OF_DRIVERS_ORDERS ="LIST_OF_DRIVERS_ORDERS";
    public static final String SELECTED_DRIVER ="SELECTED_DRIVER";
    public static final String KEY_LIST="LIST";

    public static int hourBeginWork = -1;
    public static int hourEndWork = -1;
    public static int minuteBeginWork = -1;
    public static int minuteEndWork = -1;
    public static String staticDriver;
    Spinner spDrivers;
    EditText etHourlyRate;
    Button btBack, btCalculate, btShowDetails;
    TextView tvTotalCashCostOfOrders, tvTotalCardCostOfOrders, tvDriversTodaysPaymet,
            tvReturnAmount, tvEndHourOfWork, tvBeginHourOfWork, tvWorkHoursTotal,
            tvDeliveredOrders, tvDriverGainedFromHourlyRate, tvDriverResultNumber;

    LinearLayout layoutResults;
    AppDatabase db;
    List<Order> orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_driver);


        db = AppDatabase.getDbInstance(this.getApplicationContext());

        //layout items are assigned
        spDrivers = findViewById(R.id.spDriversForPayment);

        etHourlyRate = findViewById(R.id.etHourlyRate);
        btBack = findViewById(R.id.btBack);
        btCalculate = findViewById(R.id.btCalculate);
        btShowDetails =findViewById(R.id.btShowDetails);
        layoutResults = findViewById(R.id.layoutResults);
        tvTotalCashCostOfOrders = findViewById(R.id.tvTotalCashCostOfOrders);
        tvTotalCardCostOfOrders = findViewById(R.id.tvTotalCardCostOfOrders);
        tvDriversTodaysPaymet = findViewById(R.id.tvDriversTodaysPaymet);
        tvReturnAmount = findViewById(R.id.tvReturnAmount);
        tvBeginHourOfWork = findViewById(R.id.tvBeginHourOfWork);
        tvEndHourOfWork = findViewById(R.id.tvEndHourOfWork);
        tvWorkHoursTotal = findViewById(R.id.tvWorkHoursTotal);
        tvDeliveredOrders = findViewById(R.id.tvDeliveredOrders);
        tvDriverGainedFromHourlyRate =findViewById(R.id.tvDriverGainedFromHourlyRate);
        tvDriverResultNumber =findViewById(R.id.tvDriverResultNumber);




        //views updated
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drivers, R.layout.spinner_item);
        spDrivers.setAdapter(adapter);


        btBack.setOnClickListener(v -> goToMainActivity());
        btShowDetails.setOnClickListener(v -> showOrdersOfSelectedDriver());

        btCalculate.setOnClickListener(v -> {

            if (hourBeginWork == 0 || hourEndWork == 0 || etHourlyRate.getText().toString().equals("")) {
                //missing inputs
                Toast.makeText(PaymentDriverActivity.this, "Bitte den Fromular asufüllen! ", Toast.LENGTH_LONG).show();


            } else {
                //values are ready



                calculatePayment(spDrivers.getSelectedItem().toString(), hourBeginWork,
                        minuteBeginWork, hourEndWork, minuteEndWork, Double.
                                parseDouble(etHourlyRate.getText().toString()));

                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        });

    }



    private void calculatePayment(String driver, int hourAtBegin, int minuteAtBegin, int hourAtEnd, int minuteAtEnd, double hourlyRate) {//
        deleteOldOrders(PaymentDriverActivity.this);
        staticDriver =driver;
         orders = db.orderDao().getAllOrdersOfDriver(driver, false);
        int countDeliveredOrders = orders.size();
        double sumOfDeliveredCashOrders = 0.0;
        double sumOfDeliveredCardOrders = 0.0;
        int sumOfDrivers5Mins = calculate5Mins(hourAtBegin, minuteAtBegin, hourAtEnd, minuteAtEnd);

        double paymentForHourlyRates=((hourlyRate / 12.0) * sumOfDrivers5Mins);
        double paymentTotalForDriver = paymentForHourlyRates+ countDeliveredOrders;

        for (Order order : orders) {
            if (order.getPaymentMethod().equals("Online")) {
                sumOfDeliveredCardOrders += order.getPrice();
            } else if (order.getPaymentMethod().equals("Bar")) {
                sumOfDeliveredCashOrders += order.getPrice();
            }

        }
        double sumOfExtrasAtDriver = sumOfDeliveredCashOrders - paymentTotalForDriver;
        tvTotalCashCostOfOrders.setText(sumOfDeliveredCashOrders + "€");
        tvTotalCardCostOfOrders.setText(sumOfDeliveredCardOrders + "€");
        tvDriversTodaysPaymet.setText(paymentTotalForDriver + " €");
        tvReturnAmount.setText(sumOfExtrasAtDriver + " €");
        tvWorkHoursTotal.setText(calculateWorkTime(sumOfDrivers5Mins));
        tvDeliveredOrders.setText("" + countDeliveredOrders);
        tvDriverGainedFromHourlyRate.setText("( "+paymentForHourlyRates+"€ )");
        tvDriverResultNumber.setText(driver);




        //show results
        layoutResults.setVisibility(View.VISIBLE);


    }

    private String calculateWorkTime(int sum10Mins) {
        int totalHour = sum10Mins / 6;
        int totalMinutes = (sum10Mins % 6) * 10;
        return (totalHour + " Stunden " + totalMinutes + " Minuten");

    }

    private int calculate5Mins(int hourBeginWork, int minutesBeginWork, int hourEndWork, int minuteEndWork) {
        return ((60 - minutesBeginWork) + ((hourEndWork - hourBeginWork - 1) * 60) + minuteEndWork) / 5;
    }

    public void goToMainActivity() {
        Intent intent = new Intent(PaymentDriverActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void pickHour(View view) {
        // Get Current Time
        int mHour = getCurrentHour();
        int mMinute = getCurrentMinute();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (timePicker, hourOfDay, minute) -> {
                    if (view.getId() == R.id.btIseBaslamaSaatiSec) {
                        ((TextView) tvBeginHourOfWork).setText(OrderAdapter.showAsTwoDigits(hourOfDay) + ":" + OrderAdapter.showAsTwoDigits(minute));
                        ((TextView) tvBeginHourOfWork).setVisibility(View.VISIBLE);
                        hourBeginWork = hourOfDay;
                        minuteBeginWork = minute;
                    } else if (view.getId() == R.id.btIsiBitirmeSaatiSec) {
                        ((TextView) tvEndHourOfWork).setText(OrderAdapter.showAsTwoDigits(hourOfDay) + ":" + OrderAdapter.showAsTwoDigits(minute));
                        ((TextView) tvEndHourOfWork).setVisibility(View.VISIBLE);
                        hourEndWork = hourOfDay;
                        minuteEndWork = minute;
                    }


                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private int getCurrentMinute() {
        final Calendar c = Calendar.getInstance();


        return c.get(Calendar.MINUTE);
    }

    private int getCurrentHour() {
        final Calendar c = Calendar.getInstance();

        return c.get(Calendar.HOUR_OF_DAY);
    }

    public void showOrdersOfSelectedDriver() {
        Intent i = new Intent(this, OrderDetailsOfDriverActivity.class);
        Bundle b = new Bundle();


        b.putSerializable(LIST_OF_DRIVERS_ORDERS, (Serializable) orders);
        i.putExtra(KEY_LIST,b);
        i.putExtra(SELECTED_DRIVER, staticDriver);
        startActivity(i);
    }
}