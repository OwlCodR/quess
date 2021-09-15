package com.votenote.net

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun HomeTopAppBar() {
    TopAppBar (
        contentPadding = PaddingValues(20.dp, 2.dp),
        contentColor = MaterialTheme.colors.secondary
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Home",
                fontFamily = FontFamily(Font(R.font.proxima_nova_black)),
                fontSize = 24.sp,
                modifier = Modifier.wrapContentSize()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton (onClick = {}) {
                    Icon(
                        painterResource(id = R.drawable.ic_filter_28),
                        contentDescription = "Filter"
                    )
                }
                IconButton (onClick = {}) {
                    Icon(
                        painterResource(id = R.drawable.ic_notifications_28),
                        contentDescription = "Notifications"
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopAppBar()
        Text(text = "Home")
    }
}

@Composable
fun MainScreen() {
    val tabs = listOf(BottomTabs.Search, BottomTabs.Home, BottomTabs.Chats, BottomTabs.Profile)
    var selectedTabIndex: Int by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomNavigationScreen(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabClicked = { index -> selectedTabIndex = index }
        )

        when (tabs[selectedTabIndex]) {
            BottomTabs.Search -> {
                Log.d(DEBUG_TAG, "Search")
                Text("Search")
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

        /*Column (modifier = Modifier.fillMaxSize()) {


        }*/
    }
}

@Composable
fun BottomNavigationScreen(tabs: List<BottomTabs>, selectedTabIndex: Int, onTabClicked: (Int) -> Unit) {
    Column (verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
        BottomNavigation {
            tabs.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(item.tab.imageResourceID), contentDescription = null) },
                    label = { Text(item.tab.title) },
                    alwaysShowLabel = false,
                    selected = selectedTabIndex == index,
                    onClick = { onTabClicked(index) },
                    selectedContentColor = MaterialTheme.colors.primaryVariant,
                    unselectedContentColor = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
