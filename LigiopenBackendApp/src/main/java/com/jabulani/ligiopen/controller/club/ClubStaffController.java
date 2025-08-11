package com.jabulani.ligiopen.controller.club;

import org.springframework.http.ResponseEntity;

public interface ClubStaffController {

    /**
     * Add club manager (owner only)
     */
    ResponseEntity<Object> addClubManager(Long clubId, Long managerId);

    /**
     * Remove club manager (owner only)
     */
    ResponseEntity<Object> removeClubManager(Long clubId, Long managerId);

    /**
     * Get club managers
     */
    ResponseEntity<Object> getClubManagers(Long clubId);

    /**
     * Transfer club ownership (current owner only)
     */
    ResponseEntity<Object> transferClubOwnership(Long clubId, Long newOwnerId);

    /**
     * Get club ownership history
     */
    ResponseEntity<Object> getClubOwnershipHistory(Long clubId);

    /**
     * Get club staff summary (managers and owner)
     */
    ResponseEntity<Object> getClubStaff(Long clubId);

    /**
     * Update manager permissions/role
     */
    ResponseEntity<Object> updateManagerRole(Long clubId, Long managerId, String role);

    /**
     * Invite user to be club manager
     */
    ResponseEntity<Object> inviteClubManager(Long clubId, String email, String role);

    /**
     * Accept manager invitation
     */
    ResponseEntity<Object> acceptManagerInvitation(Long invitationId);

    /**
     * Decline manager invitation
     */
    ResponseEntity<Object> declineManagerInvitation(Long invitationId);

    /**
     * Get pending manager invitations for club
     */
    ResponseEntity<Object> getPendingInvitations(Long clubId);

    /**
     * Cancel manager invitation (owner only)
     */
    ResponseEntity<Object> cancelManagerInvitation(Long invitationId);
}