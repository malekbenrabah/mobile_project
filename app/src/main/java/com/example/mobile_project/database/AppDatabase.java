package com.example.mobile_project.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobile_project.dao.ChatDao;
import com.example.mobile_project.dao.ChatMessageDao;
import com.example.mobile_project.dao.UserDao;
import com.example.mobile_project.entity.Chat;
import com.example.mobile_project.entity.ChatMessage;
import com.example.mobile_project.entity.Commentaire;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Post.class, Commentaire.class, Chat.class, ChatMessage.class}, version = 6)
@AutoMigration(from = 1, to = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static final Executor databaseWriteExecutor = Executors.newSingleThreadExecutor();
    private static final java.util.concurrent.Executor IO_EXECUTOR = java.util.concurrent.Executors.newSingleThreadExecutor();
    private static AppDatabase instance;

    public static void ioThread(Runnable runnable) {
        IO_EXECUTOR.execute(runnable);
    }

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract ChatDao chatDao();
    public abstract ChatMessageDao chatMessageDao();

    public void insertAdminUser() {
        ioThread(() -> {
            UserDao userDao = userDao();
            if (!userDao.existsUserName("admin")) {
                User adminUser = new User("admin", 986865, "admin@example.com", "adminPassword", "",User.Role.ADMIN);
                userDao.insertUser(adminUser);
            }
        });
    }
}
