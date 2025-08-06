package com.jabulani.ligiopen.data.db

import com.jabulani.ligiopen.data.db.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface DBRepository {
    suspend fun insertUser(userAccount: UserAccount)


    suspend fun updateUser(userAccount: UserAccount)


    fun getUsers(): Flow<List<UserAccount>>


    fun getUserByUserId(id: Int): Flow<UserAccount>

    suspend fun deleteUsers()
}