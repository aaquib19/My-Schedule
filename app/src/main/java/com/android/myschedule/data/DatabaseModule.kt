package com.android.myschedule.data

import android.content.Context
import androidx.room.Room
import com.android.myschedule.data.AppDatabase.Companion.MIGRATION_2_3
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(context,
        AppDatabase::class.java, "myschedule.db")
        .addMigrations(AppDatabase.MIGRATION_1_2, MIGRATION_2_3)
        .build()

    @Provides
    fun providesTaskDao(db: AppDatabase): TaskDao = db.taskDao()
}