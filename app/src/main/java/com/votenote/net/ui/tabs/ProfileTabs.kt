package com.votenote.net.ui.tabs

enum class ProfileTabs(val tab: TabModel) {
    MY(TabModel("My Polls", null)),
    LIKED(TabModel("Liked", null)),
    DISLIKED(TabModel("Disliked", null)),
}