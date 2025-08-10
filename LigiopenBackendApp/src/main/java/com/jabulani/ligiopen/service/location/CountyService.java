package com.jabulani.ligiopen.service.location;

import com.jabulani.ligiopen.entity.location.County;

import java.util.List;
import java.util.Optional;

public interface CountyService {
    
    /**
     * Create a new county
     * @param name County name
     * @param code County code
     * @param region County region
     * @return Created County entity
     */
    County createCounty(String name, String code, String region);
    
    /**
     * Update an existing county
     * @param id County ID to update
     * @param name New county name
     * @param code New county code  
     * @param region New county region
     * @return Updated County entity
     */
    County updateCounty(Long id, String name, String code, String region);
    
    /**
     * Get county by ID
     * @param id County ID
     * @return County entity
     * @throws RuntimeException if county not found
     */
    County getCountyById(Long id);
    
    /**
     * Find county by ID (safe version)
     * @param id County ID
     * @return Optional County entity
     */
    Optional<County> findCountyById(Long id);
    
    /**
     * Get county by name
     * @param name County name
     * @return Optional County entity
     */
    Optional<County> getCountyByName(String name);
    
    /**
     * Get county by code
     * @param code County code
     * @return Optional County entity
     */
    Optional<County> getCountyByCode(String code);
    
    /**
     * Get all counties ordered by name
     * @return List of all counties
     */
    List<County> getAllCounties();
    
    /**
     * Get counties by region
     * @param region Region name
     * @return List of counties in the region
     */
    List<County> getCountiesByRegion(String region);
    
    /**
     * Get all distinct regions
     * @return List of region names
     */
    List<String> getAllRegions();
    
    /**
     * Delete county by ID
     * @param id County ID to delete
     * @throws RuntimeException if county not found or has dependencies
     */
    void deleteCounty(Long id);
    
    /**
     * Check if county exists by name
     * @param name County name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Check if county exists by code
     * @param code County code
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Get total number of counties
     * @return County count
     */
    long getCountyCount();
    
    /**
     * Initialize Kenya's 47 counties if not already present
     * This method will populate the database with all Kenyan counties
     */
    void initializeKenyanCounties();
}