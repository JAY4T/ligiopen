package com.jabulani.ligiopen.data.network.model.match.fixture

enum class MatchStatus {
    PENDING,          // Match has been scheduled but not started
    FIRST_HALF,       // First half is currently being played
    HALF_TIME,        // Break between the first and second half
    SECOND_HALF,      // Second half is currently being played
    EXTRA_TIME_FIRST_HALF,  // First half of extra time (if applicable)
    EXTRA_TIME_HALF_TIME,   // Break between extra time halves
    EXTRA_TIME_SECOND_HALF, // Second half of extra time
    PENALTY_SHOOTOUT, // If the match proceeds to penalties
    COMPLETED,        // Match has ended
    CANCELLED         // Match has been canceled
}