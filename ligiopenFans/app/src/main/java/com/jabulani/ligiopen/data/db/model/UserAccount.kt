package com.jabulani.ligiopen.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val createdAt: String,
    val token: String,
    val darkMode: Boolean = false
)
