package com.example.mobile_project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mobile_project.dao.UserDao;
import com.example.mobile_project.entity.Converter;
import com.example.mobile_project.entity.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract UserDao userDao();
    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "lost_and_found")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
