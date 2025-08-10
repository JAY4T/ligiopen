package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.location.County;

import java.util.List;
import java.util.Optional;

public interface CountyDao {
    
    County createCounty(County county);
    
    County updateCounty(County county);
    
    County getCountyById(Long id);
    
    Optional<County> findCountyById(Long id);
    
    Optional<County> getCountyByName(String name);
    
    Optional<County> getCountyByCode(String code);
    
    List<County> getAllCounties();
    
    List<County> getCountiesByRegion(String region);
    
    List<String> getAllRegions();
    
    void deleteCounty(Long id);
    
    boolean existsByName(String name);
    
    boolean existsByCode(String code);
    
    long countCounties();
}