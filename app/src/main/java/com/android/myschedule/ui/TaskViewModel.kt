package com.android.myschedule.ui

import android.app.assist.AssistStructure
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.myschedule.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.android.myschedule.data.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel(){
    val tasks = taskDao.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun saveTask(title : String, notes : String?)
    {
        if(title.isBlank()) return
        viewModelScope.launch {
            taskDao.insert(Task(title = title, notes =  notes?.trim()))
        }
    }

    fun setDone(id: Int, isDone : Boolean){
        viewModelScope.launch {
            taskDao.setDone(id, isDone)
        }
    }

    fun undoDone(id : Int)
    {
        viewModelScope.launch {
            taskDao.setDone(id, false)
        }
    }
}