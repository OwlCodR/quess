package com.votenote.net

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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