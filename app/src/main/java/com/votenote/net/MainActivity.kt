package com.votenote.net

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.votenote.net.ui.tabs.BottomTabs
import com.votenote.net.ui.theme.VoteNoteTheme

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoteNoteTheme {
                MainScreen()
            }
        }
    }

    companion object {
        const val APP_PREFERENCE = "APP_PREFERENCE"
        const val DEBUG_TAG = "DEBUG"
    }
}

@ExperimentalMaterialApi
@Preview(name = "Main Screen", showBackground = true)
@Composable
fun MainPreview() {
    VoteNoteTheme {
        MainScreen()
    }
}

@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomTabs = listOf(BottomTabs.Search, BottomTabs.Home, BottomTabs.Chats, BottomTabs.Profile)
    var selectedTabIndex: Int by remember { mutableStateOf(0) }

    Scaffold (
        contentColor = MaterialTheme.colors.secondary,
        bottomBar = {
            BottomNavigation {
                bottomTabs.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = { Icon(painterResource(item.tab.imageResourceID), contentDescription = null) },
                        label = { Text(item.tab.title) },
                        alwaysShowLabel = false,
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            navController.navigate(bottomTabs[selectedTabIndex].name)
                        },
                        selectedContentColor = MaterialTheme.colors.primaryVariant,
                        unselectedContentColor = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomTabs.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomTabs.Search.name) { SearchScreen() }
            composable(BottomTabs.Home.name) { HomeScreen() }
            composable(BottomTabs.Chats.name) {
                Text("Chats", modifier = Modifier
                .fillMaxSize()
                .wrapContentSize())
            }
            composable(BottomTabs.Profile.name) {
                Text("Profile", modifier = Modifier
                .fillMaxSize()
                .wrapContentSize())
            }
        }
    }
}

