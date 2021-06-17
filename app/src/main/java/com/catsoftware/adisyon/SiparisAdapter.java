package com.catsoftware.adisyon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SiparisAdapter extends RecyclerView.Adapter<SiparisAdapter.MyViewHolder> {
    ArrayList<Siparis> mDataList;
    LayoutInflater layoutInflater;

    public SiparisAdapter(Context context, ArrayList<Siparis> siparislerArrayList){
        layoutInflater=LayoutInflater.from(context);
        this.mDataList=siparislerArrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.list_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder( SiparisAdapter.MyViewHolder holder, int position) {

        Siparis tiklanilanSiparis=mDataList.get(position);
        holder.setData(tiklanilanSiparis,position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvUcret,tvOdemeYontemi,tvSurucuNo,tvSaatDakika;
        ImageView ivDuzenle, ivSil;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvUcret=itemView.findViewById(R.id.tvUcret);
            tvOdemeYontemi=itemView.findViewById(R.id.tvOdemeYontemi);
            tvSurucuNo=itemView.findViewById(R.id.tvSurucuNo);
            tvSaatDakika=itemView.findViewById(R.id.tvSaatDakika);
            ivDuzenle=itemView.findViewById(R.id.ivDuzenle);
            ivSil=itemView.findViewById(R.id.ivSil);

        }

        public void setData(Siparis tiklanilanSiparis, int position) {
            this.tvUcret.setText(tiklanilanSiparis.getUcret());
            this.tvOdemeYontemi.setText(tiklanilanSiparis.getOdemeYontemi());
            this.tvSurucuNo.setText(tiklanilanSiparis.getSurucu()+" nolu sürücü");
            this.tvSaatDakika.setText(tiklanilanSiparis.getSaat()+":"+tiklanilanSiparis.getDakika());

        }
    }
}
