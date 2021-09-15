package com.votenote.net

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.votenote.net.MainActivity.Companion.DEBUG_TAG
import com.votenote.net.ui.tabs.BottomTabs
import com.votenote.net.ui.theme.VoteNoteTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoteNoteTheme {
                /*// A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }*/

                MainScreen()
            }
        }
    }

    companion object {
        const val APP_PREFERENCE = "APP_PREFERENCE"
        const val DEBUG_TAG = "DEBUG"
    }
}

@Preview(name = "Main Screen", showBackground = true)
@Composable
fun MainPreview() {
    VoteNoteTheme {
        MainScreen()
    }
}

@Preview(name = "Home Screen", showBackground = true)
@Composable
fun HomePreview() {
    VoteNoteTheme {
        HomeScreen()
    }
}

@Composable
fun MainScreen() {
    val tabs = listOf(BottomTabs.Search, BottomTabs.Home, BottomTabs.Chats, BottomTabs.Profile)
    var selectedTabIndex: Int by remember { mutableStateOf(0) }

    Scaffold (
        contentColor = MaterialTheme.colors.secondary,
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = { Icon(painterResource(item.tab.imageResourceID), contentDescription = null) },
                        label = { Text(item.tab.title) },
                        alwaysShowLabel = false,
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        selectedContentColor = MaterialTheme.colors.primaryVariant,
                        unselectedContentColor = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    ) { innerPadding ->

        when (tabs[selectedTabIndex]) {
            BottomTabs.Search -> {
                Log.d(DEBUG_TAG, "Search")
                Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    Text("Search", color = MaterialTheme.colors.secondary)
                }
            }
            BottomTabs.Home -> {
                Log.d(DEBUG_TAG, "Home")
                HomeScreen()
            }
            BottomTabs.Chats -> {
                Log.d(DEBUG_TAG, "Chats")
                Text("Chats", modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize())
            }
            BottomTabs.Profile -> {
                Log.d(DEBUG_TAG, "Profile")
                Text("Profile", modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize())
            }
        }
    }
}
