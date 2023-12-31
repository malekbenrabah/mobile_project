package com.example.mobile_project.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobile_project.dao.UserDao;
import com.example.mobile_project.entity.Commentaire;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;

@Database(entities = {User.class, Post.class, Commentaire.class}, version = 3)
@AutoMigration(from = 1, to = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final java.util.concurrent.Executor IO_EXECUTOR = java.util.concurrent.Executors.newSingleThreadExecutor();

    public static void ioThread(Runnable runnable) {
        IO_EXECUTOR.execute(runnable);
    }
    private static AppDatabase instance;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
