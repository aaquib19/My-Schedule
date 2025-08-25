package com.android.myschedule.ui

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(onBack : ()-> Unit, vm: TaskViewModel = hiltViewModel()){

    var title by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var showTitleError by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val focus = LocalFocusManager.current
    val scope = rememberCoroutineScope()


    fun trySave(){
        showTitleError = title.isBlank()
        if(showTitleError) return
        vm.saveTask(title.trim(), notes.ifBlank { null }?.trim())
        title= ""
        notes = ""
        scope.launch {
            snackbarHostState.showSnackbar("Task Created")
            onBack()
        }
    }


    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Create Task") },
                navigationIcon =  {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            )
        }
    )
    { innerPadding ->

        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if(showTitleError && it.isNotBlank()) showTitleError = false
                },
                label = {Text("Title *")},
                singleLine = true,
                isError =  showTitleError,
                supportingText = {if(showTitleError) Text("Title is required")},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                notes, {
                    notes = it
                }, label = {Text("Notes (optional)")},
                minLines = 3,
                isError =  showTitleError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focus.clearFocus()
                    trySave()
                })
            )
            Spacer(Modifier.height(4.dp))
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                TextButton(onClick = onBack) { Text("Cancel") }
                Spacer(Modifier.height(0.dp))
                Button(onClick = {
                    focus.clearFocus()
                    trySave()
                }, enabled = title.isNotBlank()) {
                    Text("Save")
                }

            }



        }

    }
}