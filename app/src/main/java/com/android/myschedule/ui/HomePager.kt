package com.android.myschedule.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.android.myschedule.ui.theme.CalendarScreen
import kotlinx.coroutines.launch

@Composable
fun HomePager(navController: NavController) {
    val pagerState = rememberPagerState  (initialPage = 0, pageCount = {2})
    val scope = rememberCoroutineScope()


    HorizontalPager(state = pagerState,
//        modifier = Modifier.fillMaxSize(),
    ) {
        page ->
        when(page){
            0->
            {
                HomeScreen(
                    onAddClicked = {navController.navigate("create"){launchSingleTop = true} },
                    onEditTask = {id -> navController.navigate("edit/${id}")},
//                    onShowCalendar = {scope.launch { pagerState.animateScrollToPage(1) }}
                )
            }
            1 ->{
                CalendarScreen()
            }
        }
    }
}