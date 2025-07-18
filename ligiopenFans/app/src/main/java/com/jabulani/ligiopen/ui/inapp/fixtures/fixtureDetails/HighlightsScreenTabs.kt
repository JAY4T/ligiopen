package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails

enum class HighlightsScreenTabs {
    SUMMARY,
    TIMELINE,
    LINEUPS,
    STATS,
    EDIT
}

data class HighlightsScreenTabItem(
    val name: String,
    val icon: Int,
    val tab: HighlightsScreenTabs
)
