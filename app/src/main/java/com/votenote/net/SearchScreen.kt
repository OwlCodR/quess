package com.votenote.net

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.votenote.net.ui.tabs.GlobalSearchTabs
import com.votenote.net.ui.theme.Black50
import com.votenote.net.ui.theme.VoteNoteTheme

@ExperimentalMaterialApi
@Preview(name = "Search Screen", showBackground = true)
@Composable
fun SearchPreview() {
    VoteNoteTheme {
        SearchScreen()
    }
}

@ExperimentalMaterialApi
@Composable
fun SearchTopAppBar() {
    TopAppBar (
        elevation = 0.dp,
        contentPadding = PaddingValues(20.dp, 5.dp),
        contentColor = MaterialTheme.colors.secondary
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp, 5.dp)
        ) {
            Card (
                onClick = {},
                elevation = 5.dp,
                shape = RoundedCornerShape(14.dp),
                contentColor = MaterialTheme.colors.secondary,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(10.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_28),
                        contentDescription = "Search",
                        modifier = Modifier.alpha(0.5F)
                    )

                    Text(
                        text="Search here",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.proxima_nova_regular)),
                        color = Black50,
                        modifier = Modifier.padding(6.dp)
                    )

                    /*BasicTextField(
                        value = "Search here",
                        //placeholder = { Text("Search here", modifier = Modifier.padding(0.dp)) },
                        onValueChange = { },
                        readOnly = true,
                        *//*colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            textColor = MaterialTheme.colors.secondary,
                            placeholderColor = Black50,
                            cursorColor = Black50,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),*//*
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.proxima_nova_regular)),
                            color = Black50
                        ),
                        modifier = Modifier.padding(5.dp, 12.dp)
                    )*/
                }
            }

            Text(
                text = "Search",
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
fun GlobalSearchContent() {

}

@Composable
fun GlobalSearchTabRow() {
    var state by remember { mutableStateOf(0) }
    val globalSearchTabs = listOf(GlobalSearchTabs.TOP, GlobalSearchTabs.NEW, GlobalSearchTabs.HOT)

    Column {
        TabRow(
            contentColor = MaterialTheme.colors.primaryVariant,
            selectedTabIndex = state
        ) {
            globalSearchTabs.forEachIndexed { index, title ->
                Tab(
                    // TODO("Disable ripple effect")
                    selectedContentColor = MaterialTheme.colors.primaryVariant,
                    unselectedContentColor = MaterialTheme.colors.secondary,
                    text = { Text(globalSearchTabs[index].name) },
                    selected = state == index,
                    onClick = { state = index },
                )
            }
        }
        GlobalSearchContent()
    }
}

@ExperimentalMaterialApi
@Composable
fun SearchScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchTopAppBar()
        GlobalSearchTabRow()
    }
}