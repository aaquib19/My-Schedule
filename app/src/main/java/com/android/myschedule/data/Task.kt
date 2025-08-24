package com.android.myschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title : String,
    val notes : String? = null,
    val createdAt : Long = System.currentTimeMillis(),
    val isDone : Boolean = false
)