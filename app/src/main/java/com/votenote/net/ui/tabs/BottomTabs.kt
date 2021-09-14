package com.votenote.net.ui.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTabs(val tab: TabModel) {
    Search(TabModel("Search", Icons.Outlined.Search)),
    Home(TabModel("Home", Icons.Outlined.Home)),
    Chats(TabModel("Chats", Icons.Outlined.Email)),
    Profile(TabModel("Profile", Icons.Outlined.Person)),
}