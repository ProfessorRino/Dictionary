package com.pose.dictionary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JEXEntity::class, AnnotationEntity::class], version = 1)
abstract class JEXDatabase : RoomDatabase() {

    abstract fun jexDao(): JEXDao

    companion object {
        val DATABASE_NAME = "JEGDB"
        val DATABASE_FILE_PATH = "databases/wootdictionaryJEX.db"
        var INSTANCE: JEXDatabase? = null
        fun getDataBase(context: Context): JEXDatabase? {
            if (INSTANCE == null) {
                synchronized(JEXDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        JEXDatabase::class.java, DATABASE_NAME
                    )
                        .createFromAsset(DATABASE_FILE_PATH)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}