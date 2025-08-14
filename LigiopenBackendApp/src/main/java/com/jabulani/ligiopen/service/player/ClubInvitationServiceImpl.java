package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.dao.PlayerDao;
import com.jabulani.ligiopen.dao.ClubDao;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.entity.player.ClubInvitation;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.user.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ClubInvitationServiceImpl implements ClubInvitationService {

    private static final Logger logger = LoggerFactory.getLogger(ClubInvitationServiceImpl.class);

    private final PlayerDao playerDao;
    private final ClubDao clubDao;
    private final UserEntityDao userEntityDao;
    private final ClubMembershipService clubMembershipService;

    @Autowired
    public ClubInvitationServiceImpl(PlayerDao playerDao, ClubDao clubDao, 
                                   UserEntityDao userEntityDao, ClubMembershipService clubMembershipService) {
        this.playerDao = playerDao;
        this.clubDao = clubDao;
        this.userEntityDao = userEntityDao;
        this.clubMembershipService = clubMembershipService;
    }

    @Override
    public ClubInvitation createClubInvitation(Long clubId, Long playerId, Long invitedByUserId,
                                             Player.Position proposedPosition, Integer proposedJerseyNumber,
                                             BigDecimal proposedWage, String message, LocalDateTime expiresAt) {
        try {
            logger.info("Creating club invitation from club ID: {} to player ID: {} by user ID: {}", 
                       clubId, playerId, invitedByUserId);

            // Validate entities exist
            Club club = clubDao.getClubByIdOrThrow(clubId);
            Player player = playerDao.getPlayerByIdOrThrow(playerId);
            UserEntity invitedBy = userEntityDao.getUserByIdOrThrow(invitedByUserId);

            // Validate user can invite on behalf of club
            if (!clubDao.isUserClubOwner(invitedByUserId, clubId) && 
                !clubDao.isUserClubManager(invitedByUserId, clubId)) {
                throw new RuntimeException("User is not authorized to send invitations on behalf of this club");
            }

            // Check if player already has pending invitation from this club
            if (hasPlayerPendingInvitationFromClub(playerId, clubId)) {
                throw new RuntimeException("Player already has a pending invitation from this club");
            }

            // Check if player already has active membership with any club
            List<ClubMembership> activeMemberships = playerDao.getActiveClubMemberships(playerId);
            if (!activeMemberships.isEmpty()) {
                throw new RuntimeException("Player already has an active club membership");
            }

            // Check jersey number availability if specified
            if (proposedJerseyNumber != null && 
                !playerDao.isJerseyNumberAvailable(clubId, proposedJerseyNumber, null)) {
                throw new RuntimeException("Proposed jersey number " + proposedJerseyNumber + " is already taken");
            }

            // Set default expiry if not provided (7 days)
            LocalDateTime effectiveExpiresAt = expiresAt != null ? expiresAt : LocalDateTime.now().plusDays(7);

            // Create invitation
            ClubInvitation invitation = ClubInvitation.builder()
                    .club(club)
                    .player(player)
                    .invitedBy(invitedBy)
                    .status(ClubInvitation.InvitationStatus.PENDING)
                    .invitedAt(LocalDateTime.now())
                    .expiresAt(effectiveExpiresAt)
                    .message(message)
                    .proposedPosition(proposedPosition)
                    .proposedJerseyNumber(proposedJerseyNumber)
                    .proposedWage(proposedWage)
                    .build();

            ClubInvitation savedInvitation = playerDao.saveClubInvitation(invitation);
            logger.info("Successfully created club invitation with ID: {}", savedInvitation.getId());

            return savedInvitation;

        } catch (Exception e) {
            logger.error("Failed to create club invitation from club ID: {} to player ID: {}", clubId, playerId, e);
            throw new RuntimeException("Failed to create club invitation: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubInvitation acceptClubInvitation(Long invitationId, Long playerId) {
        try {
            logger.info("Player ID: {} accepting club invitation ID: {}", playerId, invitationId);

            ClubInvitation invitation = playerDao.getClubInvitationByIdOrThrow(invitationId);

            // Validate player can respond to this invitation
            if (!canPlayerRespondToInvitation(invitationId, playerId)) {
                throw new RuntimeException("Player cannot respond to this invitation");
            }

            // Check if invitation is still valid
            if (invitation.getStatus() != ClubInvitation.InvitationStatus.PENDING) {
                throw new RuntimeException("Invitation is no longer pending");
            }

            if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Invitation has expired");
            }

            // Check if player still doesn't have an active membership
            List<ClubMembership> activeMemberships = playerDao.getActiveClubMemberships(playerId);
            if (!activeMemberships.isEmpty()) {
                throw new RuntimeException("Player already has an active club membership");
            }

            // Check jersey number availability again
            if (invitation.getProposedJerseyNumber() != null && 
                !playerDao.isJerseyNumberAvailable(invitation.getClub().getId(), 
                                                 invitation.getProposedJerseyNumber(), null)) {
                throw new RuntimeException("Proposed jersey number is no longer available");
            }

            // Create club membership
            ClubMembership membership = clubMembershipService.createClubMembership(
                playerId,
                invitation.getClub().getId(),
                invitation.getProposedPosition(),
                invitation.getProposedJerseyNumber(),
                ClubMembership.ContractType.PROFESSIONAL, // Default contract type
                LocalDateTime.now().toLocalDate(), // Contract start date
                null, // Contract end date (to be negotiated)
                invitation.getProposedWage(),
                null // Signing fee (to be negotiated)
            );

            // Update invitation status
            invitation.setStatus(ClubInvitation.InvitationStatus.ACCEPTED);
            invitation.setRespondedAt(LocalDateTime.now());

            ClubInvitation updatedInvitation = playerDao.saveClubInvitation(invitation);
            logger.info("Successfully accepted club invitation ID: {} and created membership ID: {}", 
                       invitationId, membership.getId());

            return updatedInvitation;

        } catch (Exception e) {
            logger.error("Failed to accept club invitation ID: {} by player ID: {}", invitationId, playerId, e);
            throw new RuntimeException("Failed to accept club invitation: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubInvitation rejectClubInvitation(Long invitationId, Long playerId, String rejectionReason) {
        try {
            logger.info("Player ID: {} rejecting club invitation ID: {}", playerId, invitationId);

            ClubInvitation invitation = playerDao.getClubInvitationByIdOrThrow(invitationId);

            // Validate player can respond to this invitation
            if (!canPlayerRespondToInvitation(invitationId, playerId)) {
                throw new RuntimeException("Player cannot respond to this invitation");
            }

            // Check if invitation is still pending
            if (invitation.getStatus() != ClubInvitation.InvitationStatus.PENDING) {
                throw new RuntimeException("Invitation is no longer pending");
            }

            // Update invitation status
            invitation.setStatus(ClubInvitation.InvitationStatus.REJECTED);
            invitation.setRespondedAt(LocalDateTime.now());
            // Note: rejectionReason field not available in entity

            ClubInvitation updatedInvitation = playerDao.saveClubInvitation(invitation);
            logger.info("Successfully rejected club invitation ID: {}", invitationId);

            return updatedInvitation;

        } catch (Exception e) {
            logger.error("Failed to reject club invitation ID: {} by player ID: {}", invitationId, playerId, e);
            throw new RuntimeException("Failed to reject club invitation: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubInvitation cancelClubInvitation(Long invitationId, Long userId, String cancellationReason) {
        try {
            logger.info("User ID: {} cancelling club invitation ID: {}", userId, invitationId);

            ClubInvitation invitation = playerDao.getClubInvitationByIdOrThrow(invitationId);

            // Validate user can manage this invitation
            if (!canUserManageInvitation(invitationId, userId)) {
                throw new RuntimeException("User cannot manage this invitation");
            }

            // Check if invitation can be cancelled
            if (invitation.getStatus() != ClubInvitation.InvitationStatus.PENDING) {
                throw new RuntimeException("Only pending invitations can be cancelled");
            }

            // Update invitation status
            invitation.setStatus(ClubInvitation.InvitationStatus.CANCELLED);
            // Note: cancellationReason field not available in entity

            ClubInvitation updatedInvitation = playerDao.saveClubInvitation(invitation);
            logger.info("Successfully cancelled club invitation ID: {}", invitationId);

            return updatedInvitation;

        } catch (Exception e) {
            logger.error("Failed to cancel club invitation ID: {} by user ID: {}", invitationId, userId, e);
            throw new RuntimeException("Failed to cancel club invitation: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubInvitation updateClubInvitation(Long invitationId, Long userId, Player.Position proposedPosition,
                                             Integer proposedJerseyNumber, BigDecimal proposedWage,
                                             String message, LocalDateTime expiresAt) {
        try {
            logger.info("User ID: {} updating club invitation ID: {}", userId, invitationId);

            ClubInvitation invitation = playerDao.getClubInvitationByIdOrThrow(invitationId);

            // Validate user can manage this invitation
            if (!canUserManageInvitation(invitationId, userId)) {
                throw new RuntimeException("User cannot manage this invitation");
            }

            // Check if invitation can be updated
            if (invitation.getStatus() != ClubInvitation.InvitationStatus.PENDING) {
                throw new RuntimeException("Only pending invitations can be updated");
            }

            // Check jersey number availability if changing
            if (proposedJerseyNumber != null && 
                !proposedJerseyNumber.equals(invitation.getProposedJerseyNumber()) &&
                !playerDao.isJerseyNumberAvailable(invitation.getClub().getId(), proposedJerseyNumber, null)) {
                throw new RuntimeException("Proposed jersey number " + proposedJerseyNumber + " is already taken");
            }

            // Update fields
            if (proposedPosition != null) invitation.setProposedPosition(proposedPosition);
            if (proposedJerseyNumber != null) invitation.setProposedJerseyNumber(proposedJerseyNumber);
            if (proposedWage != null) invitation.setProposedWage(proposedWage);
            if (message != null) invitation.setMessage(message);
            if (expiresAt != null) invitation.setExpiresAt(expiresAt);

            ClubInvitation updatedInvitation = playerDao.saveClubInvitation(invitation);
            logger.info("Successfully updated club invitation ID: {}", invitationId);

            return updatedInvitation;

        } catch (Exception e) {
            logger.error("Failed to update club invitation ID: {} by user ID: {}", invitationId, userId, e);
            throw new RuntimeException("Failed to update club invitation: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClubInvitation getClubInvitationById(Long invitationId) {
        return playerDao.getClubInvitationByIdOrThrow(invitationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubInvitation> getPendingInvitationsForPlayer(Long playerId, PageRequest pageRequest) {
        return playerDao.getClubInvitationsByPlayerAndStatus(playerId, ClubInvitation.InvitationStatus.PENDING, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubInvitation> getAllInvitationsForPlayer(Long playerId, PageRequest pageRequest) {
        return playerDao.getClubInvitationsByPlayer(playerId, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubInvitation> getInvitationsByClub(Long clubId, ClubInvitation.InvitationStatus status, PageRequest pageRequest) {
        if (status != null) {
            return playerDao.getClubInvitationsByClubAndStatus(clubId, status, pageRequest);
        } else {
            return playerDao.getClubInvitationsByClub(clubId, pageRequest);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubInvitation> getInvitationsSentByUser(Long userId, PageRequest pageRequest) {
        return playerDao.getClubInvitationsByInviter(userId, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubInvitation> getExpiredInvitations(PageRequest pageRequest) {
        return playerDao.getExpiredClubInvitations(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubInvitation> getInvitationsExpiringSoon(int withinHours, PageRequest pageRequest) {
        LocalDateTime cutoffTime = LocalDateTime.now().plusHours(withinHours);
        return playerDao.getClubInvitationsExpiringBefore(cutoffTime, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPlayerPendingInvitationFromClub(Long playerId, Long clubId) {
        return playerDao.hasPlayerPendingInvitationFromClub(playerId, clubId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendingInvitationsForPlayer(Long playerId) {
        return playerDao.countClubInvitationsByPlayerAndStatus(playerId, ClubInvitation.InvitationStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public long countInvitationsByClub(Long clubId, ClubInvitation.InvitationStatus status) {
        if (status != null) {
            return playerDao.countClubInvitationsByClubAndStatus(clubId, status);
        } else {
            return playerDao.countClubInvitationsByClub(clubId);
        }
    }

    @Override
    public int processExpiredInvitations() {
        try {
            logger.info("Processing expired invitations");

            List<ClubInvitation> expiredInvitations = playerDao.getPendingInvitationsExpiredBefore(LocalDateTime.now());
            int processedCount = 0;

            for (ClubInvitation invitation : expiredInvitations) {
                invitation.setStatus(ClubInvitation.InvitationStatus.EXPIRED);
                playerDao.saveClubInvitation(invitation);
                processedCount++;
            }

            logger.info("Processed {} expired invitations", processedCount);
            return processedCount;

        } catch (Exception e) {
            logger.error("Failed to process expired invitations", e);
            throw new RuntimeException("Failed to process expired invitations: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getClubInvitationStatistics(Long clubId) {
        try {
            logger.info("Getting club invitation statistics for club ID: {}", clubId);

            Map<String, Object> statistics = new HashMap<>();

            // Total invitations sent
            statistics.put("totalInvitations", countInvitationsByClub(clubId, null));
            statistics.put("pendingInvitations", countInvitationsByClub(clubId, ClubInvitation.InvitationStatus.PENDING));
            statistics.put("acceptedInvitations", countInvitationsByClub(clubId, ClubInvitation.InvitationStatus.ACCEPTED));
            statistics.put("rejectedInvitations", countInvitationsByClub(clubId, ClubInvitation.InvitationStatus.REJECTED));
            statistics.put("cancelledInvitations", countInvitationsByClub(clubId, ClubInvitation.InvitationStatus.CANCELLED));
            statistics.put("expiredInvitations", countInvitationsByClub(clubId, ClubInvitation.InvitationStatus.EXPIRED));

            // Acceptance rate
            long totalSent = countInvitationsByClub(clubId, null);
            long accepted = countInvitationsByClub(clubId, ClubInvitation.InvitationStatus.ACCEPTED);
            double acceptanceRate = totalSent > 0 ? (double) accepted / totalSent * 100 : 0.0;
            statistics.put("acceptanceRate", Math.round(acceptanceRate * 100.0) / 100.0);

            // Recent invitations (last 30 days)
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            List<ClubInvitation> recentInvitations = playerDao.getClubInvitationsByClubAfterDate(clubId, thirtyDaysAgo, PageRequest.of(0, Integer.MAX_VALUE));
            statistics.put("recentInvitations", recentInvitations.size());

            logger.info("Successfully retrieved club invitation statistics for club ID: {}", clubId);
            return statistics;

        } catch (Exception e) {
            logger.error("Failed to get club invitation statistics for club ID: {}", clubId, e);
            throw new RuntimeException("Failed to get club invitation statistics: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPlayerInvitationStatistics(Long playerId) {
        try {
            logger.info("Getting player invitation statistics for player ID: {}", playerId);

            Map<String, Object> statistics = new HashMap<>();

            // Total invitations received
            List<ClubInvitation> allInvitations = getAllInvitationsForPlayer(playerId, PageRequest.of(0, Integer.MAX_VALUE));
            statistics.put("totalInvitations", allInvitations.size());

            // By status
            statistics.put("pendingInvitations", countPendingInvitationsForPlayer(playerId));
            statistics.put("acceptedInvitations", allInvitations.stream()
                    .mapToLong(inv -> inv.getStatus() == ClubInvitation.InvitationStatus.ACCEPTED ? 1 : 0)
                    .sum());
            statistics.put("rejectedInvitations", allInvitations.stream()
                    .mapToLong(inv -> inv.getStatus() == ClubInvitation.InvitationStatus.REJECTED ? 1 : 0)
                    .sum());
            statistics.put("expiredInvitations", allInvitations.stream()
                    .mapToLong(inv -> inv.getStatus() == ClubInvitation.InvitationStatus.EXPIRED ? 1 : 0)
                    .sum());

            // Recent invitations (last 30 days)
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            long recentInvitations = allInvitations.stream()
                    .mapToLong(inv -> inv.getInvitedAt().isAfter(thirtyDaysAgo) ? 1 : 0)
                    .sum();
            statistics.put("recentInvitations", recentInvitations);

            logger.info("Successfully retrieved player invitation statistics for player ID: {}", playerId);
            return statistics;

        } catch (Exception e) {
            logger.error("Failed to get player invitation statistics for player ID: {}", playerId, e);
            throw new RuntimeException("Failed to get player invitation statistics: " + e.getMessage(), e);
        }
    }

    @Override
    public int bulkCancelInvitations(List<Long> invitationIds, Long userId, String cancellationReason) {
        try {
            logger.info("User ID: {} bulk cancelling {} invitations", userId, invitationIds.size());

            int cancelledCount = 0;
            for (Long invitationId : invitationIds) {
                try {
                    cancelClubInvitation(invitationId, userId, cancellationReason);
                    cancelledCount++;
                } catch (Exception e) {
                    logger.warn("Failed to cancel invitation ID: {} - {}", invitationId, e.getMessage());
                }
            }

            logger.info("Successfully cancelled {} out of {} invitations", cancelledCount, invitationIds.size());
            return cancelledCount;

        } catch (Exception e) {
            logger.error("Failed to bulk cancel invitations", e);
            throw new RuntimeException("Failed to bulk cancel invitations: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserManageInvitation(Long invitationId, Long userId) {
        try {
            ClubInvitation invitation = playerDao.getClubInvitationByIdOrThrow(invitationId);
            Long clubId = invitation.getClub().getId();
            
            // User can manage if they are club owner, manager, or the one who sent the invitation
            return clubDao.isUserClubOwner(userId, clubId) ||
                   clubDao.isUserClubManager(userId, clubId) ||
                   invitation.getInvitedBy().getId().equals(userId);
                   
        } catch (Exception e) {
            logger.error("Error checking if user ID: {} can manage invitation ID: {}", userId, invitationId, e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canPlayerRespondToInvitation(Long invitationId, Long playerId) {
        try {
            ClubInvitation invitation = playerDao.getClubInvitationByIdOrThrow(invitationId);
            
            // Player can respond if the invitation is for them, is pending, and not expired
            return invitation.getPlayer().getId().equals(playerId) &&
                   invitation.getStatus() == ClubInvitation.InvitationStatus.PENDING &&
                   invitation.getExpiresAt().isAfter(LocalDateTime.now());
                   
        } catch (Exception e) {
            logger.error("Error checking if player ID: {} can respond to invitation ID: {}", playerId, invitationId, e);
            return false;
        }
    }
}