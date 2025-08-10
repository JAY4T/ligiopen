package com.jabulani.ligiopen.service.location;

import com.jabulani.ligiopen.dao.CountyDao;
import com.jabulani.ligiopen.dao.StadiumDao;
import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.location.Stadium;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StadiumServiceImpl implements StadiumService {

    private static final Logger logger = LoggerFactory.getLogger(StadiumServiceImpl.class);

    private final StadiumDao stadiumDao;
    private final CountyDao countyDao;

    @Autowired
    public StadiumServiceImpl(StadiumDao stadiumDao, CountyDao countyDao) {
        this.stadiumDao = stadiumDao;
        this.countyDao = countyDao;
    }

    @Override
    @Transactional
    public Stadium createStadium(String name, String city, String town, Long countyId, 
                               String address, Integer capacity, Stadium.SurfaceType surfaceType) {
        logger.info("Creating stadium: {} in {}, {}", name, city, town);
        
        validateStadiumInput(name, city, countyId, capacity);
        
        County county = countyDao.getCountyById(countyId);
        
        // Check for duplicate stadium name in the same county
        if (stadiumDao.existsByNameAndCounty(name.trim(), county)) {
            throw new RuntimeException("Stadium with name '" + name.trim() + "' already exists in " + county.getName());
        }
        
        Stadium stadium = Stadium.builder()
                .name(name.trim())
                .city(city.trim())
                .town(town != null ? town.trim() : null)
                .county(county)
                .address(address != null ? address.trim() : null)
                .capacity(capacity)
                .surfaceType(surfaceType != null ? surfaceType : Stadium.SurfaceType.NATURAL_GRASS)
                .hasFloodlights(false)
                .hasRoof(false)
                .isVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        
        return stadiumDao.createStadium(stadium);
    }

    @Override
    @Transactional
    public Stadium createStadiumWithCoordinates(String name, String city, String town, Long countyId,
                                              String address, BigDecimal latitude, BigDecimal longitude,
                                              Integer capacity, Stadium.SurfaceType surfaceType,
                                              Boolean hasFloodlights, Boolean hasRoof) {
        logger.info("Creating stadium with coordinates: {} at {}, {}", name, latitude, longitude);
        
        validateStadiumInput(name, city, countyId, capacity);
        validateCoordinates(latitude, longitude);
        
        County county = countyDao.getCountyById(countyId);
        
        // Check for duplicate stadium name in the same county
        if (stadiumDao.existsByNameAndCounty(name.trim(), county)) {
            throw new RuntimeException("Stadium with name '" + name.trim() + "' already exists in " + county.getName());
        }
        
        Stadium stadium = Stadium.builder()
                .name(name.trim())
                .city(city.trim())
                .town(town != null ? town.trim() : null)
                .county(county)
                .address(address != null ? address.trim() : null)
                .latitude(latitude)
                .longitude(longitude)
                .capacity(capacity)
                .surfaceType(surfaceType != null ? surfaceType : Stadium.SurfaceType.NATURAL_GRASS)
                .hasFloodlights(hasFloodlights != null ? hasFloodlights : false)
                .hasRoof(hasRoof != null ? hasRoof : false)
                .isVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        
        return stadiumDao.createStadium(stadium);
    }

    @Override
    @Transactional
    public Stadium updateStadium(Long id, String name, String city, String town, Long countyId,
                               String address, Integer capacity, Stadium.SurfaceType surfaceType) {
        logger.info("Updating stadium ID: {} with name: {}", id, name);
        
        Stadium existingStadium = stadiumDao.getStadiumById(id);
        validateStadiumInput(name, city, countyId, capacity);
        
        County county = countyDao.getCountyById(countyId);
        
        // Check for duplicate stadium name in the same county (excluding current stadium)
        Optional<Stadium> nameConflict = stadiumDao.getStadiumByName(name.trim());
        if (nameConflict.isPresent() && !nameConflict.get().getId().equals(id) &&
            nameConflict.get().getCounty().getId().equals(countyId)) {
            throw new RuntimeException("Stadium with name '" + name.trim() + "' already exists in " + county.getName());
        }
        
        existingStadium.setName(name.trim());
        existingStadium.setCity(city.trim());
        existingStadium.setTown(town != null ? town.trim() : null);
        existingStadium.setCounty(county);
        existingStadium.setAddress(address != null ? address.trim() : null);
        existingStadium.setCapacity(capacity);
        existingStadium.setSurfaceType(surfaceType != null ? surfaceType : Stadium.SurfaceType.NATURAL_GRASS);
        
        return stadiumDao.updateStadium(existingStadium);
    }

    @Override
    @Transactional
    public Stadium updateStadiumCoordinates(Long id, BigDecimal latitude, BigDecimal longitude) {
        logger.info("Updating coordinates for stadium ID: {} to {}, {}", id, latitude, longitude);
        
        Stadium stadium = stadiumDao.getStadiumById(id);
        validateCoordinates(latitude, longitude);
        
        stadium.setLatitude(latitude);
        stadium.setLongitude(longitude);
        
        return stadiumDao.updateStadium(stadium);
    }

    @Override
    @Transactional
    public Stadium updateStadiumFacilities(Long id, Boolean hasFloodlights, Boolean hasRoof,
                                         Integer yearBuilt, String contactPhone, String contactEmail,
                                         BigDecimal rentalFee) {
        logger.info("Updating facilities for stadium ID: {}", id);
        
        Stadium stadium = stadiumDao.getStadiumById(id);
        
        if (hasFloodlights != null) {
            stadium.setHasFloodlights(hasFloodlights);
        }
        if (hasRoof != null) {
            stadium.setHasRoof(hasRoof);
        }
        if (yearBuilt != null) {
            if (yearBuilt < 1800 || yearBuilt > LocalDateTime.now().getYear() + 5) {
                throw new IllegalArgumentException("Invalid year built: " + yearBuilt);
            }
            stadium.setYearBuilt(yearBuilt);
        }
        if (contactPhone != null) {
            stadium.setContactPhone(contactPhone.trim());
        }
        if (contactEmail != null) {
            stadium.setContactEmail(contactEmail.trim());
        }
        if (rentalFee != null) {
            if (rentalFee.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Rental fee cannot be negative");
            }
            stadium.setRentalFee(rentalFee);
        }
        
        return stadiumDao.updateStadium(stadium);
    }

    @Override
    @Transactional
    public Stadium verifyStadium(Long id, boolean verified) {
        logger.info("Setting verification status for stadium ID: {} to {}", id, verified);
        
        Stadium stadium = stadiumDao.getStadiumById(id);
        stadium.setIsVerified(verified);
        
        return stadiumDao.updateStadium(stadium);
    }

    @Override
    @Transactional
    public Stadium setStadiumActive(Long id, boolean active) {
        logger.info("Setting active status for stadium ID: {} to {}", id, active);
        
        Stadium stadium = stadiumDao.getStadiumById(id);
        stadium.setIsActive(active);
        
        return stadiumDao.updateStadium(stadium);
    }

    @Override
    public Stadium getStadiumById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Stadium ID is required");
        }
        return stadiumDao.getStadiumById(id);
    }

    @Override
    public Optional<Stadium> findStadiumById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return stadiumDao.findStadiumById(id);
    }

    @Override
    public Optional<Stadium> getStadiumByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return stadiumDao.getStadiumByName(name.trim());
    }

    @Override
    public List<Stadium> getAllStadiums() {
        logger.debug("Fetching all stadiums");
        return stadiumDao.getAllStadiums();
    }

    @Override
    public List<Stadium> getStadiumsByCounty(Long countyId) {
        if (countyId == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        logger.debug("Fetching stadiums for county ID: {}", countyId);
        return stadiumDao.getStadiumsByCountyId(countyId);
    }

    @Override
    public List<Stadium> getStadiumsByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        logger.debug("Fetching stadiums for city: {}", city);
        return stadiumDao.getStadiumsByCity(city.trim());
    }

    @Override
    public List<Stadium> getStadiumsByCapacityRange(Integer minCapacity, Integer maxCapacity) {
        if (minCapacity != null && minCapacity < 0) {
            throw new IllegalArgumentException("Minimum capacity cannot be negative");
        }
        if (maxCapacity != null && maxCapacity < 0) {
            throw new IllegalArgumentException("Maximum capacity cannot be negative");
        }
        if (minCapacity != null && maxCapacity != null && minCapacity > maxCapacity) {
            throw new IllegalArgumentException("Minimum capacity cannot be greater than maximum capacity");
        }
        
        logger.debug("Fetching stadiums with capacity range: {} - {}", minCapacity, maxCapacity);
        return stadiumDao.getStadiumsByCapacityRange(minCapacity, maxCapacity);
    }

    @Override
    public List<Stadium> getStadiumsBySurfaceType(Stadium.SurfaceType surfaceType) {
        if (surfaceType == null) {
            throw new IllegalArgumentException("Surface type is required");
        }
        logger.debug("Fetching stadiums with surface type: {}", surfaceType);
        return stadiumDao.getStadiumsBySurfaceType(surfaceType);
    }

    @Override
    public List<Stadium> getVerifiedStadiums() {
        logger.debug("Fetching verified stadiums");
        return stadiumDao.getVerifiedStadiums();
    }

    @Override
    public List<Stadium> getActiveStadiums() {
        logger.debug("Fetching active stadiums");
        return stadiumDao.getActiveStadiums();
    }

    @Override
    public List<Stadium> searchStadiumsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllStadiums();
        }
        logger.debug("Searching stadiums with term: {}", searchTerm);
        return stadiumDao.searchStadiumsByName(searchTerm.trim());
    }

    @Override
    public List<Stadium> findStadiumsNearLocation(BigDecimal latitude, BigDecimal longitude, double radiusKm) {
        validateCoordinates(latitude, longitude);
        if (radiusKm <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        if (radiusKm > 1000) {
            throw new IllegalArgumentException("Radius cannot exceed 1000 km");
        }
        
        logger.debug("Finding stadiums near {}, {} within {} km", latitude, longitude, radiusKm);
        return stadiumDao.findStadiumsNearLocation(latitude, longitude, radiusKm);
    }

    @Override
    public List<Stadium> getStadiumsForEvents(Integer minCapacity) {
        if (minCapacity == null || minCapacity < 0) {
            throw new IllegalArgumentException("Minimum capacity must be a positive number");
        }
        logger.debug("Fetching stadiums suitable for events (min capacity: {})", minCapacity);
        return stadiumDao.getStadiumsWithMinCapacity(minCapacity);
    }

    @Override
    @Transactional
    public void deleteStadium(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Stadium ID is required");
        }
        logger.info("Deleting stadium with ID: {}", id);
        stadiumDao.deleteStadium(id);
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return stadiumDao.existsByName(name.trim());
    }

    @Override
    public boolean existsByNameAndCounty(String name, Long countyId) {
        if (name == null || name.trim().isEmpty() || countyId == null) {
            return false;
        }
        try {
            County county = countyDao.getCountyById(countyId);
            return stadiumDao.existsByNameAndCounty(name.trim(), county);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long getStadiumCount() {
        return stadiumDao.countStadiums();
    }

    @Override
    public long getStadiumCountByCounty(Long countyId) {
        if (countyId == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        return stadiumDao.countStadiumsByCounty(countyId);
    }

    @Override
    @Transactional
    public void initializeMajorKenyanStadiums() {
        logger.info("Initializing major Kenyan stadiums...");
        
        long existingCount = stadiumDao.countStadiums();
        if (existingCount >= 5) {
            logger.info("Stadiums already initialized. Found {} stadiums.", existingCount);
            return;
        }
        
        // Get required counties
        Optional<County> nairobiOpt = countyDao.getCountyByName("Nairobi");
        Optional<County> mombasaOpt = countyDao.getCountyByName("Mombasa");
        Optional<County> kisumuOpt = countyDao.getCountyByName("Kisumu");
        Optional<County> nakuruOpt = countyDao.getCountyByName("Nakuru");
        
        if (nairobiOpt.isEmpty()) {
            logger.warn("Cannot initialize stadiums - Nairobi county not found");
            return;
        }
        
        County nairobi = nairobiOpt.get();
        int created = 0;
        
        try {
            // Kasarani Stadium
            if (!stadiumDao.existsByNameAndCounty("Kasarani Stadium", nairobi)) {
                Stadium kasarani = Stadium.builder()
                        .name("Kasarani Stadium")
                        .city("Nairobi")
                        .town("Kasarani")
                        .county(nairobi)
                        .address("Thika Road, Kasarani, Nairobi")
                        .latitude(new BigDecimal("-1.2181"))
                        .longitude(new BigDecimal("36.8947"))
                        .capacity(60000)
                        .surfaceType(Stadium.SurfaceType.NATURAL_GRASS)
                        .hasFloodlights(true)
                        .hasRoof(false)
                        .yearBuilt(1987)
                        .isVerified(true)
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                stadiumDao.createStadium(kasarani);
                created++;
            }
            
            // Nyayo Stadium
            if (!stadiumDao.existsByNameAndCounty("Nyayo National Stadium", nairobi)) {
                Stadium nyayo = Stadium.builder()
                        .name("Nyayo National Stadium")
                        .city("Nairobi")
                        .town("Nairobi CBD")
                        .county(nairobi)
                        .address("Uhuru Highway, Nairobi")
                        .latitude(new BigDecimal("-1.3006"))
                        .longitude(new BigDecimal("36.8219"))
                        .capacity(30000)
                        .surfaceType(Stadium.SurfaceType.NATURAL_GRASS)
                        .hasFloodlights(true)
                        .hasRoof(false)
                        .yearBuilt(1983)
                        .isVerified(true)
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                stadiumDao.createStadium(nyayo);
                created++;
            }
            
            // Add other major stadiums if counties exist
            if (mombasaOpt.isPresent() && !stadiumDao.existsByNameAndCounty("Mombasa Municipal Stadium", mombasaOpt.get())) {
                Stadium mombasa = Stadium.builder()
                        .name("Mombasa Municipal Stadium")
                        .city("Mombasa")
                        .town("Mombasa")
                        .county(mombasaOpt.get())
                        .capacity(10000)
                        .surfaceType(Stadium.SurfaceType.NATURAL_GRASS)
                        .hasFloodlights(true)
                        .isVerified(true)
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                stadiumDao.createStadium(mombasa);
                created++;
            }
            
            if (kisumuOpt.isPresent() && !stadiumDao.existsByNameAndCounty("Moi Stadium Kisumu", kisumuOpt.get())) {
                Stadium kisumu = Stadium.builder()
                        .name("Moi Stadium Kisumu")
                        .city("Kisumu")
                        .town("Kisumu")
                        .county(kisumuOpt.get())
                        .capacity(35000)
                        .surfaceType(Stadium.SurfaceType.NATURAL_GRASS)
                        .hasFloodlights(true)
                        .isVerified(true)
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                stadiumDao.createStadium(kisumu);
                created++;
            }
            
            if (nakuruOpt.isPresent() && !stadiumDao.existsByNameAndCounty("Afraha Stadium", nakuruOpt.get())) {
                Stadium afraha = Stadium.builder()
                        .name("Afraha Stadium")
                        .city("Nakuru")
                        .town("Nakuru")
                        .county(nakuruOpt.get())
                        .capacity(8200)
                        .surfaceType(Stadium.SurfaceType.NATURAL_GRASS)
                        .hasFloodlights(false)
                        .isVerified(true)
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                stadiumDao.createStadium(afraha);
                created++;
            }
            
        } catch (Exception e) {
            logger.error("Error initializing major stadiums", e);
        }
        
        logger.info("Stadium initialization complete. Created: {}, Total: {}", created, stadiumDao.countStadiums());
    }
    
    // Private helper methods
    
    private void validateStadiumInput(String name, String city, Long countyId, Integer capacity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Stadium name is required");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        if (countyId == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        if (capacity != null && capacity < 0) {
            throw new IllegalArgumentException("Stadium capacity cannot be negative");
        }
        if (capacity != null && capacity > 200000) {
            throw new IllegalArgumentException("Stadium capacity seems unrealistic (max 200,000)");
        }
    }
    
    private void validateCoordinates(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Both latitude and longitude are required");
        }
        
        // Kenya's approximate coordinate bounds
        if (latitude.compareTo(new BigDecimal("5.0")) > 0 || latitude.compareTo(new BigDecimal("-5.0")) < 0) {
            throw new IllegalArgumentException("Latitude is outside Kenya's bounds (-5.0 to 5.0)");
        }
        if (longitude.compareTo(new BigDecimal("42.0")) > 0 || longitude.compareTo(new BigDecimal("34.0")) < 0) {
            throw new IllegalArgumentException("Longitude is outside Kenya's bounds (34.0 to 42.0)");
        }
    }
}