package com.votenote.net

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.votenote.net.ui.tabs.BottomTabs
import com.votenote.net.ui.theme.VoteNoteTheme
import com.votenote.net.ui.tabs.TabModel

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
}

@Preview(name = "Navigation Drawer", showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {
    VoteNoteTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    Box(Modifier.fillMaxSize()) {
        BottomNavigationScreen()
    }
}

@Composable
fun BottomNavigationScreen() {
    var selectedItem: Int by remember { mutableStateOf(0) }
    val items = listOf(BottomTabs.Search, BottomTabs.Home, BottomTabs.Chats, BottomTabs.Profile)

    Column (verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
        BottomNavigation {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(item.tab.imageVector, contentDescription = null) },
                    label = { Text(item.tab.title) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    selectedContentColor = MaterialTheme.colors.primaryVariant,
                    unselectedContentColor = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
