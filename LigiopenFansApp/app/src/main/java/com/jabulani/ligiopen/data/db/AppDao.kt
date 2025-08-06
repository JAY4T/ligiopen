package com.jabulani.ligiopen.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jabulani.ligiopen.data.db.model.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userAccount: UserAccount)

    @Update
    suspend fun updateUser(userAccount: UserAccount)

    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<UserAccount>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserByUserId(id: Int): Flow<UserAccount>

    @Query("DELETE FROM user")
    fun deleteUsers();
}