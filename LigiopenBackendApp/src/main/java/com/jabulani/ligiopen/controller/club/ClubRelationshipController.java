package com.jabulani.ligiopen.controller.club;

import org.springframework.http.ResponseEntity;

public interface ClubRelationshipController {

    /**
     * Add club to user's favorites
     */
    ResponseEntity<Object> addClubToFavorites(Long clubId);

    /**
     * Remove club from user's favorites
     */
    ResponseEntity<Object> removeClubFromFavorites(Long clubId);

    /**
     * Check if user has favorited a club
     */
    ResponseEntity<Object> isClubFavorited(Long clubId);

    /**
     * Get user's favorite clubs
     */
    ResponseEntity<Object> getUserFavoriteClubs(int page, int size);

    /**
     * Get users who have favorited a club
     */
    ResponseEntity<Object> getClubFavorites(Long clubId, int page, int size);

    /**
     * Get club favorites count
     */
    ResponseEntity<Object> getClubFavoritesCount(Long clubId);

    /**
     * Get clubs user owns
     */
    ResponseEntity<Object> getUserOwnedClubs();

    /**
     * Get clubs user manages
     */
    ResponseEntity<Object> getUserManagedClubs();

    /**
     * Get user's club relationships summary
     */
    ResponseEntity<Object> getUserClubRelationships();

    /**
     * Get popular clubs (most favorited)
     */
    ResponseEntity<Object> getPopularClubs(int page, int size);

    /**
     * Get recommended clubs for user (based on location, favorites, etc.)
     */
    ResponseEntity<Object> getRecommendedClubs(int page, int size);
}