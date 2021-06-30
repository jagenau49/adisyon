package com.catsoftware.adisyon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.Order;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

import static com.catsoftware.adisyon.MainActivity.deleteOldOrders;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    public static final String ID = "ID";
    public static final String IS_EDIT = "IS_EDIT";
    List<Order> mDataList;
    final LayoutInflater layoutInflater;
    AppDatabase db;
    final Context context;
    final String className;



    public OrderAdapter(Context context, List<Order> orderList, String className) {
        layoutInflater = LayoutInflater.from(context);
        this.mDataList = orderList;
        this.context = context;
        this.className = className;
    }


    @Override
    public @NotNull MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        db = AppDatabase.getDbInstance(parent.getContext());
        View v= layoutInflater.inflate(R.layout.list_item, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, int position) {

        Order onClickedOrder = mDataList.get(position);
        holder.setData(onClickedOrder, position);

    }

    @Override
    public int getItemCount() {
        return this.mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPrice, tvPaymentMethod, tvDriver, tvHourMinute, tvOrderNo;
        final ImageView ivEdit;
        final ImageView ivDelete;
        int ID = -1;
        int onClickedPosition = -1;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvHourMinute = itemView.findViewById(R.id.tvHourAndMinute);
            tvOrderNo =itemView.findViewById(R.id.tvOrderNo);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            ivDelete.setOnClickListener(v -> {

                //checks if user is sure
                AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
                mAlert.setTitle("Die Bestellung wird gelöscht!");
                mAlert.setMessage("Sind Sie sicher?");
                mAlert.setPositiveButton("Bestätigen", (dialog, which) -> {
                    deleteOrder(ID);
                    if (className.equals(MainActivity.class.getName())) {
                        refreshMainList();

                    } else if (className.equals(OrderDetailsOfDriver.class.getName())) {
                        refreshListOfQuery();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }


                    Toast.makeText(context, "Die Bestellung wurde storniert.", Toast.LENGTH_LONG).show();

                });
                mAlert.setNegativeButton("Abbruch", (dialog, which) -> {
                    //user cancelled operation, nothing is done

                });
                mAlert.show();





            });

            ivEdit.setOnClickListener(v -> {
                //order will be edited
                goToOrderEditing(ID);
            });

        }

        private void refreshMainList() {
            deleteOldOrders(context);
            mDataList = db.orderDao().getOrders(false);

        }

        private void deleteOrder(int ID) {
            db.orderDao().setIsDeleted(ID, true);
            notifyItemRemoved(onClickedPosition);

            /*
            I have found as a solution for after deleting order to refresh main order list
            It is not the best solution, it should be updated with correct solution
             */
            Intent intent=new Intent(context,MainActivity.class);
            context.startActivity(intent);

        }

        public void setData(Order onClickedOrder, int position) {
            this.tvPrice.setText(onClickedOrder.getPrice().toString() + " €");
            this.tvPaymentMethod.setText(onClickedOrder.getPaymentMethod());
            this.tvDriver.setText(onClickedOrder.getDriver() + " .Fahrer");
            this.tvOrderNo.setText(onClickedOrder.getOrderNo());
            this.tvHourMinute.setText(showAsTwoDigits(onClickedOrder.getHour()) + ":" + showAsTwoDigits(onClickedOrder.getMinute()));
            ID = onClickedOrder.getID();
            onClickedPosition = position;


        }


    }

    private void refreshListOfQuery() {
        deleteOldOrders(context);
        mDataList = db.orderDao().getAllOrdersOfDriver(PaymentDriverActivity.staticDriver, false);

    }

    private void goToOrderEditing(int ID) {
        Intent intent = new Intent(context, OrderAddActivity.class);
        intent.putExtra(IS_EDIT, true);
        intent.putExtra(OrderAdapter.ID, ID);
        context.startActivity(intent);
    }
    public static String showAsTwoDigits(int sayi) {
        DecimalFormat formatter = new DecimalFormat("00");
        return formatter.format(sayi);
    }
}
