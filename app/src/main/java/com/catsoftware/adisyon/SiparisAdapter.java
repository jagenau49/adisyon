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
import com.catsoftware.adisyon.db.SiparisSatiri;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

import static com.catsoftware.adisyon.MainActivity.deleteOldOrders;

public class SiparisAdapter extends RecyclerView.Adapter<SiparisAdapter.MyViewHolder> {
    public static final String SIPARIS_ID = "siparisId";
    public static final String DUZENLEME_MI = "duzenlemeMi";
    List<SiparisSatiri> mDataList;
    final LayoutInflater layoutInflater;
    AppDatabase db;
    final Context context;
    final String className;



    public SiparisAdapter(Context context, List<SiparisSatiri> siparisList, String className) {
        layoutInflater = LayoutInflater.from(context);
        this.mDataList = siparisList;
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
    public void onBindViewHolder(SiparisAdapter.MyViewHolder holder, int position) {

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
        final TextView tvSiparisNo;
        final ImageView ivDuzenle;
        final ImageView ivSil;
        int siparisId = -1;
        int tiklanilanPosition = -1;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvUcret = itemView.findViewById(R.id.tvUcret);
            tvOdemeYontemi = itemView.findViewById(R.id.tvOdemeYontemi);
            tvSurucuNo = itemView.findViewById(R.id.tvSurucuNo);
            tvSaatDakika = itemView.findViewById(R.id.tvSaatDakika);
            tvSiparisNo=itemView.findViewById(R.id.tvSiparisNo);
            ivDuzenle = itemView.findViewById(R.id.ivDuzenle);
            ivSil = itemView.findViewById(R.id.ivSil);

            ivSil.setOnClickListener(v -> {
                /////////////////
                //kullaniciya emin olup olmadigi soruluyor
                AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
                mAlert.setTitle("Die Bestellung wird gelöscht!");
                mAlert.setMessage("Sind Sie sicher?");
                mAlert.setPositiveButton("Bestätigen", (dialog, which) -> {
                    siparisSil(siparisId);
                    if (className.equals(MainActivity.class.getName())) {
                        anaListeyiGuncelle();

                    } else if (className.equals(surucununPaketDetaylari.class.getName())) {
                        hesapDokumuListesiniGuncelle();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }


                    Toast.makeText(context, "Die Bestellung wurde storniert.", Toast.LENGTH_LONG).show();

                });
                mAlert.setNegativeButton("Abbruch", (dialog, which) -> {
                    //vazgecildigi icin hicbir islem yapilmiyor

                });
                mAlert.show();





            });

            ivDuzenle.setOnClickListener(v -> {
                //veri duzenleme islemi yapilacak
                duzenlemeEkraninaGit(siparisId);
            });

        }

        private void anaListeyiGuncelle() {
            deleteOldOrders(context);
            mDataList = db.siparisDao().siparisleriGetir(false);

        }

        private void siparisSil(int siparisId) {
            db.siparisDao().setSilindiMi(siparisId, true);
            notifyItemRemoved(tiklanilanPosition);

            /*
            Siparis silindikten sonra mainactivity deki toplam sayiyi guncellemek icin bu cozum bulundu.
             */
            Intent intent=new Intent(context,MainActivity.class);
            context.startActivity(intent);

        }

        public void setData(SiparisSatiri tiklanilanSiparis, int position) {
            this.tvUcret.setText(tiklanilanSiparis.getUcret().toString() + " €");
            this.tvOdemeYontemi.setText(tiklanilanSiparis.getOdemeYontemi());
            this.tvSurucuNo.setText(tiklanilanSiparis.getSurucu() + " .Fahrer");
            this.tvSiparisNo.setText(tiklanilanSiparis.getSiparisNo());
            this.tvSaatDakika.setText(ikiHaneliOlsun(tiklanilanSiparis.getSaat()) + ":" + ikiHaneliOlsun(tiklanilanSiparis.getDakika()));
            siparisId = tiklanilanSiparis.getsId();
            tiklanilanPosition = position;


        }


    }

    private void hesapDokumuListesiniGuncelle() {
        deleteOldOrders(context);
        mDataList = db.siparisDao().surucununSiparisleriniGetir(surucuHesapDokumu.statikSurucuNo, false);

    }

    private void duzenlemeEkraninaGit(int siparisId) {
        Intent intent = new Intent(context, SiparisGirmeEkrani.class);
        intent.putExtra(DUZENLEME_MI, true);
        intent.putExtra(SIPARIS_ID, siparisId);
        context.startActivity(intent);
    }
    public static String ikiHaneliOlsun(int sayi) {
        DecimalFormat formatter = new DecimalFormat("00");
        return formatter.format(sayi);
    }
}
