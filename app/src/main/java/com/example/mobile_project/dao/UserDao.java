package com.example.mobile_project.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile_project.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);


    @Query("SELECT EXISTS (SELECT * FROM  user where userName=:userName)")
    boolean existsUserName(String userName);

    @Query("SELECT EXISTS (SELECT * FROM  user where email=:email)")
    boolean existsEmail(String email);

    @Query("SELECT EXISTS (SELECT * FROM  user where userName=:userName AND password=:password)")
    boolean login(String userName, String password);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE username = :username")
    User getUserByUsername(String username);


}
