package com.panasetskaia.storyarchitectlogline.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.panasetskaia.storyarchitectlogline.domain.Logline


@Database(
    entities = [Logline::class],
    version = 2,
    exportSchema = true
)
abstract class LoglineDatabase : RoomDatabase() {

    abstract fun loglineDao(): LoglineDao

    companion object {
        private var INSTANCE: LoglineDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "logline_creator.db"

        fun getInstance(application: Application): LoglineDatabase {

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(application, LoglineDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}