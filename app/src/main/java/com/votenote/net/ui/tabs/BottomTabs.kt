package com.votenote.net.ui.tabs

import com.votenote.net.R

enum class BottomTabs(val tab: TabModel) {
    Search(TabModel("Search", R.drawable.ic_search_28)),
    Home(TabModel("Home", R.drawable.ic_outline_home_28)),
    Chats(TabModel("Chats", R.drawable.ic_outline_chats_24)),
    Profile(TabModel("Profile", R.drawable.ic_menu_profile_28))
}