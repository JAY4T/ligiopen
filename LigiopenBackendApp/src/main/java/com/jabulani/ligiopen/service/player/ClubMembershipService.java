package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.Player;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ClubMembershipService {
    
    /**
     * Create a new club membership for a player
     * @param playerId Player ID
     * @param clubId Club ID
     * @param position Player position in the club
     * @param jerseyNumber Jersey number
     * @param contractType Contract type
     * @param contractStartDate Contract start date
     * @param contractEndDate Contract end date
     * @param weeklyWage Weekly wage
     * @param signingFee Signing fee
     * @return Created ClubMembership entity
     */
    ClubMembership createClubMembership(Long playerId, Long clubId, Player.Position position,
                                      Integer jerseyNumber, ClubMembership.ContractType contractType,
                                      LocalDate contractStartDate, LocalDate contractEndDate,
                                      BigDecimal weeklyWage, BigDecimal signingFee);
    
    /**
     * Update club membership details
     * @param membershipId Membership ID
     * @param position New position
     * @param jerseyNumber New jersey number
     * @param contractType New contract type
     * @param contractStartDate New contract start date
     * @param contractEndDate New contract end date
     * @param weeklyWage New weekly wage
     * @param signingFee New signing fee
     * @return Updated ClubMembership entity
     */
    ClubMembership updateClubMembership(Long membershipId, Player.Position position,
                                      Integer jerseyNumber, ClubMembership.ContractType contractType,
                                      LocalDate contractStartDate, LocalDate contractEndDate,
                                      BigDecimal weeklyWage, BigDecimal signingFee);
    
    /**
     * End a club membership
     * @param membershipId Membership ID
     * @param endDate End date
     * @param reason Reason for ending membership
     * @return Updated ClubMembership entity
     */
    ClubMembership endClubMembership(Long membershipId, LocalDate endDate, String reason);
    
    /**
     * Set player as captain
     * @param membershipId Membership ID
     * @return Updated ClubMembership entity
     */
    ClubMembership setPlayerAsCaptain(Long membershipId);
    
    /**
     * Set player as vice captain
     * @param membershipId Membership ID
     * @return Updated ClubMembership entity
     */
    ClubMembership setPlayerAsViceCaptain(Long membershipId);
    
    /**
     * Remove player from captain/vice captain role
     * @param membershipId Membership ID
     * @return Updated ClubMembership entity
     */
    ClubMembership removePlayerFromLeadershipRole(Long membershipId);
    
    /**
     * Update player's membership status
     * @param membershipId Membership ID
     * @param status New membership status
     * @return Updated ClubMembership entity
     */
    ClubMembership updateMembershipStatus(Long membershipId, ClubMembership.MembershipStatus status);
    
    /**
     * Get club membership by ID
     * @param membershipId Membership ID
     * @return ClubMembership entity
     */
    ClubMembership getClubMembershipById(Long membershipId);
    
    /**
     * Get all active memberships for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of active club memberships
     */
    List<ClubMembership> getActiveClubMemberships(Long clubId, PageRequest pageRequest);
    
    /**
     * Get all memberships for a club (including inactive)
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of all club memberships
     */
    List<ClubMembership> getAllClubMemberships(Long clubId, PageRequest pageRequest);
    
    /**
     * Get players by position within a club
     * @param clubId Club ID
     * @param position Player position
     * @param pageRequest Pagination information
     * @return List of club memberships for the position
     */
    List<ClubMembership> getClubMembershipsByPosition(Long clubId, Player.Position position, PageRequest pageRequest);
    
    /**
     * Get club's captain
     * @param clubId Club ID
     * @return Captain's club membership if exists
     */
    ClubMembership getClubCaptain(Long clubId);
    
    /**
     * Get club's vice captain
     * @param clubId Club ID
     * @return Vice captain's club membership if exists
     */
    ClubMembership getClubViceCaptain(Long clubId);
    
    /**
     * Get memberships expiring soon
     * @param withinDays Number of days to check
     * @param pageRequest Pagination information
     * @return List of memberships expiring within specified days
     */
    List<ClubMembership> getMembershipsExpiringSoon(int withinDays, PageRequest pageRequest);
    
    /**
     * Get injured players for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of injured players' memberships
     */
    List<ClubMembership> getInjuredPlayers(Long clubId, PageRequest pageRequest);
    
    /**
     * Get suspended players for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of suspended players' memberships
     */
    List<ClubMembership> getSuspendedPlayers(Long clubId, PageRequest pageRequest);
    
    /**
     * Get players on loan for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of players on loan
     */
    List<ClubMembership> getPlayersOnLoan(Long clubId, PageRequest pageRequest);
    
    /**
     * Check if jersey number is available in a club
     * @param clubId Club ID
     * @param jerseyNumber Jersey number to check
     * @param excludeMembershipId Membership ID to exclude from check (for updates)
     * @return True if jersey number is available
     */
    boolean isJerseyNumberAvailable(Long clubId, Integer jerseyNumber, Long excludeMembershipId);
    
    /**
     * Get available jersey numbers for a club
     * @param clubId Club ID
     * @param maxNumber Maximum jersey number to check
     * @return List of available jersey numbers
     */
    List<Integer> getAvailableJerseyNumbers(Long clubId, int maxNumber);
    
    /**
     * Get club membership statistics
     * @param clubId Club ID
     * @return Statistics map
     */
    java.util.Map<String, Object> getClubMembershipStatistics(Long clubId);
}