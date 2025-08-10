package com.jabulani.ligiopen.service.location;

import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.location.Stadium;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StadiumService {
    
    /**
     * Create a new stadium
     * @param name Stadium name
     * @param city City name
     * @param town Town name
     * @param countyId County ID
     * @param address Full address
     * @param capacity Stadium capacity
     * @param surfaceType Surface type
     * @return Created Stadium entity
     */
    Stadium createStadium(String name, String city, String town, Long countyId, 
                         String address, Integer capacity, Stadium.SurfaceType surfaceType);
    
    /**
     * Create a stadium with geographic coordinates
     * @param name Stadium name
     * @param city City name
     * @param town Town name
     * @param countyId County ID
     * @param address Full address
     * @param latitude GPS latitude
     * @param longitude GPS longitude
     * @param capacity Stadium capacity
     * @param surfaceType Surface type
     * @param hasFloodlights Has floodlights
     * @param hasRoof Has roof
     * @return Created Stadium entity
     */
    Stadium createStadiumWithCoordinates(String name, String city, String town, Long countyId,
                                       String address, BigDecimal latitude, BigDecimal longitude,
                                       Integer capacity, Stadium.SurfaceType surfaceType,
                                       Boolean hasFloodlights, Boolean hasRoof);
    
    /**
     * Update an existing stadium
     * @param id Stadium ID
     * @param name Stadium name
     * @param city City name
     * @param town Town name
     * @param countyId County ID
     * @param address Full address
     * @param capacity Stadium capacity
     * @param surfaceType Surface type
     * @return Updated Stadium entity
     */
    Stadium updateStadium(Long id, String name, String city, String town, Long countyId,
                         String address, Integer capacity, Stadium.SurfaceType surfaceType);
    
    /**
     * Update stadium geographic coordinates
     * @param id Stadium ID
     * @param latitude GPS latitude
     * @param longitude GPS longitude
     * @return Updated Stadium entity
     */
    Stadium updateStadiumCoordinates(Long id, BigDecimal latitude, BigDecimal longitude);
    
    /**
     * Update stadium facilities
     * @param id Stadium ID
     * @param hasFloodlights Has floodlights
     * @param hasRoof Has roof
     * @param yearBuilt Year built
     * @param contactPhone Contact phone
     * @param contactEmail Contact email
     * @param rentalFee Rental fee
     * @return Updated Stadium entity
     */
    Stadium updateStadiumFacilities(Long id, Boolean hasFloodlights, Boolean hasRoof,
                                  Integer yearBuilt, String contactPhone, String contactEmail,
                                  BigDecimal rentalFee);
    
    /**
     * Verify a stadium (admin action)
     * @param id Stadium ID
     * @param verified Verification status
     * @return Updated Stadium entity
     */
    Stadium verifyStadium(Long id, boolean verified);
    
    /**
     * Activate/deactivate a stadium
     * @param id Stadium ID
     * @param active Active status
     * @return Updated Stadium entity
     */
    Stadium setStadiumActive(Long id, boolean active);
    
    /**
     * Get stadium by ID
     * @param id Stadium ID
     * @return Stadium entity
     * @throws RuntimeException if stadium not found
     */
    Stadium getStadiumById(Long id);
    
    /**
     * Find stadium by ID (safe version)
     * @param id Stadium ID
     * @return Optional Stadium entity
     */
    Optional<Stadium> findStadiumById(Long id);
    
    /**
     * Get stadium by name
     * @param name Stadium name
     * @return Optional Stadium entity
     */
    Optional<Stadium> getStadiumByName(String name);
    
    /**
     * Get all stadiums
     * @return List of all stadiums
     */
    List<Stadium> getAllStadiums();
    
    /**
     * Get stadiums by county
     * @param countyId County ID
     * @return List of stadiums in the county
     */
    List<Stadium> getStadiumsByCounty(Long countyId);
    
    /**
     * Get stadiums by city
     * @param city City name
     * @return List of stadiums in the city
     */
    List<Stadium> getStadiumsByCity(String city);
    
    /**
     * Get stadiums by capacity range
     * @param minCapacity Minimum capacity (can be null)
     * @param maxCapacity Maximum capacity (can be null)
     * @return List of stadiums within capacity range
     */
    List<Stadium> getStadiumsByCapacityRange(Integer minCapacity, Integer maxCapacity);
    
    /**
     * Get stadiums by surface type
     * @param surfaceType Surface type
     * @return List of stadiums with specified surface
     */
    List<Stadium> getStadiumsBySurfaceType(Stadium.SurfaceType surfaceType);
    
    /**
     * Get verified stadiums only
     * @return List of verified stadiums
     */
    List<Stadium> getVerifiedStadiums();
    
    /**
     * Get active stadiums only
     * @return List of active stadiums
     */
    List<Stadium> getActiveStadiums();
    
    /**
     * Search stadiums by name
     * @param searchTerm Search term
     * @return List of stadiums matching search
     */
    List<Stadium> searchStadiumsByName(String searchTerm);
    
    /**
     * Find stadiums near a location
     * @param latitude Center latitude
     * @param longitude Center longitude
     * @param radiusKm Radius in kilometers
     * @return List of stadiums within radius
     */
    List<Stadium> findStadiumsNearLocation(BigDecimal latitude, BigDecimal longitude, double radiusKm);
    
    /**
     * Get stadiums suitable for specific events (minimum capacity)
     * @param minCapacity Minimum capacity required
     * @return List of stadiums meeting capacity requirement
     */
    List<Stadium> getStadiumsForEvents(Integer minCapacity);
    
    /**
     * Delete stadium
     * @param id Stadium ID
     * @throws RuntimeException if stadium not found or has dependencies
     */
    void deleteStadium(Long id);
    
    /**
     * Check if stadium exists by name
     * @param name Stadium name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Check if stadium exists by name in county
     * @param name Stadium name
     * @param countyId County ID
     * @return true if exists, false otherwise
     */
    boolean existsByNameAndCounty(String name, Long countyId);
    
    /**
     * Get total number of stadiums
     * @return Stadium count
     */
    long getStadiumCount();
    
    /**
     * Get stadium count by county
     * @param countyId County ID
     * @return Stadium count in county
     */
    long getStadiumCountByCounty(Long countyId);
    
    /**
     * Initialize major Kenyan stadiums if not already present
     */
    void initializeMajorKenyanStadiums();
}