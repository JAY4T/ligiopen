package com.jabulani.ligiopen.data.db

import com.jabulani.ligiopen.data.db.model.UserAccount
import kotlinx.coroutines.flow.Flow

class DBRepositoryImpl(private val appDao: AppDao): DBRepository {
    override suspend fun insertUser(userAccount: UserAccount) = appDao.insertUser(userAccount)

    override suspend fun updateUser(userAccount: UserAccount) = appDao.updateUser(userAccount)

    override fun getUsers(): Flow<List<UserAccount>> = appDao.getUsers()

    override fun getUserByUserId(id: Int): Flow<UserAccount> = appDao.getUserByUserId(id = id)
    override suspend fun deleteUsers() = appDao.deleteUsers()
}