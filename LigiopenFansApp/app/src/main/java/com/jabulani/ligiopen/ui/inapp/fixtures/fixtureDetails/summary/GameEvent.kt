package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.summary

import com.jabulani.ligiopen.R


enum class EventType {
    GOAL,
    OWN_GOAL,
    YELLOW_CARD,
    RED_CARD,
    SUBSTITUTION
}

data class Substitution(
    val playerIn: String? = null,
    val playerOut: String? = null,
)

data class GameEvent (
    val eventType: EventType,
    val minute: String,
    val team: String,
    val home: Boolean,
    val player: String?,
    val eventIcon: Int,
    val substitution: Substitution
)

val matchEvents = listOf(
    GameEvent(
        eventType = EventType.GOAL,
        minute = "16",
        team = "RBNY",
        home = false,
        player = "F.Carballo",
        eventIcon = R.drawable.goal,
        substitution = Substitution()
    ),
    GameEvent(
        eventType = EventType.GOAL,
        minute = "25",
        team = "RBNY",
        home = false,
        player = "D.Vanzeir",
        eventIcon = R.drawable.goal,
        substitution = Substitution()
    ),
    GameEvent(
        eventType = EventType.YELLOW_CARD,
        minute = "28",
        team = "RBNY",
        home = false,
        player = "D.Edelman",
        eventIcon = R.drawable.yellow_card,
        substitution = Substitution()
    ),
    GameEvent(
        eventType = EventType.YELLOW_CARD,
        minute = "28",
        team = "RBNY",
        home = false,
        player = "D.Edelman",
        eventIcon = R.drawable.yellow_card,
        substitution = Substitution()
    ),
    GameEvent(
        eventType = EventType.YELLOW_CARD,
        minute = "33",
        team = "NYC",
        home = true,
        player = "J.Sands",
        eventIcon = R.drawable.yellow_card,
        substitution = Substitution()
    ),
    GameEvent(
        eventType = EventType.YELLOW_CARD,
        minute = "39",
        team = "NYC",
        home = true,
        player = "K.O'Toole",
        eventIcon = R.drawable.yellow_card,
        substitution = Substitution()
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "62",
        team = "RBNY",
        home = false,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "N.Eile",
            playerOut = "F.Carballo"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "62",
        team = "RBNY",
        home = false,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "P.Stroud",
            playerOut = "F.Carballo"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "74",
        team = "NYC",
        home = true,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "A.Perea",
            playerOut = "H.Wolf"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "82",
        team = "NYC",
        home = true,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "A.Ojeda",
            playerOut = "M.Ilenic"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "89",
        team = "RBNY",
        home = false,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "W.Carmona",
            playerOut = "E.Forsberg"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "90",
        team = "NYC",
        home = true,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "J.Fernandez",
            playerOut = "K.O'Toole"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "90",
        team = "RBNY",
        home = false,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "S.Ngoma Jr",
            playerOut = "D.Vanzeir"
        )
    ),
    GameEvent(
        eventType = EventType.SUBSTITUTION,
        minute = "90'+7",
        team = "RBNY",
        home = false,
        player = null,
        eventIcon = R.drawable.substitution,
        substitution = Substitution(
            playerIn = "E.Manoel",
            playerOut = "L.Morgan"
        )
    ),
)