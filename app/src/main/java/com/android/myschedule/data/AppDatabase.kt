package com.android.myschedule.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Task :: class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao


    companion object{
        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE INDEX IF NOT EXISTS index_tasks_createdAt ON tasks(createdAt)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN epochDay INTEGER NOT NULL DEFAULT 0")
            }
        }


    }
}