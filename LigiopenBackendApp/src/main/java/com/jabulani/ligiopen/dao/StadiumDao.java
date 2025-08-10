package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.location.Stadium;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StadiumDao {
    
    Stadium createStadium(Stadium stadium);
    
    Stadium updateStadium(Stadium stadium);
    
    Stadium getStadiumById(Long id);
    
    Optional<Stadium> findStadiumById(Long id);
    
    Optional<Stadium> getStadiumByName(String name);
    
    List<Stadium> getAllStadiums();
    
    List<Stadium> getStadiumsByCounty(County county);
    
    List<Stadium> getStadiumsByCountyId(Long countyId);
    
    List<Stadium> getStadiumsByCity(String city);
    
    List<Stadium> getStadiumsByCapacityRange(Integer minCapacity, Integer maxCapacity);
    
    List<Stadium> getStadiumsBySurfaceType(Stadium.SurfaceType surfaceType);
    
    List<Stadium> getVerifiedStadiums();
    
    List<Stadium> getActiveStadiums();
    
    List<Stadium> searchStadiumsByName(String searchTerm);
    
    /**
     * Find stadiums within a certain distance from coordinates
     * @param latitude Center latitude
     * @param longitude Center longitude
     * @param radiusKm Radius in kilometers
     * @return List of stadiums within radius
     */
    List<Stadium> findStadiumsNearLocation(BigDecimal latitude, BigDecimal longitude, double radiusKm);
    
    void deleteStadium(Long id);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndCounty(String name, County county);
    
    long countStadiums();
    
    long countStadiumsByCounty(Long countyId);
    
    /**
     * Get stadiums with minimum capacity for specific events
     * @param minCapacity Minimum capacity required
     * @return List of stadiums meeting capacity requirement
     */
    List<Stadium> getStadiumsWithMinCapacity(Integer minCapacity);
}