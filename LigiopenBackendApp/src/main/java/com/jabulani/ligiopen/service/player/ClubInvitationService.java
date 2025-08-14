package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.entity.player.ClubInvitation;
import com.jabulani.ligiopen.entity.player.Player;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ClubInvitationService {
    
    /**
     * Create a club invitation to a player
     * @param clubId Club ID
     * @param playerId Player ID
     * @param invitedByUserId User ID of the person sending the invitation
     * @param proposedPosition Proposed position for the player
     * @param proposedJerseyNumber Proposed jersey number
     * @param proposedWage Proposed weekly wage
     * @param message Invitation message
     * @param expiresAt Invitation expiry date
     * @return Created ClubInvitation entity
     */
    ClubInvitation createClubInvitation(Long clubId, Long playerId, Long invitedByUserId,
                                      Player.Position proposedPosition, Integer proposedJerseyNumber,
                                      BigDecimal proposedWage, String message, LocalDateTime expiresAt);
    
    /**
     * Accept a club invitation
     * @param invitationId Invitation ID
     * @param playerId Player ID (for security verification)
     * @return Updated ClubInvitation entity
     */
    ClubInvitation acceptClubInvitation(Long invitationId, Long playerId);
    
    /**
     * Reject a club invitation
     * @param invitationId Invitation ID
     * @param playerId Player ID (for security verification)
     * @param rejectionReason Optional rejection reason
     * @return Updated ClubInvitation entity
     */
    ClubInvitation rejectClubInvitation(Long invitationId, Long playerId, String rejectionReason);
    
    /**
     * Cancel a club invitation (by club/inviter)
     * @param invitationId Invitation ID
     * @param userId User ID (must be club owner/manager)
     * @param cancellationReason Optional cancellation reason
     * @return Updated ClubInvitation entity
     */
    ClubInvitation cancelClubInvitation(Long invitationId, Long userId, String cancellationReason);
    
    /**
     * Update club invitation details (before acceptance)
     * @param invitationId Invitation ID
     * @param userId User ID (must be club owner/manager)
     * @param proposedPosition New proposed position
     * @param proposedJerseyNumber New proposed jersey number
     * @param proposedWage New proposed wage
     * @param message Updated message
     * @param expiresAt Updated expiry date
     * @return Updated ClubInvitation entity
     */
    ClubInvitation updateClubInvitation(Long invitationId, Long userId, Player.Position proposedPosition,
                                      Integer proposedJerseyNumber, BigDecimal proposedWage,
                                      String message, LocalDateTime expiresAt);
    
    /**
     * Get club invitation by ID
     * @param invitationId Invitation ID
     * @return ClubInvitation entity
     */
    ClubInvitation getClubInvitationById(Long invitationId);
    
    /**
     * Get pending invitations for a player
     * @param playerId Player ID
     * @param pageRequest Pagination information
     * @return List of pending invitations
     */
    List<ClubInvitation> getPendingInvitationsForPlayer(Long playerId, PageRequest pageRequest);
    
    /**
     * Get all invitations for a player (including accepted/rejected/expired)
     * @param playerId Player ID
     * @param pageRequest Pagination information
     * @return List of all invitations for the player
     */
    List<ClubInvitation> getAllInvitationsForPlayer(Long playerId, PageRequest pageRequest);
    
    /**
     * Get invitations sent by a club
     * @param clubId Club ID
     * @param status Optional invitation status filter
     * @param pageRequest Pagination information
     * @return List of invitations sent by the club
     */
    List<ClubInvitation> getInvitationsByClub(Long clubId, ClubInvitation.InvitationStatus status, PageRequest pageRequest);
    
    /**
     * Get invitations sent by a user
     * @param userId User ID
     * @param pageRequest Pagination information
     * @return List of invitations sent by the user
     */
    List<ClubInvitation> getInvitationsSentByUser(Long userId, PageRequest pageRequest);
    
    /**
     * Get expired invitations
     * @param pageRequest Pagination information
     * @return List of expired invitations
     */
    List<ClubInvitation> getExpiredInvitations(PageRequest pageRequest);
    
    /**
     * Get invitations expiring soon
     * @param withinHours Number of hours to check
     * @param pageRequest Pagination information
     * @return List of invitations expiring within specified hours
     */
    List<ClubInvitation> getInvitationsExpiringSoon(int withinHours, PageRequest pageRequest);
    
    /**
     * Check if player has pending invitation from club
     * @param playerId Player ID
     * @param clubId Club ID
     * @return True if player has pending invitation from club
     */
    boolean hasPlayerPendingInvitationFromClub(Long playerId, Long clubId);
    
    /**
     * Count pending invitations for a player
     * @param playerId Player ID
     * @return Number of pending invitations
     */
    long countPendingInvitationsForPlayer(Long playerId);
    
    /**
     * Count invitations sent by a club
     * @param clubId Club ID
     * @param status Optional invitation status filter
     * @return Number of invitations sent by the club
     */
    long countInvitationsByClub(Long clubId, ClubInvitation.InvitationStatus status);
    
    /**
     * Process expired invitations (mark as expired)
     * @return Number of invitations marked as expired
     */
    int processExpiredInvitations();
    
    /**
     * Get club invitation statistics
     * @param clubId Club ID
     * @return Statistics map
     */
    java.util.Map<String, Object> getClubInvitationStatistics(Long clubId);
    
    /**
     * Get player invitation statistics
     * @param playerId Player ID
     * @return Statistics map
     */
    java.util.Map<String, Object> getPlayerInvitationStatistics(Long playerId);
    
    /**
     * Bulk cancel invitations (for cleanup)
     * @param invitationIds List of invitation IDs to cancel
     * @param userId User ID performing the action
     * @param cancellationReason Reason for cancellation
     * @return Number of invitations cancelled
     */
    int bulkCancelInvitations(List<Long> invitationIds, Long userId, String cancellationReason);
    
    /**
     * Check if user can manage invitation (is club owner/manager)
     * @param invitationId Invitation ID
     * @param userId User ID
     * @return True if user can manage the invitation
     */
    boolean canUserManageInvitation(Long invitationId, Long userId);
    
    /**
     * Check if player can respond to invitation
     * @param invitationId Invitation ID
     * @param playerId Player ID
     * @return True if player can respond to the invitation
     */
    boolean canPlayerRespondToInvitation(Long invitationId, Long playerId);
}