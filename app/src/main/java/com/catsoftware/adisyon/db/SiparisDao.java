package com.catsoftware.adisyon.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface SiparisDao {

    @Query("SELECT * FROM siparislerTablosu WHERE silindiMi=:silinmisMi ORDER BY sId DESC")
    List<SiparisSatiri> siparisleriGetir(boolean silinmisMi);//en yeni en ustte silinmemisleri cekiyor

    @Query("SELECT * FROM siparislerTablosu WHERE sId=:sId ")
    List<SiparisSatiri> siparisDetayGetir(int sId); //id ye ait siparis detaylarini donduruyor

    @Query("SELECT * FROM siparislerTablosu WHERE surucu=:surucu AND silindiMi=:silinmisMi ")
    List<SiparisSatiri> surucununSiparisleriniGetir(String surucu, boolean silinmisMi); //id ye ait siparis detaylarini donduruyor


    @Query("SELECT * FROM siparislerTablosu WHERE sId IN (:sIds)")
    List<SiparisSatiri> loadAllByIds(int[] sIds);
    @Query("UPDATE siparislerTablosu SET saat=:saat, dakika=:dakika, surucu=:surucu, odemeYontemi=:odemeYontemi, ucret=:ucret, siparisNo=:siparisNo WHERE sId=:duzenlenecekSiparisId")
    void guncelleSiparis(int duzenlenecekSiparisId, int saat, int dakika, String surucu, String odemeYontemi, double ucret, String siparisNo);//siparisi gelen verilerle guncelliyor


    @Query("UPDATE siparislerTablosu SET silindiMi=:silinmisMi WHERE sId=:sId") void setSilindiMi(int sId, boolean silinmisMi);//siparisi silindi olarak isaretliyor




    @Insert
    void insertSiparis(SiparisSatiri... siparislerTablosu);


}