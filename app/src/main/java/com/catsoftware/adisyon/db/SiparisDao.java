package com.catsoftware.adisyon.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface SiparisDao {
    @Query("SELECT * FROM siparislerTablosu")
    List<Siparis> getAll();

    @Query("SELECT * FROM siparislerTablosu WHERE sId IN (:sIds)")
    List<Siparis> loadAllByIds(int[] sIds);


        /*  //TODO: kendine uyarla
        @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1")
        User findByName(String first, String last);
        */


    @Insert
    void insertSiparis(Siparis... siparislerTablosu);

    @Delete
    void delete(Siparis siparislerTablosu);


}