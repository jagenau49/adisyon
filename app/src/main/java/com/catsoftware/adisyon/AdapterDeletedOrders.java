package com.catsoftware.adisyon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.Order;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

import static com.catsoftware.adisyon.MainActivity.deleteOldOrders;

public class AdapterDeletedOrders extends RecyclerView.Adapter<AdapterDeletedOrders.MyViewHolder> {

    List<Order> mDataList;
    final LayoutInflater layoutInflater;
    AppDatabase db;
    final Context context;



    public AdapterDeletedOrders(Context context, List<Order> orders) {
        layoutInflater = LayoutInflater.from(context);
        this.mDataList = orders;
        this.context = context;

    }


    @Override
    public AdapterDeletedOrders.@NotNull MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        db = AppDatabase.getDbInstance(parent.getContext());
        View v= layoutInflater.inflate(R.layout.list_item_recylable, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterDeletedOrders.MyViewHolder holder, int position) {

        Order clickedOrder = mDataList.get(position);
        holder.setData(clickedOrder, position);

    }

    @Override
    public int getItemCount() {
        return this.mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPrice, tvPaymentMethod, tvDriver, tvHourAndMinute;
        final Button btRecycle;
        int ID = -1;
        int clickedPos = -1;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvHourAndMinute = itemView.findViewById(R.id.tvHourAndMinute);

            btRecycle =itemView.findViewById(R.id.btRecycle);
            btRecycle.setOnClickListener(v -> {
                //checks if user really wants
                AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
                mAlert.setTitle("Die stornierte Bestellung wird recycelt");
                mAlert.setMessage("Sind Sie sicher");
                mAlert.setPositiveButton("Bestätigen", (dialog, which) -> {

                    recycleDeletedOrder(ID);

                    notifyDataSetChanged();
                    refreshMainList();



                }).setNegativeButton("Abbruch", (dialog, which) -> {

                });

                mAlert.show();

            });



        }

        private void refreshMainList() {
            deleteOldOrders(context);
            mDataList = db.orderDao().getOrders(true);
        }

        private void recycleDeletedOrder(int ID) {
            db.orderDao().setIsDeleted(ID, false);
            notifyItemRemoved(clickedPos);

        }

        public void setData(Order clickedOrder, int position) {
            this.tvPrice.setText(clickedOrder.getPrice().toString() + " €");
            this.tvPaymentMethod.setText(clickedOrder.getPaymentMethod());
            this.tvDriver.setText(clickedOrder.getDriver() + ".Fahrer");
            this.tvHourAndMinute.setText(updateAsTwoDigits(clickedOrder.getHour()) + ":" +
                    updateAsTwoDigits(clickedOrder.getMinute()));
            ID = clickedOrder.getID();
            clickedPos = position;


        }

        public String updateAsTwoDigits(int sayi) {
            DecimalFormat formatter = new DecimalFormat("00");
            return formatter.format(sayi);
        }
    }



}


