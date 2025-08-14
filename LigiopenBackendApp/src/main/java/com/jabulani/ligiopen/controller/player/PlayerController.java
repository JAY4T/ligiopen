package com.jabulani.ligiopen.controller.player;

import com.jabulani.ligiopen.dto.player.PlayerRegistrationDto;
import com.jabulani.ligiopen.dto.player.PlayerUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface PlayerController {

    /**
     * Register a new player
     */
    @PostMapping("/registration")
    ResponseEntity<Object> registerPlayer(@RequestBody PlayerRegistrationDto registrationDto);

    /**
     * Get player by ID
     */
    @GetMapping("/{playerId}")
    ResponseEntity<Object> getPlayerById(@PathVariable Long playerId);

    /**
     * Update player profile
     */
    @PutMapping("/{playerId}")
    ResponseEntity<Object> updatePlayerProfile(@PathVariable Long playerId, @RequestBody PlayerUpdateDto updateDto);

    /**
     * Upload player main photo
     */
    @PostMapping("/{playerId}/photo")
    ResponseEntity<Object> uploadPlayerMainPhoto(@PathVariable Long playerId, @RequestParam("photo") MultipartFile photoFile);

    /**
     * Add additional player photo
     */
    @PostMapping("/{playerId}/photos")
    ResponseEntity<Object> addPlayerPhoto(@PathVariable Long playerId, @RequestParam("photo") MultipartFile photoFile);

    /**
     * Delete player main photo
     */
    @DeleteMapping("/{playerId}/photo")
    ResponseEntity<Object> deletePlayerMainPhoto(@PathVariable Long playerId);

    /**
     * Delete specific player photo
     */
    @DeleteMapping("/{playerId}/photos/{photoId}")
    ResponseEntity<Object> deletePlayerPhoto(@PathVariable Long playerId, @PathVariable Long photoId);

    /**
     * Search players by name
     */
    @GetMapping("/search")
    ResponseEntity<Object> searchPlayersByName(@RequestParam String query, 
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size);

    /**
     * Get players by position
     */
    @GetMapping("/position/{position}")
    ResponseEntity<Object> getPlayersByPosition(@PathVariable String position,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size);

    /**
     * Get players by nationality
     */
    @GetMapping("/nationality/{nationality}")
    ResponseEntity<Object> getPlayersByNationality(@PathVariable String nationality,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size);

    /**
     * Get players by age range
     */
    @GetMapping("/age-range")
    ResponseEntity<Object> getPlayersByAgeRange(@RequestParam int minAge,
                                              @RequestParam int maxAge,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size);

    /**
     * Get free agents
     */
    @GetMapping("/free-agents")
    ResponseEntity<Object> getFreeAgents(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size);

    /**
     * Get players by club
     */
    @GetMapping("/club/{clubId}")
    ResponseEntity<Object> getPlayersByClub(@PathVariable Long clubId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size);

    /**
     * Get all active players
     */
    @GetMapping("/active")
    ResponseEntity<Object> getAllActivePlayers(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size);

    /**
     * Get player's current club membership
     */
    @GetMapping("/{playerId}/current-club")
    ResponseEntity<Object> getCurrentClubMembership(@PathVariable Long playerId);

    /**
     * Get player's club history
     */
    @GetMapping("/{playerId}/club-history")
    ResponseEntity<Object> getPlayerClubHistory(@PathVariable Long playerId);

    /**
     * Get player statistics
     */
    @GetMapping("/{playerId}/statistics")
    ResponseEntity<Object> getPlayerStatistics(@PathVariable Long playerId);

    /**
     * Get player transfer history
     */
    @GetMapping("/{playerId}/transfers")
    ResponseEntity<Object> getPlayerTransferHistory(@PathVariable Long playerId);

    /**
     * Check if player is available for transfer
     */
    @GetMapping("/{playerId}/transfer-availability")
    ResponseEntity<Object> checkPlayerTransferAvailability(@PathVariable Long playerId);

    /**
     * Deactivate player
     */
    @PostMapping("/{playerId}/deactivate")
    ResponseEntity<Object> deactivatePlayer(@PathVariable Long playerId);

    /**
     * Reactivate player
     */
    @PostMapping("/{playerId}/reactivate")
    ResponseEntity<Object> reactivatePlayer(@PathVariable Long playerId);

    /**
     * Delete player (soft delete)
     */
    @DeleteMapping("/{playerId}")
    ResponseEntity<Object> deletePlayer(@PathVariable Long playerId);

    /**
     * Get player by email
     */
    @GetMapping("/email/{email}")
    ResponseEntity<Object> getPlayerByEmail(@PathVariable String email);

    /**
     * Get player by FKF registration number
     */
    @GetMapping("/fkf-registration/{registrationNumber}")
    ResponseEntity<Object> getPlayerByFkfRegistrationNumber(@PathVariable String registrationNumber);

    /**
     * Check if email is available
     */
    @GetMapping("/check-email")
    ResponseEntity<Object> checkEmailAvailability(@RequestParam String email);

    /**
     * Check if FKF registration number is available
     */
    @GetMapping("/check-fkf-registration")
    ResponseEntity<Object> checkFkfRegistrationAvailability(@RequestParam String registrationNumber);

    /**
     * Get recently registered players
     */
    @GetMapping("/recent")
    ResponseEntity<Object> getRecentlyRegisteredPlayers(@RequestParam(defaultValue = "10") int limit);

    /**
     * Get top valued players
     */
    @GetMapping("/top-valued")
    ResponseEntity<Object> getTopValuedPlayers(@RequestParam(defaultValue = "10") int limit);

    /**
     * Get players by preferred foot
     */
    @GetMapping("/preferred-foot/{foot}")
    ResponseEntity<Object> getPlayersByPreferredFoot(@PathVariable String foot,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size);

    /**
     * Get players by height range
     */
    @GetMapping("/height-range")
    ResponseEntity<Object> getPlayersByHeightRange(@RequestParam Double minHeight,
                                                  @RequestParam Double maxHeight,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size);
}