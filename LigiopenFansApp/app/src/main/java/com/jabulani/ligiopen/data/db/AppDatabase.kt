package com.jabulani.ligiopen.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jabulani.ligiopen.data.db.model.UserAccount

@Database(entities = [UserAccount::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                val builder = Room.databaseBuilder(context, AppDatabase::class.java, "ligiopen_db")
                    .fallbackToDestructiveMigration()
                builder.build().also { Instance = it }
            }

        }
    }
}