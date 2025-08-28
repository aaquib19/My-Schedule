package com.android.myschedule.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.myschedule.data.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onAddClicked: () -> Unit, vm : TaskViewModel = hiltViewModel()) {
    val tasks by vm.tasks.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ){
        inner ->
        if(tasks.isEmpty()){
            Text("No tasks yet. Tap + to add one.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(inner).padding(16.dp)
            )
        }
        else
        {
            LazyColumn (modifier = Modifier.padding(inner),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                items(tasks, key = {it.id}){
                    task ->
                    val onChecked = remember(task.id) {
                        { checked: Boolean  ->
                            vm.setDone(task.id, checked)
                            if (checked) {
                                // show UNDO when marking complete
                                scope.launch  {
                                    val res = snackbarHostState.showSnackbar(
                                        message = "Task completed",
                                        actionLabel = "UNDO",
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Short
                                    )
                                    if (res == SnackbarResult.ActionPerformed) {
                                        vm.undoDone(task.id)
                                    }
                                }
                            }
                        }
                    }
                    TaskRow(
                        task = task,
                        onCheckedChange = onChecked
                    )
                }
            }
        }


    }

}
@Composable
private fun TaskRow(
    task: Task,
    onCheckedChange: (Boolean) -> Unit
) {
    ElevatedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = onCheckedChange
            )
            Column(modifier = Modifier.weight(1f)) {
                val alpha = if (task.isDone) 0.6f else 1f
                val deco = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha(alpha),
                    textDecoration = deco
                )
                if (!task.notes.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        task.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(alpha)
                    )
                }
            }
        }
    }
}
