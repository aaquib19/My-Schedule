package com.android.myschedule.data

import android.content.Context
import androidx.room.Room
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
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()

    @Provides
    fun providesTaskDao(db: AppDatabase): TaskDao = db.taskDao()
}