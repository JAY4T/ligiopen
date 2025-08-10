package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.location.Stadium;
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
import java.util.List;
import java.util.Optional;

@Repository
public class StadiumDaoImpl implements StadiumDao {

    private static final Logger logger = LoggerFactory.getLogger(StadiumDaoImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public StadiumDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Stadium createStadium(Stadium stadium) {
        try {
            logger.info("Creating stadium: {}", stadium.getName());
            
            // Set creation timestamp if not already set
            if (stadium.getCreatedAt() == null) {
                stadium.setCreatedAt(LocalDateTime.now());
            }
            
            entityManager.persist(stadium);
            entityManager.flush();
            logger.info("Successfully created stadium with ID: {}", stadium.getId());
            return stadium;
        } catch (Exception e) {
            logger.error("Failed to create stadium: {}", stadium.getName(), e);
            throw new RuntimeException("Failed to create stadium", e);
        }
    }

    @Override
    @Transactional
    public Stadium updateStadium(Stadium stadium) {
        try {
            logger.info("Updating stadium with ID: {}", stadium.getId());
            Stadium updatedStadium = entityManager.merge(stadium);
            entityManager.flush();
            logger.info("Successfully updated stadium: {}", updatedStadium.getName());
            return updatedStadium;
        } catch (Exception e) {
            logger.error("Failed to update stadium with ID: {}", stadium.getId(), e);
            throw new RuntimeException("Failed to update stadium", e);
        }
    }

    @Override
    public Stadium getStadiumById(Long id) {
        try {
            logger.debug("Fetching stadium by ID: {}", id);
            Stadium stadium = entityManager.find(Stadium.class, id);
            if (stadium == null) {
                logger.warn("Stadium not found with ID: {}", id);
                throw new RuntimeException("Stadium not found with ID: " + id);
            }
            return stadium;
        } catch (Exception e) {
            logger.error("Error fetching stadium by ID: {}", id, e);
            throw new RuntimeException("Error fetching stadium by ID: " + id, e);
        }
    }

    @Override
    public Optional<Stadium> findStadiumById(Long id) {
        try {
            logger.debug("Finding stadium by ID: {}", id);
            Stadium stadium = entityManager.find(Stadium.class, id);
            return Optional.ofNullable(stadium);
        } catch (Exception e) {
            logger.error("Error finding stadium by ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Stadium> getStadiumByName(String name) {
        try {
            logger.debug("Finding stadium by name: {}", name);
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s WHERE LOWER(s.name) = LOWER(:name)", Stadium.class);
            query.setParameter("name", name);
            Stadium stadium = query.getSingleResult();
            return Optional.of(stadium);
        } catch (NoResultException e) {
            logger.debug("No stadium found with name: {}", name);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding stadium by name: {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Stadium> getAllStadiums() {
        try {
            logger.debug("Fetching all stadiums");
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county ORDER BY s.name ASC", Stadium.class);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums", stadiums.size());
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching all stadiums", e);
            throw new RuntimeException("Error fetching all stadiums", e);
        }
    }

    @Override
    public List<Stadium> getStadiumsByCounty(County county) {
        try {
            logger.debug("Fetching stadiums by county: {}", county.getName());
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s WHERE s.county = :county ORDER BY s.name ASC", Stadium.class);
            query.setParameter("county", county);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums in county: {}", stadiums.size(), county.getName());
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching stadiums by county: {}", county.getName(), e);
            throw new RuntimeException("Error fetching stadiums by county: " + county.getName(), e);
        }
    }

    @Override
    public List<Stadium> getStadiumsByCountyId(Long countyId) {
        try {
            logger.debug("Fetching stadiums by county ID: {}", countyId);
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE s.county.id = :countyId ORDER BY s.name ASC", Stadium.class);
            query.setParameter("countyId", countyId);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums in county ID: {}", stadiums.size(), countyId);
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching stadiums by county ID: {}", countyId, e);
            throw new RuntimeException("Error fetching stadiums by county ID: " + countyId, e);
        }
    }

    @Override
    public List<Stadium> getStadiumsByCity(String city) {
        try {
            logger.debug("Fetching stadiums by city: {}", city);
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE LOWER(s.city) = LOWER(:city) ORDER BY s.name ASC", Stadium.class);
            query.setParameter("city", city);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums in city: {}", stadiums.size(), city);
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching stadiums by city: {}", city, e);
            throw new RuntimeException("Error fetching stadiums by city: " + city, e);
        }
    }

    @Override
    public List<Stadium> getStadiumsByCapacityRange(Integer minCapacity, Integer maxCapacity) {
        try {
            logger.debug("Fetching stadiums with capacity between {} and {}", minCapacity, maxCapacity);
            StringBuilder jpql = new StringBuilder("SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE 1=1");
            
            if (minCapacity != null) {
                jpql.append(" AND s.capacity >= :minCapacity");
            }
            if (maxCapacity != null) {
                jpql.append(" AND s.capacity <= :maxCapacity");
            }
            jpql.append(" ORDER BY s.capacity DESC, s.name ASC");
            
            TypedQuery<Stadium> query = entityManager.createQuery(jpql.toString(), Stadium.class);
            
            if (minCapacity != null) {
                query.setParameter("minCapacity", minCapacity);
            }
            if (maxCapacity != null) {
                query.setParameter("maxCapacity", maxCapacity);
            }
            
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums in capacity range", stadiums.size());
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching stadiums by capacity range: {} - {}", minCapacity, maxCapacity, e);
            throw new RuntimeException("Error fetching stadiums by capacity range", e);
        }
    }

    @Override
    public List<Stadium> getStadiumsBySurfaceType(Stadium.SurfaceType surfaceType) {
        try {
            logger.debug("Fetching stadiums with surface type: {}", surfaceType);
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE s.surfaceType = :surfaceType ORDER BY s.name ASC", Stadium.class);
            query.setParameter("surfaceType", surfaceType);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums with surface type: {}", stadiums.size(), surfaceType);
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching stadiums by surface type: {}", surfaceType, e);
            throw new RuntimeException("Error fetching stadiums by surface type: " + surfaceType, e);
        }
    }

    @Override
    public List<Stadium> getVerifiedStadiums() {
        try {
            logger.debug("Fetching verified stadiums");
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE s.isVerified = true ORDER BY s.name ASC", Stadium.class);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} verified stadiums", stadiums.size());
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching verified stadiums", e);
            throw new RuntimeException("Error fetching verified stadiums", e);
        }
    }

    @Override
    public List<Stadium> getActiveStadiums() {
        try {
            logger.debug("Fetching active stadiums");
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE s.isActive = true ORDER BY s.name ASC", Stadium.class);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} active stadiums", stadiums.size());
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching active stadiums", e);
            throw new RuntimeException("Error fetching active stadiums", e);
        }
    }

    @Override
    public List<Stadium> searchStadiumsByName(String searchTerm) {
        try {
            logger.debug("Searching stadiums by name with term: {}", searchTerm);
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE LOWER(s.name) LIKE LOWER(:searchTerm) ORDER BY s.name ASC", Stadium.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums matching search term: {}", stadiums.size(), searchTerm);
            return stadiums;
        } catch (Exception e) {
            logger.error("Error searching stadiums by name: {}", searchTerm, e);
            throw new RuntimeException("Error searching stadiums by name: " + searchTerm, e);
        }
    }

    @Override
    public List<Stadium> findStadiumsNearLocation(BigDecimal latitude, BigDecimal longitude, double radiusKm) {
        try {
            logger.debug("Finding stadiums near location: {}, {} within {} km", latitude, longitude, radiusKm);
            
            // Using Haversine formula approximation in JPQL
            // Note: This is an approximation. For production, consider using PostGIS or similar
            String jpql = "SELECT s FROM Stadium s LEFT JOIN FETCH s.county " +
                         "WHERE s.latitude IS NOT NULL AND s.longitude IS NOT NULL " +
                         "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
                         "cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
                         "sin(radians(s.latitude)))) <= :radiusKm " +
                         "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
                         "cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
                         "sin(radians(s.latitude))))";
            
            TypedQuery<Stadium> query = entityManager.createQuery(jpql, Stadium.class);
            query.setParameter("latitude", latitude.doubleValue());
            query.setParameter("longitude", longitude.doubleValue());
            query.setParameter("radiusKm", radiusKm);
            
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums within {} km of location", stadiums.size(), radiusKm);
            return stadiums;
        } catch (Exception e) {
            logger.error("Error finding stadiums near location: {}, {} within {} km", latitude, longitude, radiusKm, e);
            // Fallback to all stadiums if geospatial query fails
            logger.warn("Falling back to all stadiums due to location query error");
            return getAllStadiums();
        }
    }

    @Override
    @Transactional
    public void deleteStadium(Long id) {
        try {
            logger.info("Deleting stadium with ID: {}", id);
            Stadium stadium = entityManager.find(Stadium.class, id);
            if (stadium == null) {
                logger.warn("Attempted to delete non-existent stadium with ID: {}", id);
                throw new RuntimeException("Stadium not found with ID: " + id);
            }
            entityManager.remove(stadium);
            entityManager.flush();
            logger.info("Successfully deleted stadium: {}", stadium.getName());
        } catch (Exception e) {
            logger.error("Failed to delete stadium with ID: {}", id, e);
            throw new RuntimeException("Failed to delete stadium with ID: " + id, e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try {
            logger.debug("Checking if stadium exists by name: {}", name);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Stadium s WHERE LOWER(s.name) = LOWER(:name)", Long.class);
            query.setParameter("name", name);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("Stadium exists by name '{}': {}", name, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if stadium exists by name: {}", name, e);
            return false;
        }
    }

    @Override
    public boolean existsByNameAndCounty(String name, County county) {
        try {
            logger.debug("Checking if stadium exists by name and county: {} in {}", name, county.getName());
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Stadium s WHERE LOWER(s.name) = LOWER(:name) AND s.county = :county", Long.class);
            query.setParameter("name", name);
            query.setParameter("county", county);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("Stadium exists by name '{}' in county '{}': {}", name, county.getName(), exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if stadium exists by name and county: {} in {}", name, county.getName(), e);
            return false;
        }
    }

    @Override
    public long countStadiums() {
        try {
            logger.debug("Counting total stadiums");
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Stadium s", Long.class);
            Long count = query.getSingleResult();
            logger.debug("Total stadiums count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting stadiums", e);
            return 0;
        }
    }

    @Override
    public long countStadiumsByCounty(Long countyId) {
        try {
            logger.debug("Counting stadiums in county ID: {}", countyId);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Stadium s WHERE s.county.id = :countyId", Long.class);
            query.setParameter("countyId", countyId);
            Long count = query.getSingleResult();
            logger.debug("Stadiums count in county ID {}: {}", countyId, count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting stadiums by county ID: {}", countyId, e);
            return 0;
        }
    }

    @Override
    public List<Stadium> getStadiumsWithMinCapacity(Integer minCapacity) {
        try {
            logger.debug("Fetching stadiums with minimum capacity: {}", minCapacity);
            TypedQuery<Stadium> query = entityManager.createQuery(
                "SELECT s FROM Stadium s LEFT JOIN FETCH s.county WHERE s.capacity >= :minCapacity ORDER BY s.capacity DESC, s.name ASC", Stadium.class);
            query.setParameter("minCapacity", minCapacity);
            List<Stadium> stadiums = query.getResultList();
            logger.debug("Found {} stadiums with minimum capacity: {}", stadiums.size(), minCapacity);
            return stadiums;
        } catch (Exception e) {
            logger.error("Error fetching stadiums with minimum capacity: {}", minCapacity, e);
            throw new RuntimeException("Error fetching stadiums with minimum capacity: " + minCapacity, e);
        }
    }
}