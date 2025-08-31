package com.android.myschedule.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)


    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    fun observeTask(id : Int) : Flow<Task>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * From tasks ORDER BY id DESC")
    fun getAllTasks() : Flow<List<Task>>

    @Query("UPDATE tasks SET isDone = :done WHERE id=:taskId")
    suspend fun setDone(taskId: Int, done: Boolean)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Query("DELETE FROM tasks Where id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM tasks where epochDay = :epochDay ORDER BY id DESC")
    fun getTaskForDate(epochDay : Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks where epochDay BETWEEN :startDate AND :endDate ORDER BY id DESC")
    fun getTaskForRange(startDate : Long, endDate : Long) : Flow<List<Task>>
}