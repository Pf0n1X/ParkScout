package com.example.parkscout.Repository.ModelSQL.dao

import androidx.room.*
import com.example.parkscout.Repository.ChatWithUsers
import com.example.parkscout.Repository.User

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM users")
    fun getUser(): User;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User);

    @Delete
    fun delete(user: User);

    @Transaction
    @Query("SELECT * FROM users where uid = :uid")
    fun getUserById(uid: String): User;

    @Transaction
    @Query("SELECT * FROM users")
    fun getAll(): List<User>;
}