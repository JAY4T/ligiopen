package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.stats

// Data class for statistics
data class MatchStatistic(
    val label: String,
    val homeValue: String,
    val awayValue: String
)

data class MatchStatistics(
    val homeTeamName: String,
    val awayTeamName: String,
    val statistics: List<MatchStatistic>
)

val sampleStats = MatchStatistics(
    homeTeamName = "ORL",
    awayTeamName = "ALT",
    statistics = listOf(
        MatchStatistic("Shots", "10", "12"),
        MatchStatistic("Shots on Target", "6", "7"),
        MatchStatistic("Possession", "55%", "45%"),
        MatchStatistic("Passing Accuracy", "82%", "79%"),
        MatchStatistic("Corners", "5", "4"),
        MatchStatistic("Fouls", "12", "15"),
        MatchStatistic("Offsides", "3", "2"),
        MatchStatistic("Yellow Cards", "2", "3"),
        MatchStatistic("Red Cards", "0", "1")
    )
)