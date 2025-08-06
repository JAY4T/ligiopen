package com.jabulani.ligiopen.ui.inapp.home

enum class HomeScreenTab {
    NEWS,
    HOME,
    SCORES,
    CLUBS,
    MATCHES,
    FINANCING,
    PROFILE,
    STANDINGS
}

data class HomeScreenTabItem(
    val name: String,
    val icon: Int,
    val tab: HomeScreenTab
)