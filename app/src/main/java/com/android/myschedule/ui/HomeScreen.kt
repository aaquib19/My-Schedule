package com.android.myschedule.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.myschedule.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClicked: () -> Unit,
    onEditTask: (Int) -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val formatter = remember { DateTimeFormatter.ofPattern("EEE, dd MMM") }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            DaySwitcherHeader(
                date = currentDate,
                onPrevious = {currentDate = currentDate.minusDays(1)},
                onNext = { currentDate = currentDate.plusDays(1)},
                formatter = formatter
            )
        }
        TaskContent(
            tasks = tasks,
            onEditTask = onEditTask,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState,
            scope = scope,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DaySwitcherHeader(
    date: LocalDate,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    formatter: DateTimeFormatter,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButton(onClick = onPrevious) {Icon(
            imageVector = Icons.Filled.KeyboardDoubleArrowLeft,
            contentDescription = "Previous Day"
        ) }
        Column {
            Text(text = date.format(formatter))
            if(date == LocalDate.now()){
                Text("Today")}
        }
        IconButton(onClick = onPrevious) {Icon(
            imageVector = Icons.Filled.KeyboardDoubleArrowRight,
            contentDescription = "Next Day"
        ) }
    }

}

@Composable
private fun TaskContent(
    tasks: List<Task>,
    onEditTask: (Int) -> Unit,
    viewModel: TaskViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        EmptyTasksState(modifier = modifier)
    } else {
        TaskList(
            tasks = tasks,
            onEditTask = onEditTask,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState,
            scope = scope,
            modifier = modifier
        )
    }
}

@Composable
private fun EmptyTasksState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No tasks yet",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to add your first task",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TaskList(
    tasks: List<Task>,
    onEditTask: (Int) -> Unit,
    viewModel: TaskViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = tasks,
            key = { it.id }
        ) { task ->
            SwipeableTaskItem(
                task = task,
                onEditTask = onEditTask,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableTaskItem(
    task: Task,
    onEditTask: (Int) -> Unit,
    viewModel: TaskViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    handleTaskToggle(task, viewModel, scope, snackbarHostState)
                    false // Reset the swipe state
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    handleTaskDeletion(task, viewModel, scope, snackbarHostState)
                    true // Allow dismissal
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            SwipeBackground(dismissValue = swipeState.dismissDirection)
        }
    ) {
        TaskRow(
            task = task,
            onCheckedChange = { isChecked ->
                handleTaskToggle(task, viewModel, scope, snackbarHostState, isChecked)
            },
            onClick = { onEditTask(task.id) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(
    dismissValue: SwipeToDismissBoxValue
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = when (dismissValue) {
            SwipeToDismissBoxValue.StartToEnd -> Arrangement.Start
            SwipeToDismissBoxValue.EndToStart -> Arrangement.End
            else -> Arrangement.End
        }
    ) {
        Icon(
            imageVector = when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Filled.Add // Or use a check icon
                else -> Icons.Filled.Delete
            },
            contentDescription = when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> "Toggle Complete"
                else -> "Delete"
            },
            tint = when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.error
            }
        )
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = onCheckedChange
            )

            TaskContent(
                task = task,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TaskContent(
    task: Task,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val alpha = if (task.isDone) 0.6f else 1f
        val textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None

        Text(
            text = task.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.alpha(alpha),
            textDecoration = textDecoration
        )

        if (!task.notes.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.notes,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(alpha),
                textDecoration = textDecoration
            )
        }
    }
}

// Business logic functions
private fun handleTaskToggle(
    task: Task,
    viewModel: TaskViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    isChecked: Boolean? = null
) {
    val newStatus = isChecked ?: !task.isDone
    viewModel.setDone(task.id, newStatus)

    if (newStatus) {
        showCompletionSnackbar(task, viewModel, scope, snackbarHostState)
    }
}

private fun handleTaskDeletion(
    task: Task,
    viewModel: TaskViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    viewModel.deleteTask(task)
    showDeletionSnackbar(task, viewModel, scope, snackbarHostState)
}

private fun showCompletionSnackbar(
    task: Task,
    viewModel: TaskViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = "Task completed",
            actionLabel = "UNDO",
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.undoDone(task.id)
        }
    }
}

private fun showDeletionSnackbar(
    task: Task,
    viewModel: TaskViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = "Task deleted",
            actionLabel = "UNDO",
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.undoDelete(task)
        }
    }
}