package com.votenote.net

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.motion.widget.Debug.getLocation
import com.google.gson.Gson
import com.votenote.net.retrofit.model.User
import com.votenote.net.ui.tabs.ProfileTabs
import com.votenote.net.ui.theme.Black
import com.votenote.net.ui.theme.Black10
import com.votenote.net.ui.theme.Black30
import com.votenote.net.ui.theme.Black50

@Composable
fun ProfileTopAppBar(isMyProfile: Boolean) {
    // @TODO("Remake other app bars like this")

    TopAppBar (
        title = { if (isMyProfile) "My Profile" else "Profile" },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        contentColor = MaterialTheme.colors.secondary,
        actions = {
            IconButton (onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_more_vert_24),
                    contentDescription = "Options"
                )
            }
        }
    )
}

@Composable
fun ProfileScreen(tag: String?) {
    val sharedPreferences = LocalContext.current
        .getSharedPreferences(MainActivity.APP_PREFERENCE, Context.MODE_PRIVATE)
    val user: User? = Gson().fromJson(sharedPreferences.getString("user", null), User::class.java)

    var isMyProfile = false

    if (user != null) {
        isMyProfile = user.tag.equals(tag)
    } else {
        Log.d(MainActivity.DEBUG_TAG, "${getLocation()} Unpredictable! user == null")
    }

    ProfileTopAppBar(isMyProfile)
    Column(modifier = Modifier.fillMaxSize()) {
        UserInfo()
        ProfileTabs(isMyProfile)
    }


    LazyColumn{

    }
    // list of polls
}

@Composable
fun ProfileTabs(isMyProfile: Boolean) {

    /* Sections: Polls, Liked, Disliked or nothing */

    if (isMyProfile) {
        var state by remember { mutableStateOf(0) }
        val profileTabs = listOf(ProfileTabs.MY, ProfileTabs.LIKED, ProfileTabs.DISLIKED)

        Column {
            TabRow(
                contentColor = MaterialTheme.colors.primaryVariant,
                selectedTabIndex = state
            ) {
                profileTabs.forEachIndexed { index, title ->
                    Tab(
                        // TODO("Disable ripple effect")
                        selectedContentColor = MaterialTheme.colors.primaryVariant,
                        unselectedContentColor = MaterialTheme.colors.secondary,
                        text = { Text(profileTabs[index].name) },
                        selected = state == index,
                        onClick = { state = index },
                    )
                }
            }
        }
    } else {
        Divider(color = Black10, thickness = 1.dp)
    }
}

@Composable
fun UserInfo() {
    Column(modifier = Modifier.padding(20.dp)) {
        Row() {
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(R.drawable.profile_logo),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)                          // clip to the circle shape
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Petrov Vasily",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.proxima_nova_bold)),
                        color = Black,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 5.dp)
                    ) {
                        Text(
                            text = "@the_end",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.proxima_nova_bold)),
                            color = Black50,
                        )
                        Text(
                            text = "last seen yesterday",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.proxima_nova_light)),
                            color = Black30,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "About me",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.proxima_nova_bold)),
            color = Black,
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Everything beloved spring. Help evil find acceptance vexed boy feeling understood large express continue was thought adieus aware either. Agreeable pressed entrance set sense worth savings conduct written engrossed. ",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.proxima_nova_regular)),
            color = Black50,
        )

        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            InfoItem("124", "Polls")
            InfoItem("23", "Communities")
            InfoItem("453", "Followers")
            InfoItem("114", "Followings")
        }
    }
}

@Composable
fun InfoItem(count: String, subtitle: String) {
    Column(
        modifier = Modifier.padding(10.dp, 0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.proxima_nova_bold)),
            color = Black,
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.proxima_nova_regular)),
            color = Black,
        )
    }
}