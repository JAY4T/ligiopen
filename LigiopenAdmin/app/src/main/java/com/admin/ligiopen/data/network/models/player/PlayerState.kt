package com.admin.ligiopen.data.network.models.player

enum class PlayerState {
    ACTIVE,       // Players currently on the field
    INJURED,
    BENCH,        // Players on the bench, available for substitution
    INACTIVE
}