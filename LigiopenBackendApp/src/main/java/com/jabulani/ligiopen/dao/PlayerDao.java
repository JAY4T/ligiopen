package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.entity.player.ClubInvitation;
import com.jabulani.ligiopen.entity.club.Club;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    
    /**
     * Create a new player
     */
    Player createPlayer(Player player);
    
    /**
     * Update an existing player
     */
    Player updatePlayer(Player player);
    
    /**
     * Get player by ID
     */
    Player getPlayerById(Long id);
    
    /**
     * Find player by ID (optional)
     */
    Optional<Player> findPlayerById(Long id);
    
    /**
     * Get player by email
     */
    Optional<Player> getPlayerByEmail(String email);
    
    /**
     * Get player by FKF registration number
     */
    Optional<Player> getPlayerByFkfRegistrationNumber(String fkfRegistrationNumber);
    
    /**
     * Get all active players
     */
    List<Player> getActivePlayers();
    
    /**
     * Get all players (including inactive)
     */
    List<Player> getAllPlayers();
    
    /**
     * Search players by name (first name or last name)
     */
    List<Player> searchPlayersByName(String query, int offset, int limit);
    
    /**
     * Get players by position
     */
    List<Player> getPlayersByPosition(Player.Position position, int offset, int limit);
    
    /**
     * Get players by nationality
     */
    List<Player> getPlayersByNationality(String nationality, int offset, int limit);
    
    /**
     * Get players by birth date range
     */
    List<Player> getPlayersByBirthDateRange(LocalDate startDate, LocalDate endDate, int offset, int limit);
    
    /**
     * Get players by club
     */
    List<Player> getPlayersByClub(Club club);
    
    /**
     * Get players by club ID
     */
    List<Player> getPlayersByClubId(Long clubId);
    
    /**
     * Get free agents (players without active club membership)
     */
    List<Player> getFreeAgents(int offset, int limit);
    
    /**
     * Get players with active club memberships
     */
    List<Player> getPlayersWithActiveClubs(int offset, int limit);
    
    /**
     * Get recently registered players
     */
    List<Player> getRecentlyRegisteredPlayers(int limit);
    
    /**
     * Get top valued players
     */
    List<Player> getTopValuedPlayers(int limit);
    
    /**
     * Get players by preferred foot
     */
    List<Player> getPlayersByPreferredFoot(Player.PreferredFoot preferredFoot, int offset, int limit);
    
    /**
     * Get players by height range
     */
    List<Player> getPlayersByHeightRange(Double minHeight, Double maxHeight, int offset, int limit);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if FKF registration number exists
     */
    boolean existsByFkfRegistrationNumber(String fkfRegistrationNumber);
    
    /**
     * Check if ID number exists
     */
    boolean existsByIdNumber(String idNumber);
    
    /**
     * Check if passport number exists
     */
    boolean existsByPassportNumber(String passportNumber);
    
    /**
     * Delete player (soft delete by setting isActive = false)
     */
    void deletePlayer(Long playerId);
    
    /**
     * Hard delete player from database
     */
    void hardDeletePlayer(Long playerId);
    
    /**
     * Count total players
     */
    long countAllPlayers();
    
    /**
     * Count active players
     */
    long countActivePlayers();
    
    /**
     * Count players by club
     */
    long countPlayersByClub(Long clubId);
    
    /**
     * Count free agents
     */
    long countFreeAgents();
    
    // Club Membership related methods
    
    /**
     * Get current club membership for a player
     */
    Optional<ClubMembership> getCurrentClubMembership(Long playerId);
    
    /**
     * Get all club memberships for a player (history)
     */
    List<ClubMembership> getPlayerClubMemberships(Long playerId);
    
    /**
     * Get active club memberships for a club
     */
    List<ClubMembership> getActiveClubMemberships(Long clubId);
    
    /**
     * Get club membership by player and club
     */
    Optional<ClubMembership> getClubMembershipByPlayerAndClub(Long playerId, Long clubId);
    
    // Transfer related methods
    
    /**
     * Get player transfer history
     */
    List<PlayerTransfer> getPlayerTransferHistory(Long playerId);
    
    /**
     * Get pending transfers for a player
     */
    List<PlayerTransfer> getPendingTransfersForPlayer(Long playerId);
    
    /**
     * Get transfers by club (incoming and outgoing)
     */
    List<PlayerTransfer> getTransfersByClub(Long clubId);
    
    /**
     * Get recent transfers
     */
    List<PlayerTransfer> getRecentTransfers(int limit);
    
    /**
     * Check if player has pending transfer
     */
    boolean hasPlayerPendingTransfer(Long playerId);
    
    // Additional methods needed for ClubMembershipService
    
    /**
     * Get player by ID or throw exception
     */
    Player getPlayerByIdOrThrow(Long playerId);
    
    /**
     * Save or update club membership
     */
    ClubMembership saveClubMembership(ClubMembership membership);
    
    /**
     * Get club membership by ID or throw exception
     */
    ClubMembership getClubMembershipByIdOrThrow(Long membershipId);
    
    /**
     * Get active club memberships for a club with pagination
     */
    List<ClubMembership> getActiveClubMembershipsByClub(Long clubId, PageRequest pageRequest);
    
    /**
     * Get all club memberships for a club with pagination
     */
    List<ClubMembership> getAllClubMembershipsByClub(Long clubId, PageRequest pageRequest);
    
    /**
     * Get club memberships by position
     */
    List<ClubMembership> getClubMembershipsByPosition(Long clubId, Player.Position position, PageRequest pageRequest);
    
    /**
     * Get club captain
     */
    ClubMembership getClubCaptain(Long clubId);
    
    /**
     * Get club vice captain
     */
    ClubMembership getClubViceCaptain(Long clubId);
    
    /**
     * Get memberships expiring before date
     */
    List<ClubMembership> getMembershipsExpiringBefore(LocalDate cutoffDate, PageRequest pageRequest);
    
    /**
     * Get club memberships by status
     */
    List<ClubMembership> getClubMembershipsByStatus(Long clubId, ClubMembership.MembershipStatus status, PageRequest pageRequest);
    
    /**
     * Check if jersey number is available
     */
    boolean isJerseyNumberAvailable(Long clubId, Integer jerseyNumber, Long excludeMembershipId);
    
    /**
     * Get taken jersey numbers for a club
     */
    List<Integer> getTakenJerseyNumbers(Long clubId);
    
    // ClubInvitation related methods
    
    /**
     * Save or update club invitation
     */
    ClubInvitation saveClubInvitation(ClubInvitation invitation);
    
    /**
     * Get club invitation by ID or throw exception
     */
    ClubInvitation getClubInvitationByIdOrThrow(Long invitationId);
    
    /**
     * Get club invitations by player and status
     */
    List<ClubInvitation> getClubInvitationsByPlayerAndStatus(Long playerId, ClubInvitation.InvitationStatus status, PageRequest pageRequest);
    
    /**
     * Get all club invitations for a player
     */
    List<ClubInvitation> getClubInvitationsByPlayer(Long playerId, PageRequest pageRequest);
    
    /**
     * Get club invitations by club and status
     */
    List<ClubInvitation> getClubInvitationsByClubAndStatus(Long clubId, ClubInvitation.InvitationStatus status, PageRequest pageRequest);
    
    /**
     * Get all club invitations for a club
     */
    List<ClubInvitation> getClubInvitationsByClub(Long clubId, PageRequest pageRequest);
    
    /**
     * Get club invitations by inviter
     */
    List<ClubInvitation> getClubInvitationsByInviter(Long userId, PageRequest pageRequest);
    
    /**
     * Get expired club invitations
     */
    List<ClubInvitation> getExpiredClubInvitations(PageRequest pageRequest);
    
    /**
     * Get club invitations expiring before date
     */
    List<ClubInvitation> getClubInvitationsExpiringBefore(LocalDateTime cutoffTime, PageRequest pageRequest);
    
    /**
     * Get pending invitations expired before date
     */
    List<ClubInvitation> getPendingInvitationsExpiredBefore(LocalDateTime cutoffTime);
    
    /**
     * Get club invitations by club after date
     */
    List<ClubInvitation> getClubInvitationsByClubAfterDate(Long clubId, LocalDateTime afterDate, PageRequest pageRequest);
    
    /**
     * Check if player has pending invitation from club
     */
    boolean hasPlayerPendingInvitationFromClub(Long playerId, Long clubId);
    
    /**
     * Count club invitations by player and status
     */
    long countClubInvitationsByPlayerAndStatus(Long playerId, ClubInvitation.InvitationStatus status);
    
    /**
     * Count club invitations by club and status
     */
    long countClubInvitationsByClubAndStatus(Long clubId, ClubInvitation.InvitationStatus status);
    
    /**
     * Count club invitations by club
     */
    long countClubInvitationsByClub(Long clubId);
}