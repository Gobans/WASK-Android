package com.naccoro.wask.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ReplacementHistoryEntity.class}, version = 1)
public abstract class WaskDatabaseManager extends RoomDatabase {
    public abstract ReplacementHistoryDao replacementHistoryDao();

    private static WaskDatabaseManager instance;

    public static WaskDatabaseManager getInstance(Context context) {
        if (instance == null) {
            synchronized (WaskDatabaseManager.class) {
                instance = Room
                        .databaseBuilder(context.getApplicationContext(), WaskDatabaseManager.class, "wask.db")
                        .build();
            }
        }
        return instance;
    }
}
