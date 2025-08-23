package com.android.myschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyScheduleAppRoot()
        }
    }


    @Composable
     fun MyScheduleAppRoot() {
        MaterialTheme {
            val navController = rememberNavController()
            Scaffold (
                topBar = { TopAppBar(title = {Text("My Schedule")}) },
                floatingActionButton = {
                    FloatingActionButton(onClick = navController.navigate("create")) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            ){ innerPadding ->
                AppNavHost(navController, Modifier.padding(innerPadding))
            }
        }
    }

    @Composable
    fun HomeScreen() {
        Text("Today’s plan goes here")
    }
    @Composable
    fun AppNavHost(navController: Any, modifier: Modifier = Modifier) {
        NavHost(navController = navController, startDestination = "home", modifier = modifier){
            composable("home"){HomeScreen}
            composable("create"){CreateTaskScreen(onBack = {navController.popBackStack()})}
        }

    }


    @Composable
    fun CreateTaskScreen(onBack: () -> Unit) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("Create Task") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Text(
                text = "Simple form placeholder",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    @Composable
    fun SmallTopAppBar(title: () -> Unit, navigationIcon: () -> IconButton) {
        TODO("Not yet implemented")
    }

}