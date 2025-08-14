package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.entity.player.ClubInvitation;
import com.jabulani.ligiopen.entity.club.Club;

import java.math.BigDecimal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerDaoImpl implements PlayerDao {

    private static final Logger logger = LoggerFactory.getLogger(PlayerDaoImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public PlayerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Player createPlayer(Player player) {
        try {
            logger.info("Creating player: {} {}", player.getFirstName(), player.getLastName());
            
            // Set creation timestamp if not already set
            if (player.getCreatedAt() == null) {
                player.setCreatedAt(LocalDateTime.now());
            }
            if (player.getUpdatedAt() == null) {
                player.setUpdatedAt(LocalDateTime.now());
            }
            
            entityManager.persist(player);
            entityManager.flush();
            logger.info("Successfully created player with ID: {}", player.getId());
            return player;
        } catch (Exception e) {
            logger.error("Failed to create player: {} {}", player.getFirstName(), player.getLastName(), e);
            throw new RuntimeException("Failed to create player", e);
        }
    }

    @Override
    @Transactional
    public Player updatePlayer(Player player) {
        try {
            logger.info("Updating player with ID: {}", player.getId());
            player.setUpdatedAt(LocalDateTime.now());
            Player updatedPlayer = entityManager.merge(player);
            entityManager.flush();
            logger.info("Successfully updated player: {} {}", updatedPlayer.getFirstName(), updatedPlayer.getLastName());
            return updatedPlayer;
        } catch (Exception e) {
            logger.error("Failed to update player with ID: {}", player.getId(), e);
            throw new RuntimeException("Failed to update player", e);
        }
    }

    @Override
    public Player getPlayerById(Long id) {
        try {
            logger.debug("Fetching player by ID: {}", id);
            Player player = entityManager.find(Player.class, id);
            if (player == null) {
                logger.warn("Player not found with ID: {}", id);
                throw new RuntimeException("Player not found with ID: " + id);
            }
            logger.debug("Successfully fetched player: {} {}", player.getFirstName(), player.getLastName());
            return player;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch player by ID: {}", id, e);
            throw new RuntimeException("Failed to fetch player", e);
        }
    }

    @Override
    public Optional<Player> findPlayerById(Long id) {
        try {
            logger.debug("Finding player by ID: {}", id);
            Player player = entityManager.find(Player.class, id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            logger.error("Failed to find player by ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> getPlayerByEmail(String email) {
        try {
            logger.debug("Finding player by email: {}", email);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.email = :email AND p.isActive = true", Player.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            logger.debug("No player found with email: {}", email);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Failed to find player by email: {}", email, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> getPlayerByFkfRegistrationNumber(String fkfRegistrationNumber) {
        try {
            logger.debug("Finding player by FKF registration number: {}", fkfRegistrationNumber);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.fkfRegistrationNumber = :fkfRegistrationNumber AND p.isActive = true", Player.class);
            query.setParameter("fkfRegistrationNumber", fkfRegistrationNumber);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            logger.debug("No player found with FKF registration number: {}", fkfRegistrationNumber);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Failed to find player by FKF registration number: {}", fkfRegistrationNumber, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Player> getActivePlayers() {
        try {
            logger.debug("Fetching all active players");
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true ORDER BY p.lastName, p.firstName", Player.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch active players", e);
            throw new RuntimeException("Failed to fetch active players", e);
        }
    }

    @Override
    public List<Player> getAllPlayers() {
        try {
            logger.debug("Fetching all players");
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p ORDER BY p.lastName, p.firstName", Player.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch all players", e);
            throw new RuntimeException("Failed to fetch all players", e);
        }
    }

    @Override
    public List<Player> searchPlayersByName(String query, int offset, int limit) {
        try {
            logger.debug("Searching players by name: {} (offset: {}, limit: {})", query, offset, limit);
            String searchQuery = "%" + query.toLowerCase() + "%";
            TypedQuery<Player> typedQuery = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND " +
                "(LOWER(p.firstName) LIKE :query OR LOWER(p.lastName) LIKE :query OR LOWER(p.nickname) LIKE :query) " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            typedQuery.setParameter("query", searchQuery);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(limit);
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("Failed to search players by name: {}", query, e);
            throw new RuntimeException("Failed to search players by name", e);
        }
    }

    @Override
    public List<Player> getPlayersByPosition(Player.Position position, int offset, int limit) {
        try {
            logger.debug("Fetching players by position: {} (offset: {}, limit: {})", position, offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND " +
                "(p.primaryPosition = :position OR :position MEMBER OF p.secondaryPositions) " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            query.setParameter("position", position);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players by position: {}", position, e);
            throw new RuntimeException("Failed to fetch players by position", e);
        }
    }

    @Override
    public List<Player> getPlayersByNationality(String nationality, int offset, int limit) {
        try {
            logger.debug("Fetching players by nationality: {} (offset: {}, limit: {})", nationality, offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND " +
                "(p.nationality = :nationality OR p.secondNationality = :nationality) " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            query.setParameter("nationality", nationality);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players by nationality: {}", nationality, e);
            throw new RuntimeException("Failed to fetch players by nationality", e);
        }
    }

    @Override
    public List<Player> getPlayersByBirthDateRange(LocalDate startDate, LocalDate endDate, int offset, int limit) {
        try {
            logger.debug("Fetching players by birth date range: {} to {} (offset: {}, limit: {})", 
                        startDate, endDate, offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND " +
                "p.dateOfBirth BETWEEN :startDate AND :endDate " +
                "ORDER BY p.dateOfBirth DESC, p.lastName, p.firstName", Player.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players by birth date range: {} to {}", startDate, endDate, e);
            throw new RuntimeException("Failed to fetch players by birth date range", e);
        }
    }

    @Override
    public List<Player> getPlayersByClub(Club club) {
        return getPlayersByClubId(club.getId());
    }

    @Override
    public List<Player> getPlayersByClubId(Long clubId) {
        try {
            logger.debug("Fetching players by club ID: {}", clubId);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT DISTINCT p FROM Player p " +
                "JOIN p.clubMemberships cm " +
                "WHERE p.isActive = true AND cm.club.id = :clubId AND cm.isActive = true " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            query.setParameter("clubId", clubId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players by club ID: {}", clubId, e);
            throw new RuntimeException("Failed to fetch players by club", e);
        }
    }

    @Override
    public List<Player> getFreeAgents(int offset, int limit) {
        try {
            logger.debug("Fetching free agents (offset: {}, limit: {})", offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND " +
                "NOT EXISTS (SELECT cm FROM ClubMembership cm WHERE cm.player = p AND cm.isActive = true) " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch free agents", e);
            throw new RuntimeException("Failed to fetch free agents", e);
        }
    }

    @Override
    public List<Player> getPlayersWithActiveClubs(int offset, int limit) {
        try {
            logger.debug("Fetching players with active clubs (offset: {}, limit: {})", offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT DISTINCT p FROM Player p " +
                "JOIN p.clubMemberships cm " +
                "WHERE p.isActive = true AND cm.isActive = true " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players with active clubs", e);
            throw new RuntimeException("Failed to fetch players with active clubs", e);
        }
    }

    @Override
    public List<Player> getRecentlyRegisteredPlayers(int limit) {
        try {
            logger.debug("Fetching recently registered players (limit: {})", limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true " +
                "ORDER BY p.createdAt DESC", Player.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch recently registered players", e);
            throw new RuntimeException("Failed to fetch recently registered players", e);
        }
    }

    @Override
    public List<Player> getTopValuedPlayers(int limit) {
        try {
            logger.debug("Fetching top valued players (limit: {})", limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND p.marketValue IS NOT NULL " +
                "ORDER BY p.marketValue DESC", Player.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch top valued players", e);
            throw new RuntimeException("Failed to fetch top valued players", e);
        }
    }

    @Override
    public List<Player> getPlayersByPreferredFoot(Player.PreferredFoot preferredFoot, int offset, int limit) {
        try {
            logger.debug("Fetching players by preferred foot: {} (offset: {}, limit: {})", preferredFoot, offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND p.preferredFoot = :preferredFoot " +
                "ORDER BY p.lastName, p.firstName", Player.class);
            query.setParameter("preferredFoot", preferredFoot);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players by preferred foot: {}", preferredFoot, e);
            throw new RuntimeException("Failed to fetch players by preferred foot", e);
        }
    }

    @Override
    public List<Player> getPlayersByHeightRange(Double minHeight, Double maxHeight, int offset, int limit) {
        try {
            logger.debug("Fetching players by height range: {} to {} cm (offset: {}, limit: {})", 
                        minHeight, maxHeight, offset, limit);
            TypedQuery<Player> query = entityManager.createQuery(
                "SELECT p FROM Player p WHERE p.isActive = true AND " +
                "p.heightCm BETWEEN :minHeight AND :maxHeight " +
                "ORDER BY p.heightCm DESC, p.lastName, p.firstName", Player.class);
            query.setParameter("minHeight", minHeight);
            query.setParameter("maxHeight", maxHeight);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch players by height range: {} to {}", minHeight, maxHeight, e);
            throw new RuntimeException("Failed to fetch players by height range", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Player p WHERE p.email = :email AND p.isActive = true", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Failed to check if email exists: {}", email, e);
            return false;
        }
    }

    @Override
    public boolean existsByFkfRegistrationNumber(String fkfRegistrationNumber) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Player p WHERE p.fkfRegistrationNumber = :fkfRegistrationNumber AND p.isActive = true", Long.class);
            query.setParameter("fkfRegistrationNumber", fkfRegistrationNumber);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Failed to check if FKF registration number exists: {}", fkfRegistrationNumber, e);
            return false;
        }
    }

    @Override
    public boolean existsByIdNumber(String idNumber) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Player p WHERE p.idNumber = :idNumber AND p.isActive = true", Long.class);
            query.setParameter("idNumber", idNumber);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Failed to check if ID number exists: {}", idNumber, e);
            return false;
        }
    }

    @Override
    public boolean existsByPassportNumber(String passportNumber) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Player p WHERE p.passportNumber = :passportNumber AND p.isActive = true", Long.class);
            query.setParameter("passportNumber", passportNumber);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Failed to check if passport number exists: {}", passportNumber, e);
            return false;
        }
    }

    @Override
    @Transactional
    public void deletePlayer(Long playerId) {
        try {
            logger.info("Soft deleting player with ID: {}", playerId);
            Player player = getPlayerById(playerId);
            player.setIsActive(false);
            player.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(player);
            entityManager.flush();
            logger.info("Successfully soft deleted player with ID: {}", playerId);
        } catch (Exception e) {
            logger.error("Failed to soft delete player with ID: {}", playerId, e);
            throw new RuntimeException("Failed to delete player", e);
        }
    }

    @Override
    @Transactional
    public void hardDeletePlayer(Long playerId) {
        try {
            logger.warn("Hard deleting player with ID: {}", playerId);
            Player player = getPlayerById(playerId);
            entityManager.remove(player);
            entityManager.flush();
            logger.warn("Successfully hard deleted player with ID: {}", playerId);
        } catch (Exception e) {
            logger.error("Failed to hard delete player with ID: {}", playerId, e);
            throw new RuntimeException("Failed to hard delete player", e);
        }
    }

    @Override
    public long countAllPlayers() {
        try {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Player p", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Failed to count all players", e);
            return 0;
        }
    }

    @Override
    public long countActivePlayers() {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Player p WHERE p.isActive = true", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Failed to count active players", e);
            return 0;
        }
    }

    @Override
    public long countPlayersByClub(Long clubId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(DISTINCT p) FROM Player p " +
                "JOIN p.clubMemberships cm " +
                "WHERE p.isActive = true AND cm.club.id = :clubId AND cm.isActive = true", Long.class);
            query.setParameter("clubId", clubId);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Failed to count players by club ID: {}", clubId, e);
            return 0;
        }
    }

    @Override
    public long countFreeAgents() {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Player p WHERE p.isActive = true AND " +
                "NOT EXISTS (SELECT cm FROM ClubMembership cm WHERE cm.player = p AND cm.isActive = true)", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Failed to count free agents", e);
            return 0;
        }
    }

    // Club Membership related methods

    @Override
    public Optional<ClubMembership> getCurrentClubMembership(Long playerId) {
        try {
            logger.debug("Finding current club membership for player ID: {}", playerId);
            TypedQuery<ClubMembership> query = entityManager.createQuery(
                "SELECT cm FROM ClubMembership cm " +
                "WHERE cm.player.id = :playerId AND cm.isActive = true", ClubMembership.class);
            query.setParameter("playerId", playerId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            logger.debug("No current club membership found for player ID: {}", playerId);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Failed to find current club membership for player ID: {}", playerId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<ClubMembership> getPlayerClubMemberships(Long playerId) {
        try {
            logger.debug("Fetching club membership history for player ID: {}", playerId);
            TypedQuery<ClubMembership> query = entityManager.createQuery(
                "SELECT cm FROM ClubMembership cm " +
                "WHERE cm.player.id = :playerId " +
                "ORDER BY cm.joinedDate DESC", ClubMembership.class);
            query.setParameter("playerId", playerId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch club memberships for player ID: {}", playerId, e);
            throw new RuntimeException("Failed to fetch player club memberships", e);
        }
    }

    @Override
    public List<ClubMembership> getActiveClubMemberships(Long clubId) {
        try {
            logger.debug("Fetching active club memberships for club ID: {}", clubId);
            TypedQuery<ClubMembership> query = entityManager.createQuery(
                "SELECT cm FROM ClubMembership cm " +
                "WHERE cm.club.id = :clubId AND cm.isActive = true " +
                "ORDER BY cm.jerseyNumber, cm.player.lastName", ClubMembership.class);
            query.setParameter("clubId", clubId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch active club memberships for club ID: {}", clubId, e);
            throw new RuntimeException("Failed to fetch active club memberships", e);
        }
    }

    @Override
    public Optional<ClubMembership> getClubMembershipByPlayerAndClub(Long playerId, Long clubId) {
        try {
            logger.debug("Finding club membership for player ID: {} and club ID: {}", playerId, clubId);
            TypedQuery<ClubMembership> query = entityManager.createQuery(
                "SELECT cm FROM ClubMembership cm " +
                "WHERE cm.player.id = :playerId AND cm.club.id = :clubId AND cm.isActive = true", ClubMembership.class);
            query.setParameter("playerId", playerId);
            query.setParameter("clubId", clubId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            logger.debug("No club membership found for player ID: {} and club ID: {}", playerId, clubId);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Failed to find club membership for player ID: {} and club ID: {}", playerId, clubId, e);
            return Optional.empty();
        }
    }

    // Transfer related methods

    @Override
    public List<PlayerTransfer> getPlayerTransferHistory(Long playerId) {
        try {
            logger.debug("Fetching transfer history for player ID: {}", playerId);
            TypedQuery<PlayerTransfer> query = entityManager.createQuery(
                "SELECT pt FROM PlayerTransfer pt " +
                "WHERE pt.player.id = :playerId " +
                "ORDER BY pt.transferDate DESC", PlayerTransfer.class);
            query.setParameter("playerId", playerId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch transfer history for player ID: {}", playerId, e);
            throw new RuntimeException("Failed to fetch player transfer history", e);
        }
    }

    @Override
    public List<PlayerTransfer> getPendingTransfersForPlayer(Long playerId) {
        try {
            logger.debug("Fetching pending transfers for player ID: {}", playerId);
            TypedQuery<PlayerTransfer> query = entityManager.createQuery(
                "SELECT pt FROM PlayerTransfer pt " +
                "WHERE pt.player.id = :playerId AND pt.transferStatus = 'PENDING' " +
                "ORDER BY pt.transferDate DESC", PlayerTransfer.class);
            query.setParameter("playerId", playerId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch pending transfers for player ID: {}", playerId, e);
            throw new RuntimeException("Failed to fetch pending transfers", e);
        }
    }

    @Override
    public List<PlayerTransfer> getTransfersByClub(Long clubId) {
        try {
            logger.debug("Fetching transfers for club ID: {}", clubId);
            TypedQuery<PlayerTransfer> query = entityManager.createQuery(
                "SELECT pt FROM PlayerTransfer pt " +
                "WHERE pt.fromClub.id = :clubId OR pt.toClub.id = :clubId " +
                "ORDER BY pt.transferDate DESC", PlayerTransfer.class);
            query.setParameter("clubId", clubId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch transfers for club ID: {}", clubId, e);
            throw new RuntimeException("Failed to fetch transfers by club", e);
        }
    }

    @Override
    public List<PlayerTransfer> getRecentTransfers(int limit) {
        try {
            logger.debug("Fetching recent transfers (limit: {})", limit);
            TypedQuery<PlayerTransfer> query = entityManager.createQuery(
                "SELECT pt FROM PlayerTransfer pt " +
                "WHERE pt.transferStatus = 'COMPLETED' " +
                "ORDER BY pt.transferDate DESC", PlayerTransfer.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch recent transfers", e);
            throw new RuntimeException("Failed to fetch recent transfers", e);
        }
    }

    @Override
    public boolean hasPlayerPendingTransfer(Long playerId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(pt) FROM PlayerTransfer pt " +
                "WHERE pt.player.id = :playerId AND pt.transferStatus = 'PENDING'", Long.class);
            query.setParameter("playerId", playerId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Failed to check if player has pending transfer: {}", playerId, e);
            return false;
        }
    }

    // Stub implementations for newly added methods - to be implemented with actual database logic

    @Override
    public Player getPlayerByIdOrThrow(Long playerId) {
        Player player = getPlayerById(playerId);
        if (player == null) {
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
        return player;
    }

    @Override
    public ClubMembership saveClubMembership(ClubMembership membership) {
        throw new RuntimeException("Method saveClubMembership not yet implemented");
    }

    @Override
    public ClubMembership getClubMembershipByIdOrThrow(Long membershipId) {
        throw new RuntimeException("Method getClubMembershipByIdOrThrow not yet implemented");
    }

    @Override
    public List<ClubMembership> getActiveClubMembershipsByClub(Long clubId, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getActiveClubMembershipsByClub not yet implemented");
    }

    @Override
    public List<ClubMembership> getAllClubMembershipsByClub(Long clubId, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getAllClubMembershipsByClub not yet implemented");
    }

    @Override
    public List<ClubMembership> getClubMembershipsByPosition(Long clubId, Player.Position position, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubMembershipsByPosition not yet implemented");
    }

    @Override
    public ClubMembership getClubCaptain(Long clubId) {
        throw new RuntimeException("Method getClubCaptain not yet implemented");
    }

    @Override
    public ClubMembership getClubViceCaptain(Long clubId) {
        throw new RuntimeException("Method getClubViceCaptain not yet implemented");
    }

    @Override
    public List<ClubMembership> getMembershipsExpiringBefore(LocalDate cutoffDate, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getMembershipsExpiringBefore not yet implemented");
    }

    @Override
    public List<ClubMembership> getClubMembershipsByStatus(Long clubId, ClubMembership.MembershipStatus status, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubMembershipsByStatus not yet implemented");
    }

    @Override
    public boolean isJerseyNumberAvailable(Long clubId, Integer jerseyNumber, Long excludeMembershipId) {
        throw new RuntimeException("Method isJerseyNumberAvailable not yet implemented");
    }

    @Override
    public List<Integer> getTakenJerseyNumbers(Long clubId) {
        throw new RuntimeException("Method getTakenJerseyNumbers not yet implemented");
    }

    @Override
    public ClubInvitation saveClubInvitation(ClubInvitation invitation) {
        throw new RuntimeException("Method saveClubInvitation not yet implemented");
    }

    @Override
    public ClubInvitation getClubInvitationByIdOrThrow(Long invitationId) {
        throw new RuntimeException("Method getClubInvitationByIdOrThrow not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsByPlayerAndStatus(Long playerId, ClubInvitation.InvitationStatus status, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsByPlayerAndStatus not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsByPlayer(Long playerId, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsByPlayer not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsByClubAndStatus(Long clubId, ClubInvitation.InvitationStatus status, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsByClubAndStatus not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsByClub(Long clubId, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsByClub not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsByInviter(Long userId, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsByInviter not yet implemented");
    }

    @Override
    public List<ClubInvitation> getExpiredClubInvitations(org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getExpiredClubInvitations not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsExpiringBefore(LocalDateTime cutoffTime, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsExpiringBefore not yet implemented");
    }

    @Override
    public List<ClubInvitation> getPendingInvitationsExpiredBefore(LocalDateTime cutoffTime) {
        throw new RuntimeException("Method getPendingInvitationsExpiredBefore not yet implemented");
    }

    @Override
    public List<ClubInvitation> getClubInvitationsByClubAfterDate(Long clubId, LocalDateTime afterDate, org.springframework.data.domain.PageRequest pageRequest) {
        throw new RuntimeException("Method getClubInvitationsByClubAfterDate not yet implemented");
    }

    @Override
    public boolean hasPlayerPendingInvitationFromClub(Long playerId, Long clubId) {
        throw new RuntimeException("Method hasPlayerPendingInvitationFromClub not yet implemented");
    }

    @Override
    public long countClubInvitationsByPlayerAndStatus(Long playerId, ClubInvitation.InvitationStatus status) {
        throw new RuntimeException("Method countClubInvitationsByPlayerAndStatus not yet implemented");
    }

    @Override
    public long countClubInvitationsByClubAndStatus(Long clubId, ClubInvitation.InvitationStatus status) {
        throw new RuntimeException("Method countClubInvitationsByClubAndStatus not yet implemented");
    }

    @Override
    public long countClubInvitationsByClub(Long clubId) {
        throw new RuntimeException("Method countClubInvitationsByClub not yet implemented");
    }
}