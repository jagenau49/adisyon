package com.catsoftware.adisyon.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


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
    @Query("UPDATE siparislerTablosu SET saat=:saat, dakika=:dakika, surucu=:surucu, odemeYontemi=:odemeYontemi, ucret=:ucret WHERE sId=:duzenlenecekSiparisId")
    void guncelleSiparis(int duzenlenecekSiparisId,int saat,int dakika,String surucu,String odemeYontemi, double ucret);//siparisi gelen verilerle guncelliyor


    @Query("UPDATE siparislerTablosu SET silindiMi=:silinmisMi WHERE sId=:sId") void silindiIsaretle(int sId,boolean silinmisMi);//siparisi silindi olarak isaretliyor


        /*  //TODO: kendine uyarla
        @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1")
        User findByName(String first, String last);
        */


    @Insert
    void insertSiparis(SiparisSatiri... siparislerTablosu);



    @Delete
    void deleteSiparis(SiparisSatiri... siparislerTablosu);

    @Delete
    void delete(SiparisSatiri siparislerTablosu);


}