package com.example.mobile_project.dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM  user where userName=:userName AND password=:password")
    User authenticate(String userName, String password);

    @Query("SELECT * FROM user")
    LiveData<List<User>> findAllUsers();

    @Query("SELECT * FROM user WHERE username = :username")
    LiveData<User> findByUsername(String username);

    @Query("SELECT * FROM user WHERE id=:id")
    User getUserById(int id);
}
