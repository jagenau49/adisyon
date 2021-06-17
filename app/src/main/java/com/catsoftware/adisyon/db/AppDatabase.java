package com.catsoftware.adisyon.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Siparis.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SiparisDao siparisDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context){
        if (INSTANCE==null){
INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"Siparis_Veritabani")
        .allowMainThreadQueries()
        .build();
        }
        return INSTANCE;
    }
}
