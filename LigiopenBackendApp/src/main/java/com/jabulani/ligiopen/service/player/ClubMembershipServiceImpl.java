package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.dao.PlayerDao;
import com.jabulani.ligiopen.dao.ClubDao;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.club.Club;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Service
@Transactional
public class ClubMembershipServiceImpl implements ClubMembershipService {

    private static final Logger logger = LoggerFactory.getLogger(ClubMembershipServiceImpl.class);

    private final PlayerDao playerDao;
    private final ClubDao clubDao;

    @Autowired
    public ClubMembershipServiceImpl(PlayerDao playerDao, ClubDao clubDao) {
        this.playerDao = playerDao;
        this.clubDao = clubDao;
    }

    @Override
    public ClubMembership createClubMembership(Long playerId, Long clubId, Player.Position position,
                                             Integer jerseyNumber, ClubMembership.ContractType contractType,
                                             LocalDate contractStartDate, LocalDate contractEndDate,
                                             BigDecimal weeklyWage, BigDecimal signingFee) {
        try {
            logger.info("Creating club membership for player ID: {} and club ID: {}", playerId, clubId);

            // Validate inputs
            Player player = playerDao.getPlayerByIdOrThrow(playerId);
            Club club = clubDao.getClubByIdOrThrow(clubId);

            // Check if player already has an active membership
            List<ClubMembership> activeMemberships = playerDao.getActiveClubMemberships(playerId);
            if (!activeMemberships.isEmpty()) {
                throw new RuntimeException("Player already has an active club membership");
            }

            // Check jersey number availability
            if (jerseyNumber != null && !isJerseyNumberAvailable(clubId, jerseyNumber, null)) {
                throw new RuntimeException("Jersey number " + jerseyNumber + " is already taken");
            }

            // Create new membership
            ClubMembership membership = ClubMembership.builder()
                    .player(player)
                    .club(club)
                    .position(position)
                    .jerseyNumber(jerseyNumber)
                    .isActive(true)
                    .joinedDate(LocalDate.now())
                    .isCaptain(false)
                    .isViceCaptain(false)
                    .contractType(contractType)
                    .contractStartDate(contractStartDate != null ? contractStartDate : LocalDate.now())
                    .contractEndDate(contractEndDate)
                    .weeklyWage(weeklyWage)
                    .signingFee(signingFee)
                    .membershipStatus(ClubMembership.MembershipStatus.ACTIVE)
                    .build();

            ClubMembership savedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully created club membership with ID: {}", savedMembership.getId());

            return savedMembership;

        } catch (Exception e) {
            logger.error("Failed to create club membership for player ID: {} and club ID: {}", playerId, clubId, e);
            throw new RuntimeException("Failed to create club membership: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubMembership updateClubMembership(Long membershipId, Player.Position position,
                                             Integer jerseyNumber, ClubMembership.ContractType contractType,
                                             LocalDate contractStartDate, LocalDate contractEndDate,
                                             BigDecimal weeklyWage, BigDecimal signingFee) {
        try {
            logger.info("Updating club membership with ID: {}", membershipId);

            ClubMembership membership = playerDao.getClubMembershipByIdOrThrow(membershipId);

            // Check jersey number availability if changing
            if (jerseyNumber != null && !jerseyNumber.equals(membership.getJerseyNumber()) &&
                !isJerseyNumberAvailable(membership.getClub().getId(), jerseyNumber, membershipId)) {
                throw new RuntimeException("Jersey number " + jerseyNumber + " is already taken");
            }

            // Update fields
            if (position != null) membership.setPosition(position);
            if (jerseyNumber != null) membership.setJerseyNumber(jerseyNumber);
            if (contractType != null) membership.setContractType(contractType);
            if (contractStartDate != null) membership.setContractStartDate(contractStartDate);
            if (contractEndDate != null) membership.setContractEndDate(contractEndDate);
            if (weeklyWage != null) membership.setWeeklyWage(weeklyWage);
            if (signingFee != null) membership.setSigningFee(signingFee);

            ClubMembership updatedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully updated club membership with ID: {}", membershipId);

            return updatedMembership;

        } catch (Exception e) {
            logger.error("Failed to update club membership with ID: {}", membershipId, e);
            throw new RuntimeException("Failed to update club membership: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubMembership endClubMembership(Long membershipId, LocalDate endDate, String reason) {
        try {
            logger.info("Ending club membership with ID: {}", membershipId);

            ClubMembership membership = playerDao.getClubMembershipByIdOrThrow(membershipId);

            membership.setIsActive(false);
            membership.setLeftDate(endDate != null ? endDate : LocalDate.now());
            membership.setMembershipStatus(ClubMembership.MembershipStatus.TERMINATED);

            // Remove leadership roles when ending membership
            if (membership.getIsCaptain() || membership.getIsViceCaptain()) {
                membership.setIsCaptain(false);
                membership.setIsViceCaptain(false);
            }

            ClubMembership updatedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully ended club membership with ID: {}", membershipId);

            return updatedMembership;

        } catch (Exception e) {
            logger.error("Failed to end club membership with ID: {}", membershipId, e);
            throw new RuntimeException("Failed to end club membership: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubMembership setPlayerAsCaptain(Long membershipId) {
        try {
            logger.info("Setting player as captain for membership ID: {}", membershipId);

            ClubMembership membership = playerDao.getClubMembershipByIdOrThrow(membershipId);
            Long clubId = membership.getClub().getId();

            // Remove current captain if exists
            ClubMembership currentCaptain = getClubCaptain(clubId);
            if (currentCaptain != null && !currentCaptain.getId().equals(membershipId)) {
                currentCaptain.setIsCaptain(false);
                playerDao.saveClubMembership(currentCaptain);
            }

            // Set new captain
            membership.setIsCaptain(true);
            membership.setIsViceCaptain(false); // Can't be both captain and vice captain

            ClubMembership updatedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully set player as captain for membership ID: {}", membershipId);

            return updatedMembership;

        } catch (Exception e) {
            logger.error("Failed to set player as captain for membership ID: {}", membershipId, e);
            throw new RuntimeException("Failed to set player as captain: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubMembership setPlayerAsViceCaptain(Long membershipId) {
        try {
            logger.info("Setting player as vice captain for membership ID: {}", membershipId);

            ClubMembership membership = playerDao.getClubMembershipByIdOrThrow(membershipId);
            Long clubId = membership.getClub().getId();

            // Remove current vice captain if exists
            ClubMembership currentViceCaptain = getClubViceCaptain(clubId);
            if (currentViceCaptain != null && !currentViceCaptain.getId().equals(membershipId)) {
                currentViceCaptain.setIsViceCaptain(false);
                playerDao.saveClubMembership(currentViceCaptain);
            }

            // Set new vice captain
            membership.setIsViceCaptain(true);
            membership.setIsCaptain(false); // Can't be both captain and vice captain

            ClubMembership updatedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully set player as vice captain for membership ID: {}", membershipId);

            return updatedMembership;

        } catch (Exception e) {
            logger.error("Failed to set player as vice captain for membership ID: {}", membershipId, e);
            throw new RuntimeException("Failed to set player as vice captain: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubMembership removePlayerFromLeadershipRole(Long membershipId) {
        try {
            logger.info("Removing player from leadership role for membership ID: {}", membershipId);

            ClubMembership membership = playerDao.getClubMembershipByIdOrThrow(membershipId);

            membership.setIsCaptain(false);
            membership.setIsViceCaptain(false);

            ClubMembership updatedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully removed player from leadership role for membership ID: {}", membershipId);

            return updatedMembership;

        } catch (Exception e) {
            logger.error("Failed to remove player from leadership role for membership ID: {}", membershipId, e);
            throw new RuntimeException("Failed to remove player from leadership role: " + e.getMessage(), e);
        }
    }

    @Override
    public ClubMembership updateMembershipStatus(Long membershipId, ClubMembership.MembershipStatus status) {
        try {
            logger.info("Updating membership status to {} for membership ID: {}", status, membershipId);

            ClubMembership membership = playerDao.getClubMembershipByIdOrThrow(membershipId);

            membership.setMembershipStatus(status);

            // Update active status based on membership status
            if (status == ClubMembership.MembershipStatus.TERMINATED) {
                membership.setIsActive(false);
                if (membership.getLeftDate() == null) {
                    membership.setLeftDate(LocalDate.now());
                }
            }

            ClubMembership updatedMembership = playerDao.saveClubMembership(membership);
            logger.info("Successfully updated membership status for membership ID: {}", membershipId);

            return updatedMembership;

        } catch (Exception e) {
            logger.error("Failed to update membership status for membership ID: {}", membershipId, e);
            throw new RuntimeException("Failed to update membership status: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClubMembership getClubMembershipById(Long membershipId) {
        return playerDao.getClubMembershipByIdOrThrow(membershipId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getActiveClubMemberships(Long clubId, PageRequest pageRequest) {
        return playerDao.getActiveClubMembershipsByClub(clubId, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getAllClubMemberships(Long clubId, PageRequest pageRequest) {
        return playerDao.getAllClubMembershipsByClub(clubId, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getClubMembershipsByPosition(Long clubId, Player.Position position, PageRequest pageRequest) {
        return playerDao.getClubMembershipsByPosition(clubId, position, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ClubMembership getClubCaptain(Long clubId) {
        return playerDao.getClubCaptain(clubId);
    }

    @Override
    @Transactional(readOnly = true)
    public ClubMembership getClubViceCaptain(Long clubId) {
        return playerDao.getClubViceCaptain(clubId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getMembershipsExpiringSoon(int withinDays, PageRequest pageRequest) {
        LocalDate cutoffDate = LocalDate.now().plusDays(withinDays);
        return playerDao.getMembershipsExpiringBefore(cutoffDate, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getInjuredPlayers(Long clubId, PageRequest pageRequest) {
        return playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.INJURED, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getSuspendedPlayers(Long clubId, PageRequest pageRequest) {
        return playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.SUSPENDED, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMembership> getPlayersOnLoan(Long clubId, PageRequest pageRequest) {
        return playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.ON_LOAN, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isJerseyNumberAvailable(Long clubId, Integer jerseyNumber, Long excludeMembershipId) {
        return playerDao.isJerseyNumberAvailable(clubId, jerseyNumber, excludeMembershipId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAvailableJerseyNumbers(Long clubId, int maxNumber) {
        List<Integer> takenNumbers = playerDao.getTakenJerseyNumbers(clubId);
        return IntStream.rangeClosed(1, maxNumber)
                .filter(number -> !takenNumbers.contains(number))
                .boxed()
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getClubMembershipStatistics(Long clubId) {
        try {
            logger.info("Getting club membership statistics for club ID: {}", clubId);

            Map<String, Object> statistics = new HashMap<>();

            // Basic counts
            statistics.put("totalMembers", playerDao.getActiveClubMembershipsByClub(clubId, PageRequest.of(0, Integer.MAX_VALUE)).size());
            statistics.put("totalMembersAllTime", playerDao.getAllClubMembershipsByClub(clubId, PageRequest.of(0, Integer.MAX_VALUE)).size());

            // By status
            statistics.put("activePlayers", playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.ACTIVE, PageRequest.of(0, Integer.MAX_VALUE)).size());
            statistics.put("injuredPlayers", playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.INJURED, PageRequest.of(0, Integer.MAX_VALUE)).size());
            statistics.put("suspendedPlayers", playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.SUSPENDED, PageRequest.of(0, Integer.MAX_VALUE)).size());
            statistics.put("playersOnLoan", playerDao.getClubMembershipsByStatus(clubId, ClubMembership.MembershipStatus.ON_LOAN, PageRequest.of(0, Integer.MAX_VALUE)).size());

            // By position
            Map<String, Integer> positionCounts = new HashMap<>();
            for (Player.Position position : Player.Position.values()) {
                int count = playerDao.getClubMembershipsByPosition(clubId, position, PageRequest.of(0, Integer.MAX_VALUE)).size();
                positionCounts.put(position.toString(), count);
            }
            statistics.put("playersByPosition", positionCounts);

            // Leadership
            ClubMembership captain = getClubCaptain(clubId);
            ClubMembership viceCaptain = getClubViceCaptain(clubId);
            statistics.put("hasCaptain", captain != null);
            statistics.put("hasViceCaptain", viceCaptain != null);

            if (captain != null) {
                Map<String, Object> captainInfo = new HashMap<>();
                captainInfo.put("playerId", captain.getPlayer().getId());
                captainInfo.put("playerName", captain.getPlayer().getFirstName() + " " + captain.getPlayer().getLastName());
                captainInfo.put("jerseyNumber", captain.getJerseyNumber());
                statistics.put("captain", captainInfo);
            }

            if (viceCaptain != null) {
                Map<String, Object> viceCaptainInfo = new HashMap<>();
                viceCaptainInfo.put("playerId", viceCaptain.getPlayer().getId());
                viceCaptainInfo.put("playerName", viceCaptain.getPlayer().getFirstName() + " " + viceCaptain.getPlayer().getLastName());
                viceCaptainInfo.put("jerseyNumber", viceCaptain.getJerseyNumber());
                statistics.put("viceCaptain", viceCaptainInfo);
            }

            // Contracts expiring soon
            statistics.put("contractsExpiringSoon", getMembershipsExpiringSoon(180, PageRequest.of(0, Integer.MAX_VALUE)).size()); // 6 months

            logger.info("Successfully retrieved club membership statistics for club ID: {}", clubId);
            return statistics;

        } catch (Exception e) {
            logger.error("Failed to get club membership statistics for club ID: {}", clubId, e);
            throw new RuntimeException("Failed to get club membership statistics: " + e.getMessage(), e);
        }
    }
}