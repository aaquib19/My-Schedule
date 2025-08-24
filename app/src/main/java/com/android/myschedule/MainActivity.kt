package com.android.myschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.TopAppBar
import com.android.myschedule.ui.CreateTaskScreen
import com.android.myschedule.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyScheduleAppRoot()
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
     fun MyScheduleAppRoot() {
        MaterialTheme {
            val navController = rememberNavController()
            Scaffold (
                topBar = { TopAppBar(title = {Text("My Schedule")}) },
                floatingActionButton = {
                    FloatingActionButton(onClick = {navController.navigate("create")}) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            ){ innerPadding ->
                AppNavHost(navController, Modifier.padding(innerPadding))
            }
        }
    }

    @Composable
    fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
        NavHost(navController = navController, startDestination = "home", modifier = modifier){
            composable("home"){HomeScreen(onAddClicked = {navController.navigate("create")})}
            composable("create"){CreateTaskScreen(onBack = {navController.popBackStack()})}
        }

    }

}