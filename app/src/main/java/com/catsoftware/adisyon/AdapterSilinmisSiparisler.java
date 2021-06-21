package com.catsoftware.adisyon;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.catsoftware.adisyon.db.AppDatabase;
import com.catsoftware.adisyon.db.SiparisSatiri;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterSilinmisSiparisler extends RecyclerView.Adapter<AdapterSilinmisSiparisler.MyViewHolder> {

    List<SiparisSatiri> mDataList;
    final LayoutInflater layoutInflater;
    AppDatabase db;
    final Context context;



    public AdapterSilinmisSiparisler(Context context, List<SiparisSatiri> siparisList) {
        layoutInflater = LayoutInflater.from(context);
        this.mDataList = siparisList;
        this.context = context;

    }


    @Override
    public AdapterSilinmisSiparisler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        db = AppDatabase.getDbInstance(parent.getContext());
        View v= layoutInflater.inflate(R.layout.list_item_geri_alinabilir, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterSilinmisSiparisler.MyViewHolder holder, int position) {

        SiparisSatiri tiklanilanSiparis = mDataList.get(position);
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
                mAlert.setTitle("SILINMIS SIPARIS GERI ALINACAK");
                mAlert.setMessage("Sectiginiz siparisin tekrardan sürücünün hesabina eklenmesini onayliyor musunuz?");
                mAlert.setPositiveButton("Onayliyorum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        silinmisiGeriAl(siparisId);

                        notifyDataSetChanged();
                        anaListeyiGuncelle();

                        Toast.makeText(context, "Siparis sürücünün hesabina yeniden eklendi.", Toast.LENGTH_LONG).show();

                    }
                }).setNegativeButton("Vazgec", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mAlert.show();

            });



        }

        private void anaListeyiGuncelle() {
            mDataList = db.siparisDao().siparisleriGetir(true);
        }

        private void silinmisiGeriAl(int siparisId) {
            db.siparisDao().setSilindiMi(siparisId, false);
            notifyItemRemoved(tiklanilanPosition);

        }

        public void setData(SiparisSatiri tiklanilanSiparis, int position) {
            this.tvUcret.setText(tiklanilanSiparis.getUcret().toString() + " €");
            this.tvOdemeYontemi.setText(tiklanilanSiparis.getOdemeYontemi());
            this.tvSurucuNo.setText(tiklanilanSiparis.getSurucu() + " nolu sürücü");
            this.tvSaatDakika.setText(ikiHaneliOlsun(tiklanilanSiparis.getSaat()) + ":" + ikiHaneliOlsun(tiklanilanSiparis.getDakika()));
            siparisId = tiklanilanSiparis.getsId();
            tiklanilanPosition = position;


        }

        public String ikiHaneliOlsun(int sayi) {
            DecimalFormat formatter = new DecimalFormat("00");
            return formatter.format(sayi);
        }
    }



}


