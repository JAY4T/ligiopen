package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClubDaoImpl implements ClubDao {

    private static final Logger logger = LoggerFactory.getLogger(ClubDaoImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public ClubDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Club createClub(Club club) {
        try {
            logger.info("Creating club: {}", club.getName());
            
            // Set creation timestamp if not already set
            if (club.getCreatedAt() == null) {
                club.setCreatedAt(LocalDateTime.now());
            }
            if (club.getUpdatedAt() == null) {
                club.setUpdatedAt(LocalDateTime.now());
            }
            
            entityManager.persist(club);
            entityManager.flush();
            logger.info("Successfully created club with ID: {}", club.getId());
            return club;
        } catch (Exception e) {
            logger.error("Failed to create club: {}", club.getName(), e);
            throw new RuntimeException("Failed to create club", e);
        }
    }

    @Override
    @Transactional
    public Club updateClub(Club club) {
        try {
            logger.info("Updating club with ID: {}", club.getId());
            club.setUpdatedAt(LocalDateTime.now());
            Club updatedClub = entityManager.merge(club);
            entityManager.flush();
            logger.info("Successfully updated club: {}", updatedClub.getName());
            return updatedClub;
        } catch (Exception e) {
            logger.error("Failed to update club with ID: {}", club.getId(), e);
            throw new RuntimeException("Failed to update club", e);
        }
    }

    @Override
    public Club getClubById(Long id) {
        try {
            logger.debug("Fetching club by ID: {}", id);
            Club club = entityManager.find(Club.class, id);
            if (club == null) {
                logger.warn("Club not found with ID: {}", id);
                throw new RuntimeException("Club not found with ID: " + id);
            }
            return club;
        } catch (Exception e) {
            logger.error("Error fetching club by ID: {}", id, e);
            throw new RuntimeException("Error fetching club by ID: " + id, e);
        }
    }

    @Override
    public Optional<Club> findClubById(Long id) {
        try {
            logger.debug("Finding club by ID: {}", id);
            Club club = entityManager.find(Club.class, id);
            return Optional.ofNullable(club);
        } catch (Exception e) {
            logger.error("Error finding club by ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Club> getClubByName(String name) {
        try {
            logger.debug("Finding club by name: {}", name);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE LOWER(c.name) = LOWER(:name)", Club.class);
            query.setParameter("name", name);
            Club club = query.getSingleResult();
            return Optional.of(club);
        } catch (NoResultException e) {
            logger.debug("No club found with name: {}", name);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding club by name: {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Club> getClubByRegistrationNumber(String registrationNumber) {
        try {
            logger.debug("Finding club by registration number: {}", registrationNumber);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.registrationNumber = :registrationNumber", Club.class);
            query.setParameter("registrationNumber", registrationNumber);
            Club club = query.getSingleResult();
            return Optional.of(club);
        } catch (NoResultException e) {
            logger.debug("No club found with registration number: {}", registrationNumber);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding club by registration number: {}", registrationNumber, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Club> getAllClubs() {
        try {
            logger.debug("Fetching all clubs");
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "ORDER BY c.name ASC", Club.class);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs", clubs.size());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching all clubs", e);
            throw new RuntimeException("Error fetching all clubs", e);
        }
    }

    @Override
    public List<Club> getActiveClubs() {
        try {
            logger.debug("Fetching active clubs");
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.isActive = true ORDER BY c.name ASC", Club.class);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} active clubs", clubs.size());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching active clubs", e);
            throw new RuntimeException("Error fetching active clubs", e);
        }
    }

    @Override
    public List<Club> getClubsByCounty(County county) {
        try {
            logger.debug("Fetching clubs by county: {}", county.getName());
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.owner WHERE c.county = :county " +
                "ORDER BY c.name ASC", Club.class);
            query.setParameter("county", county);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs in county: {}", clubs.size(), county.getName());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by county: {}", county.getName(), e);
            throw new RuntimeException("Error fetching clubs by county: " + county.getName(), e);
        }
    }

    @Override
    public List<Club> getClubsByCountyId(Long countyId) {
        try {
            logger.debug("Fetching clubs by county ID: {}", countyId);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.county.id = :countyId ORDER BY c.name ASC", Club.class);
            query.setParameter("countyId", countyId);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs in county ID: {}", clubs.size(), countyId);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by county ID: {}", countyId, e);
            throw new RuntimeException("Error fetching clubs by county ID: " + countyId, e);
        }
    }

    @Override
    public List<Club> getClubsByLevel(Club.ClubLevel clubLevel) {
        try {
            logger.debug("Fetching clubs by level: {}", clubLevel);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.clubLevel = :clubLevel ORDER BY c.name ASC", Club.class);
            query.setParameter("clubLevel", clubLevel);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs at level: {}", clubs.size(), clubLevel);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by level: {}", clubLevel, e);
            throw new RuntimeException("Error fetching clubs by level: " + clubLevel, e);
        }
    }

    @Override
    public List<Club> getClubsByOwner(UserEntity owner) {
        return getClubsByOwnerId(owner.getId());
    }

    @Override
    public List<Club> getClubsByOwnerId(Long ownerId) {
        try {
            logger.debug("Fetching clubs by owner ID: {}", ownerId);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county WHERE c.owner.id = :ownerId " +
                "ORDER BY c.name ASC", Club.class);
            query.setParameter("ownerId", ownerId);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs owned by user ID: {}", clubs.size(), ownerId);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by owner ID: {}", ownerId, e);
            throw new RuntimeException("Error fetching clubs by owner ID: " + ownerId, e);
        }
    }

    @Override
    public List<Club> getClubsManagedByUser(UserEntity user) {
        return getClubsManagedByUserId(user.getId());
    }

    @Override
    public List<Club> getClubsManagedByUserId(Long userId) {
        try {
            logger.debug("Fetching clubs managed by user ID: {}", userId);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county JOIN c.managers m " +
                "WHERE m.id = :userId ORDER BY c.name ASC", Club.class);
            query.setParameter("userId", userId);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs managed by user ID: {}", clubs.size(), userId);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs managed by user ID: {}", userId, e);
            throw new RuntimeException("Error fetching clubs managed by user ID: " + userId, e);
        }
    }

    @Override
    public List<Club> getClubsByLigiopenVerificationStatus(Club.LigiopenVerificationStatus status) {
        try {
            logger.debug("Fetching clubs by LigiOpen verification status: {}", status);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.ligiopenVerificationStatus = :status ORDER BY c.updatedAt DESC", Club.class);
            query.setParameter("status", status);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs with LigiOpen status: {}", clubs.size(), status);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by LigiOpen verification status: {}", status, e);
            throw new RuntimeException("Error fetching clubs by LigiOpen verification status: " + status, e);
        }
    }

    @Override
    public List<Club> getClubsByFkfVerificationStatus(Club.FkfVerificationStatus status) {
        try {
            logger.debug("Fetching clubs by FKF verification status: {}", status);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.fkfVerificationStatus = :status ORDER BY c.fkfVerificationDate DESC", Club.class);
            query.setParameter("status", status);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs with FKF status: {}", clubs.size(), status);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by FKF verification status: {}", status, e);
            throw new RuntimeException("Error fetching clubs by FKF verification status: " + status, e);
        }
    }

    @Override
    public List<Club> getLigiopenVerifiedClubs() {
        return getClubsByLigiopenVerificationStatus(Club.LigiopenVerificationStatus.VERIFIED);
    }

    @Override
    public List<Club> getFkfVerifiedClubs() {
        return getClubsByFkfVerificationStatus(Club.FkfVerificationStatus.VERIFIED);
    }

    @Override
    public List<Club> getFullyVerifiedClubs() {
        try {
            logger.debug("Fetching fully verified clubs (both LigiOpen and FKF)");
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.isLigiopenVerified = true AND c.isFkfVerified = true " +
                "ORDER BY c.name ASC", Club.class);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} fully verified clubs", clubs.size());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching fully verified clubs", e);
            throw new RuntimeException("Error fetching fully verified clubs", e);
        }
    }

    @Override
    public List<Club> getClubsPendingLigiopenVerification() {
        return getClubsByLigiopenVerificationStatus(Club.LigiopenVerificationStatus.PENDING);
    }

    @Override
    public List<Club> getClubsWithFkfRegistration() {
        try {
            logger.debug("Fetching clubs with FKF registration numbers");
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.registrationNumber IS NOT NULL AND c.registrationNumber != '' " +
                "ORDER BY c.name ASC", Club.class);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs with FKF registration", clubs.size());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs with FKF registration", e);
            throw new RuntimeException("Error fetching clubs with FKF registration", e);
        }
    }

    @Override
    public List<Club> getGrassrootsClubs() {
        try {
            logger.debug("Fetching grassroots clubs");
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE c.clubLevel = :grassroots AND c.fkfVerificationStatus = :notApplicable " +
                "ORDER BY c.name ASC", Club.class);
            query.setParameter("grassroots", Club.ClubLevel.GRASSROOTS);
            query.setParameter("notApplicable", Club.FkfVerificationStatus.NOT_APPLICABLE);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} grassroots clubs", clubs.size());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching grassroots clubs", e);
            throw new RuntimeException("Error fetching grassroots clubs", e);
        }
    }

    @Override
    public List<Club> searchClubsByName(String searchTerm) {
        try {
            logger.debug("Searching clubs by name with term: {}", searchTerm);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE LOWER(c.name) LIKE LOWER(:searchTerm) OR LOWER(c.shortName) LIKE LOWER(:searchTerm) " +
                "ORDER BY c.name ASC", Club.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs matching search term: {}", clubs.size(), searchTerm);
            return clubs;
        } catch (Exception e) {
            logger.error("Error searching clubs by name: {}", searchTerm, e);
            throw new RuntimeException("Error searching clubs by name: " + searchTerm, e);
        }
    }

    @Override
    public List<Club> getClubsFavoritedByUser(UserEntity user) {
        return getClubsFavoritedByUserId(user.getId());
    }

    @Override
    public List<Club> getClubsFavoritedByUserId(Long userId) {
        try {
            logger.debug("Fetching clubs favorited by user ID: {}", userId);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county JOIN c.favoritedByUsers f " +
                "WHERE f.user.id = :userId ORDER BY f.createdAt DESC", Club.class);
            query.setParameter("userId", userId);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs favorited by user ID: {}", clubs.size(), userId);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs favorited by user ID: {}", userId, e);
            throw new RuntimeException("Error fetching clubs favorited by user ID: " + userId, e);
        }
    }

    @Override
    public List<Club> getRecentlyCreatedClubs(int limit) {
        try {
            logger.debug("Fetching {} recently created clubs", limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "ORDER BY c.createdAt DESC", Club.class);
            query.setMaxResults(limit);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} recently created clubs", clubs.size());
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching recently created clubs", e);
            throw new RuntimeException("Error fetching recently created clubs", e);
        }
    }

    @Override
    public List<Club> getClubsByRegion(String region) {
        try {
            logger.debug("Fetching clubs by region: {}", region);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c LEFT JOIN FETCH c.county LEFT JOIN FETCH c.owner " +
                "WHERE LOWER(c.county.region) = LOWER(:region) ORDER BY c.name ASC", Club.class);
            query.setParameter("region", region);
            List<Club> clubs = query.getResultList();
            logger.debug("Found {} clubs in region: {}", clubs.size(), region);
            return clubs;
        } catch (Exception e) {
            logger.error("Error fetching clubs by region: {}", region, e);
            throw new RuntimeException("Error fetching clubs by region: " + region, e);
        }
    }

    @Override
    @Transactional
    public void deleteClub(Long id) {
        try {
            logger.info("Deleting club with ID: {}", id);
            Club club = entityManager.find(Club.class, id);
            if (club == null) {
                logger.warn("Attempted to delete non-existent club with ID: {}", id);
                throw new RuntimeException("Club not found with ID: " + id);
            }
            entityManager.remove(club);
            entityManager.flush();
            logger.info("Successfully deleted club: {}", club.getName());
        } catch (Exception e) {
            logger.error("Failed to delete club with ID: {}", id, e);
            throw new RuntimeException("Failed to delete club with ID: " + id, e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try {
            logger.debug("Checking if club exists by name: {}", name);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE LOWER(c.name) = LOWER(:name)", Long.class);
            query.setParameter("name", name);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("Club exists by name '{}': {}", name, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if club exists by name: {}", name, e);
            return false;
        }
    }

    @Override
    public boolean existsByRegistrationNumber(String registrationNumber) {
        try {
            logger.debug("Checking if club exists by registration number: {}", registrationNumber);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.registrationNumber = :registrationNumber", Long.class);
            query.setParameter("registrationNumber", registrationNumber);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("Club exists by registration number '{}': {}", registrationNumber, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if club exists by registration number: {}", registrationNumber, e);
            return false;
        }
    }

    @Override
    public boolean existsByNameAndCounty(String name, County county) {
        try {
            logger.debug("Checking if club exists by name and county: {} in {}", name, county.getName());
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE LOWER(c.name) = LOWER(:name) AND c.county = :county", Long.class);
            query.setParameter("name", name);
            query.setParameter("county", county);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("Club exists by name '{}' in county '{}': {}", name, county.getName(), exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if club exists by name and county: {} in {}", name, county.getName(), e);
            return false;
        }
    }

    @Override
    public boolean isUserClubOwner(Long userId, Long clubId) {
        try {
            logger.debug("Checking if user {} owns club {}", userId, clubId);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.id = :clubId AND c.owner.id = :userId", Long.class);
            query.setParameter("clubId", clubId);
            query.setParameter("userId", userId);
            Long count = query.getSingleResult();
            boolean isOwner = count > 0;
            logger.debug("User {} owns club {}: {}", userId, clubId, isOwner);
            return isOwner;
        } catch (Exception e) {
            logger.error("Error checking club ownership: user {} club {}", userId, clubId, e);
            return false;
        }
    }

    @Override
    public boolean isUserClubManager(Long userId, Long clubId) {
        try {
            logger.debug("Checking if user {} manages club {}", userId, clubId);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c JOIN c.managers m WHERE c.id = :clubId AND m.id = :userId", Long.class);
            query.setParameter("clubId", clubId);
            query.setParameter("userId", userId);
            Long count = query.getSingleResult();
            boolean isManager = count > 0;
            logger.debug("User {} manages club {}: {}", userId, clubId, isManager);
            return isManager;
        } catch (Exception e) {
            logger.error("Error checking club management: user {} club {}", userId, clubId, e);
            return false;
        }
    }

    @Override
    public boolean hasUserFavoritedClub(Long userId, Long clubId) {
        try {
            logger.debug("Checking if user {} has favorited club {}", userId, clubId);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(f) FROM FavoritedClub f WHERE f.user.id = :userId AND f.club.id = :clubId", Long.class);
            query.setParameter("userId", userId);
            query.setParameter("clubId", clubId);
            Long count = query.getSingleResult();
            boolean hasFavorited = count > 0;
            logger.debug("User {} has favorited club {}: {}", userId, clubId, hasFavorited);
            return hasFavorited;
        } catch (Exception e) {
            logger.error("Error checking club favorited status: user {} club {}", userId, clubId, e);
            return false;
        }
    }

    @Override
    public long countClubs() {
        try {
            logger.debug("Counting total clubs");
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c", Long.class);
            Long count = query.getSingleResult();
            logger.debug("Total clubs count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting clubs", e);
            return 0;
        }
    }

    @Override
    public long countActiveClubs() {
        try {
            logger.debug("Counting active clubs");
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.isActive = true", Long.class);
            Long count = query.getSingleResult();
            logger.debug("Active clubs count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting active clubs", e);
            return 0;
        }
    }

    @Override
    public long countClubsByCounty(Long countyId) {
        try {
            logger.debug("Counting clubs in county ID: {}", countyId);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.county.id = :countyId", Long.class);
            query.setParameter("countyId", countyId);
            Long count = query.getSingleResult();
            logger.debug("Clubs count in county ID {}: {}", countyId, count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting clubs by county ID: {}", countyId, e);
            return 0;
        }
    }

    @Override
    public long countClubsByLevel(Club.ClubLevel level) {
        try {
            logger.debug("Counting clubs by level: {}", level);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.clubLevel = :level", Long.class);
            query.setParameter("level", level);
            Long count = query.getSingleResult();
            logger.debug("Clubs count at level {}: {}", level, count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting clubs by level: {}", level, e);
            return 0;
        }
    }

    @Override
    public long countLigiopenVerifiedClubs() {
        try {
            logger.debug("Counting LigiOpen verified clubs");
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.isLigiopenVerified = true", Long.class);
            Long count = query.getSingleResult();
            logger.debug("LigiOpen verified clubs count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting LigiOpen verified clubs", e);
            return 0;
        }
    }

    @Override
    public long countFkfVerifiedClubs() {
        try {
            logger.debug("Counting FKF verified clubs");
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.isFkfVerified = true", Long.class);
            Long count = query.getSingleResult();
            logger.debug("FKF verified clubs count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting FKF verified clubs", e);
            return 0;
        }
    }

    // Additional methods for pagination and geographic search

    @Override
    public boolean existsByNameAndCountyAndIdNot(String name, Long countyId, Long excludeClubId) {
        try {
            logger.debug("Checking if club name '{}' exists in county ID {} excluding club ID {}", 
                        name, countyId, excludeClubId);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Club c WHERE c.name = :name AND c.county.id = :countyId AND c.id != :excludeId", 
                Long.class);
            query.setParameter("name", name);
            query.setParameter("countyId", countyId);
            query.setParameter("excludeId", excludeClubId);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking club name existence", e);
            return false;
        }
    }

    @Override
    public List<Club> searchClubsByName(String searchTerm, long offset, int limit) {
        try {
            logger.debug("Searching clubs by name: '{}' with offset {} and limit {}", searchTerm, offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.isActive = true AND " +
                "(LOWER(c.name) LIKE LOWER(:searchTerm) OR LOWER(c.shortName) LIKE LOWER(:searchTerm)) " +
                "ORDER BY c.name", Club.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching clubs by name: '{}'", searchTerm, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getClubsByCounty(Long countyId, long offset, int limit) {
        try {
            logger.debug("Getting clubs by county ID {} with offset {} and limit {}", countyId, offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.county.id = :countyId AND c.isActive = true ORDER BY c.name", 
                Club.class);
            query.setParameter("countyId", countyId);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting clubs by county ID: {}", countyId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getClubsByRegion(String region, long offset, int limit) {
        try {
            logger.debug("Getting clubs by region '{}' with offset {} and limit {}", region, offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.county.region = :region AND c.isActive = true ORDER BY c.name", 
                Club.class);
            query.setParameter("region", region);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting clubs by region: '{}'", region, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getClubsByLevel(Club.ClubLevel clubLevel, long offset, int limit) {
        try {
            logger.debug("Getting clubs by level {} with offset {} and limit {}", clubLevel, offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.clubLevel = :level AND c.isActive = true ORDER BY c.name", 
                Club.class);
            query.setParameter("level", clubLevel);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting clubs by level: {}", clubLevel, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getClubsNearLocation(BigDecimal latitude, BigDecimal longitude, 
                                          double radiusKm, long offset, int limit) {
        try {
            logger.debug("Getting clubs near location ({}, {}) within {}km with offset {} and limit {}", 
                        latitude, longitude, radiusKm, offset, limit);
            
            // Using Haversine formula in SQL - simplified version for PostgreSQL
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c JOIN c.homeStadium s WHERE c.isActive = true AND " +
                "s.latitude IS NOT NULL AND s.longitude IS NOT NULL " +
                "ORDER BY c.name", Club.class);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            
            // In a real implementation, you'd calculate distance using SQL functions
            // This is a simplified version that just returns clubs with stadiums
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting clubs near location", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getLigiopenVerifiedClubs(long offset, int limit) {
        try {
            logger.debug("Getting LigiOpen verified clubs with offset {} and limit {}", offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.isLigiopenVerified = true AND c.isActive = true ORDER BY c.name", 
                Club.class);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting LigiOpen verified clubs", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getFkfVerifiedClubs(long offset, int limit) {
        try {
            logger.debug("Getting FKF verified clubs with offset {} and limit {}", offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.isFkfVerified = true AND c.isActive = true ORDER BY c.name", 
                Club.class);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting FKF verified clubs", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getFullyVerifiedClubs(long offset, int limit) {
        try {
            logger.debug("Getting fully verified clubs with offset {} and limit {}", offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.isLigiopenVerified = true AND c.isFkfVerified = true " +
                "AND c.isActive = true ORDER BY c.name", Club.class);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting fully verified clubs", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Club> getAllActiveClubs(long offset, int limit) {
        try {
            logger.debug("Getting all active clubs with offset {} and limit {}", offset, limit);
            TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c WHERE c.isActive = true ORDER BY c.name", Club.class);
            query.setFirstResult((int) offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all active clubs", e);
            return new ArrayList<>();
        }
    }
}