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

public class AdapterSilinmisSiparisler extends RecyclerView.Adapter<AdapterSilinmisSiparisler.MyViewHolder> {

    List<Order> mDataList;
    final LayoutInflater layoutInflater;
    AppDatabase db;
    final Context context;



    public AdapterSilinmisSiparisler(Context context, List<Order> siparisList) {
        layoutInflater = LayoutInflater.from(context);
        this.mDataList = siparisList;
        this.context = context;

    }


    @Override
    public AdapterSilinmisSiparisler.@NotNull MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        db = AppDatabase.getDbInstance(parent.getContext());
        View v= layoutInflater.inflate(R.layout.list_item_geri_alinabilir, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterSilinmisSiparisler.MyViewHolder holder, int position) {

        Order tiklanilanSiparis = mDataList.get(position);
        holder.setData(tiklanilanSiparis, position);

    }

    @Override
    public int getItemCount() {
        return this.mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView tvUcret;
        final TextView tvOdemeYontemi;
        final TextView tvSurucuNo;
        final TextView tvSaatDakika;
        final Button btGeriAl;
        int siparisId = -1;
        int tiklanilanPosition = -1;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvUcret = itemView.findViewById(R.id.tvUcret);
            tvOdemeYontemi = itemView.findViewById(R.id.tvOdemeYontemi);
            tvSurucuNo = itemView.findViewById(R.id.tvSurucuNo);
            tvSaatDakika = itemView.findViewById(R.id.tvSaatDakika);

            btGeriAl=itemView.findViewById(R.id.btSilinmisiGeriAl);
            btGeriAl.setOnClickListener(v -> {
                //kullaniciya emin olup olmadigi soruluyor
                AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
                mAlert.setTitle("Die stornierte Bestellung wird recycelt");
                mAlert.setMessage("Sind Sie sicher");
                mAlert.setPositiveButton("Bestätigen", (dialog, which) -> {

                    silinmisiGeriAl(siparisId);

                    notifyDataSetChanged();
                    anaListeyiGuncelle();



                }).setNegativeButton("Abbruch", (dialog, which) -> {

                });

                mAlert.show();

            });



        }

        private void anaListeyiGuncelle() {
            deleteOldOrders(context);
            mDataList = db.orderDao().getOrders(true);
        }

        private void silinmisiGeriAl(int siparisId) {
            db.orderDao().setSilindiMi(siparisId, false);
            notifyItemRemoved(tiklanilanPosition);

        }

        public void setData(Order tiklanilanSiparis, int position) {
            this.tvUcret.setText(tiklanilanSiparis.getPrice().toString() + " €");
            this.tvOdemeYontemi.setText(tiklanilanSiparis.getPaymentMethod());
            this.tvSurucuNo.setText(tiklanilanSiparis.getDriver() + ".Fahrer");
            this.tvSaatDakika.setText(ikiHaneliOlsun(tiklanilanSiparis.getHour()) + ":" + ikiHaneliOlsun(tiklanilanSiparis.getMinute()));
            siparisId = tiklanilanSiparis.getID();
            tiklanilanPosition = position;


        }

        public String ikiHaneliOlsun(int sayi) {
            DecimalFormat formatter = new DecimalFormat("00");
            return formatter.format(sayi);
        }
    }



}


