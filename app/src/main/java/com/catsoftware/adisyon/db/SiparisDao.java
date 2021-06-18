package com.catsoftware.adisyon.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface SiparisDao {

    @Query("SELECT * FROM siparislerTablosu WHERE silindiMi=:silinmisMi ORDER BY sId DESC")//TODO:duzelt
    List<SiparisSatiri> siparisleriGetir(boolean silinmisMi);//en yeni en ustte silinmemisleri cekiyor


    @Query("SELECT * FROM siparislerTablosu WHERE sId IN (:sIds)")
    List<SiparisSatiri> loadAllByIds(int[] sIds);

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