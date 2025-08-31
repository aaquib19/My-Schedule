package com.android.myschedule.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tasks", indices = [Index("createdAt")])
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title : String,
    val notes : String? = null,
    val createdAt : Long = System.currentTimeMillis(),
    val isDone : Boolean = false,
    val epochDay : Long //LocalDatte.now().tooEpochDay()
)