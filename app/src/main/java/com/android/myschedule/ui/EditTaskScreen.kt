package com.android.myschedule.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EditTaskScreen (
    taskId: Int,
    onBack : () -> Unit,
    vm : TaskViewModel  = hiltViewModel()
){

}