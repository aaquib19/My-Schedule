package com.android.myschedule.ui

import androidx.lifecycle.ViewModel
import com.android.myschedule.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.android.myschedule.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel(){
    private val _selectedEpochDay = MutableStateFlow(LocalDate.now().toEpochDay())
    val selectedEpochDay = _selectedEpochDay.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksForSelectedDate : StateFlow<List<Task>> = selectedEpochDay.flatMapLatest { taskDao.getTaskForDate(it) }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun setSelectedDate(date : LocalDate){
        _selectedEpochDay.value = date.toEpochDay()
    }

    fun saveTask(title : String, notes : String?, epochDay : Long)
    {
        if(title.isBlank()) return
        viewModelScope.launch {
            taskDao.insert(Task(title = title, notes =  notes?.trim(), epochDay = epochDay))
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

    fun observerTask(id : Int) = taskDao.observeTask(id)

    fun updateTask(updated : Task){
        viewModelScope.launch { taskDao.update(updated) }
    }

    fun deleteTask(task : Task){
        viewModelScope.launch { taskDao.deleteById(task.id)}
    }

    fun undoDelete(task : Task){
        viewModelScope.launch { taskDao.insert(task) }
    }

    fun getTasksCountForDay(epochDay: Long): kotlinx.coroutines.flow.Flow<Int> {
        return taskDao.getTaskCountForDay(epochDay)
    }
}