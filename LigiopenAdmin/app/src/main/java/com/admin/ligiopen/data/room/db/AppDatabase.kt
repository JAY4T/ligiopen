package com.admin.ligiopen.data.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.admin.ligiopen.data.room.models.UserAccount

@Database(entities = [UserAccount::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                val builder = Room.databaseBuilder(context, AppDatabase::class.java, "ligiopenadmin_db")
                    .fallbackToDestructiveMigration()
                builder.build().also { Instance = it }
            }

        }
    }
}