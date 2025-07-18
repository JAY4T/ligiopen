package com.jabulani.ligiopen.data.network.model.player

enum class PlayerState {
    ACTIVE,       // Players currently on the field
    INJURED,
    BENCH,        // Players on the bench, available for substitution
    INACTIVE
}