package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.location.County;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CountyDaoImpl implements CountyDao {

    private static final Logger logger = LoggerFactory.getLogger(CountyDaoImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public CountyDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public County createCounty(County county) {
        try {
            logger.info("Creating county: {}", county.getName());
            entityManager.persist(county);
            entityManager.flush();
            logger.info("Successfully created county with ID: {}", county.getId());
            return county;
        } catch (Exception e) {
            logger.error("Failed to create county: {}", county.getName(), e);
            throw new RuntimeException("Failed to create county", e);
        }
    }

    @Override
    @Transactional
    public County updateCounty(County county) {
        try {
            logger.info("Updating county with ID: {}", county.getId());
            County updatedCounty = entityManager.merge(county);
            entityManager.flush();
            logger.info("Successfully updated county: {}", updatedCounty.getName());
            return updatedCounty;
        } catch (Exception e) {
            logger.error("Failed to update county with ID: {}", county.getId(), e);
            throw new RuntimeException("Failed to update county", e);
        }
    }

    @Override
    public County getCountyById(Long id) {
        try {
            logger.debug("Fetching county by ID: {}", id);
            County county = entityManager.find(County.class, id);
            if (county == null) {
                logger.warn("County not found with ID: {}", id);
                throw new RuntimeException("County not found with ID: " + id);
            }
            return county;
        } catch (Exception e) {
            logger.error("Error fetching county by ID: {}", id, e);
            throw new RuntimeException("Error fetching county by ID: " + id, e);
        }
    }

    @Override
    public Optional<County> findCountyById(Long id) {
        try {
            logger.debug("Finding county by ID: {}", id);
            County county = entityManager.find(County.class, id);
            return Optional.ofNullable(county);
        } catch (Exception e) {
            logger.error("Error finding county by ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<County> getCountyByName(String name) {
        try {
            logger.debug("Finding county by name: {}", name);
            TypedQuery<County> query = entityManager.createQuery(
                "SELECT c FROM County c WHERE LOWER(c.name) = LOWER(:name)", County.class);
            query.setParameter("name", name);
            County county = query.getSingleResult();
            return Optional.of(county);
        } catch (NoResultException e) {
            logger.debug("No county found with name: {}", name);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding county by name: {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<County> getCountyByCode(String code) {
        try {
            logger.debug("Finding county by code: {}", code);
            TypedQuery<County> query = entityManager.createQuery(
                "SELECT c FROM County c WHERE c.code = :code", County.class);
            query.setParameter("code", code);
            County county = query.getSingleResult();
            return Optional.of(county);
        } catch (NoResultException e) {
            logger.debug("No county found with code: {}", code);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding county by code: {}", code, e);
            return Optional.empty();
        }
    }

    @Override
    public List<County> getAllCounties() {
        try {
            logger.debug("Fetching all counties");
            TypedQuery<County> query = entityManager.createQuery(
                "SELECT c FROM County c ORDER BY c.name ASC", County.class);
            List<County> counties = query.getResultList();
            logger.debug("Found {} counties", counties.size());
            return counties;
        } catch (Exception e) {
            logger.error("Error fetching all counties", e);
            throw new RuntimeException("Error fetching all counties", e);
        }
    }

    @Override
    public List<County> getCountiesByRegion(String region) {
        try {
            logger.debug("Fetching counties by region: {}", region);
            TypedQuery<County> query = entityManager.createQuery(
                "SELECT c FROM County c WHERE LOWER(c.region) = LOWER(:region) ORDER BY c.name ASC", County.class);
            query.setParameter("region", region);
            List<County> counties = query.getResultList();
            logger.debug("Found {} counties in region: {}", counties.size(), region);
            return counties;
        } catch (Exception e) {
            logger.error("Error fetching counties by region: {}", region, e);
            throw new RuntimeException("Error fetching counties by region: " + region, e);
        }
    }

    @Override
    public List<String> getAllRegions() {
        try {
            logger.debug("Fetching all distinct regions");
            TypedQuery<String> query = entityManager.createQuery(
                "SELECT DISTINCT c.region FROM County c ORDER BY c.region ASC", String.class);
            List<String> regions = query.getResultList();
            logger.debug("Found {} distinct regions", regions.size());
            return regions;
        } catch (Exception e) {
            logger.error("Error fetching all regions", e);
            throw new RuntimeException("Error fetching all regions", e);
        }
    }

    @Override
    @Transactional
    public void deleteCounty(Long id) {
        try {
            logger.info("Deleting county with ID: {}", id);
            County county = entityManager.find(County.class, id);
            if (county == null) {
                logger.warn("Attempted to delete non-existent county with ID: {}", id);
                throw new RuntimeException("County not found with ID: " + id);
            }
            entityManager.remove(county);
            entityManager.flush();
            logger.info("Successfully deleted county: {}", county.getName());
        } catch (Exception e) {
            logger.error("Failed to delete county with ID: {}", id, e);
            throw new RuntimeException("Failed to delete county with ID: " + id, e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try {
            logger.debug("Checking if county exists by name: {}", name);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM County c WHERE LOWER(c.name) = LOWER(:name)", Long.class);
            query.setParameter("name", name);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("County exists by name '{}': {}", name, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if county exists by name: {}", name, e);
            return false;
        }
    }

    @Override
    public boolean existsByCode(String code) {
        try {
            logger.debug("Checking if county exists by code: {}", code);
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM County c WHERE c.code = :code", Long.class);
            query.setParameter("code", code);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.debug("County exists by code '{}': {}", code, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if county exists by code: {}", code, e);
            return false;
        }
    }

    @Override
    public long countCounties() {
        try {
            logger.debug("Counting total counties");
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM County c", Long.class);
            Long count = query.getSingleResult();
            logger.debug("Total counties count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting counties", e);
            return 0;
        }
    }
}