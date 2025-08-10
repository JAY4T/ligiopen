package com.jabulani.ligiopen.service.location;

import com.jabulani.ligiopen.dao.CountyDao;
import com.jabulani.ligiopen.entity.location.County;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CountyServiceImpl implements CountyService {

    private static final Logger logger = LoggerFactory.getLogger(CountyServiceImpl.class);

    private final CountyDao countyDao;

    @Autowired
    public CountyServiceImpl(CountyDao countyDao) {
        this.countyDao = countyDao;
    }

    @Override
    @Transactional
    public County createCounty(String name, String code, String region) {
        logger.info("Creating county: {} (Code: {}, Region: {})", name, code, region);
        
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("County name is required");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("County code is required");
        }
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("County region is required");
        }
        
        // Check for duplicates
        if (countyDao.existsByName(name.trim())) {
            throw new RuntimeException("County with name '" + name.trim() + "' already exists");
        }
        if (countyDao.existsByCode(code.trim())) {
            throw new RuntimeException("County with code '" + code.trim() + "' already exists");
        }
        
        County county = County.builder()
                .name(name.trim())
                .code(code.trim())
                .region(region.trim())
                .build();
        
        return countyDao.createCounty(county);
    }

    @Override
    @Transactional
    public County updateCounty(Long id, String name, String code, String region) {
        logger.info("Updating county ID: {} with name: {}, code: {}, region: {}", id, name, code, region);
        
        County existingCounty = countyDao.getCountyById(id);
        
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("County name is required");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("County code is required");
        }
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("County region is required");
        }
        
        // Check for duplicates (excluding current county)
        Optional<County> nameConflict = countyDao.getCountyByName(name.trim());
        if (nameConflict.isPresent() && !nameConflict.get().getId().equals(id)) {
            throw new RuntimeException("County with name '" + name.trim() + "' already exists");
        }
        
        Optional<County> codeConflict = countyDao.getCountyByCode(code.trim());
        if (codeConflict.isPresent() && !codeConflict.get().getId().equals(id)) {
            throw new RuntimeException("County with code '" + code.trim() + "' already exists");
        }
        
        existingCounty.setName(name.trim());
        existingCounty.setCode(code.trim());
        existingCounty.setRegion(region.trim());
        
        return countyDao.updateCounty(existingCounty);
    }

    @Override
    public County getCountyById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        return countyDao.getCountyById(id);
    }

    @Override
    public Optional<County> findCountyById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return countyDao.findCountyById(id);
    }

    @Override
    public Optional<County> getCountyByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return countyDao.getCountyByName(name.trim());
    }

    @Override
    public Optional<County> getCountyByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return countyDao.getCountyByCode(code.trim());
    }

    @Override
    public List<County> getAllCounties() {
        logger.debug("Fetching all counties");
        return countyDao.getAllCounties();
    }

    @Override
    public List<County> getCountiesByRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("Region is required");
        }
        logger.debug("Fetching counties for region: {}", region);
        return countyDao.getCountiesByRegion(region.trim());
    }

    @Override
    public List<String> getAllRegions() {
        logger.debug("Fetching all regions");
        return countyDao.getAllRegions();
    }

    @Override
    @Transactional
    public void deleteCounty(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        logger.info("Deleting county with ID: {}", id);
        countyDao.deleteCounty(id);
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return countyDao.existsByName(name.trim());
    }

    @Override
    public boolean existsByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return countyDao.existsByCode(code.trim());
    }

    @Override
    public long getCountyCount() {
        return countyDao.countCounties();
    }

    @Override
    @Transactional
    public void initializeKenyanCounties() {
        logger.info("Initializing Kenyan counties data...");
        
        long existingCount = countyDao.countCounties();
        if (existingCount >= 47) {
            logger.info("Counties already initialized. Found {} counties.", existingCount);
            return;
        }
        
        // Kenya's 47 counties data
        String[][] kenyaCounties = {
            // Central Region
            {"Nairobi", "047", "Central"},
            {"Kiambu", "022", "Central"},
            {"Murang'a", "021", "Central"},
            {"Nyeri", "018", "Central"},
            {"Kirinyaga", "020", "Central"},
            {"Nyandarua", "019", "Central"},
            
            // Coast Region
            {"Mombasa", "001", "Coast"},
            {"Kilifi", "003", "Coast"},
            {"Tana River", "006", "Coast"},
            {"Lamu", "004", "Coast"},
            {"Taita-Taveta", "005", "Coast"},
            {"Kwale", "002", "Coast"},
            
            // Eastern Region
            {"Machakos", "016", "Eastern"},
            {"Kitui", "015", "Eastern"},
            {"Makueni", "017", "Eastern"},
            {"Embu", "014", "Eastern"},
            {"Tharaka-Nithi", "013", "Eastern"},
            {"Meru", "012", "Eastern"},
            {"Isiolo", "011", "Eastern"},
            {"Marsabit", "010", "Eastern"},
            
            // North Eastern Region
            {"Garissa", "007", "North Eastern"},
            {"Wajir", "008", "North Eastern"},
            {"Mandera", "009", "North Eastern"},
            
            // Nyanza Region
            {"Siaya", "041", "Nyanza"},
            {"Kisumu", "042", "Nyanza"},
            {"Homa Bay", "043", "Nyanza"},
            {"Migori", "044", "Nyanza"},
            {"Kisii", "045", "Nyanza"},
            {"Nyamira", "046", "Nyanza"},
            
            // Rift Valley Region
            {"Turkana", "023", "Rift Valley"},
            {"West Pokot", "024", "Rift Valley"},
            {"Samburu", "025", "Rift Valley"},
            {"Trans Nzoia", "026", "Rift Valley"},
            {"Uasin Gishu", "027", "Rift Valley"},
            {"Elgeyo-Marakwet", "028", "Rift Valley"},
            {"Nandi", "029", "Rift Valley"},
            {"Baringo", "030", "Rift Valley"},
            {"Laikipia", "031", "Rift Valley"},
            {"Nakuru", "032", "Rift Valley"},
            {"Narok", "033", "Rift Valley"},
            {"Kajiado", "034", "Rift Valley"},
            {"Kericho", "035", "Rift Valley"},
            {"Bomet", "036", "Rift Valley"},
            
            // Western Region
            {"Kakamega", "037", "Western"},
            {"Vihiga", "038", "Western"},
            {"Bungoma", "039", "Western"},
            {"Busia", "040", "Western"}
        };
        
        int created = 0;
        int skipped = 0;
        
        for (String[] countyData : kenyaCounties) {
            String name = countyData[0];
            String code = countyData[1];
            String region = countyData[2];
            
            try {
                if (!countyDao.existsByName(name)) {
                    County county = County.builder()
                            .name(name)
                            .code(code)
                            .region(region)
                            .build();
                    countyDao.createCounty(county);
                    created++;
                    logger.debug("Created county: {} ({})", name, region);
                } else {
                    skipped++;
                    logger.debug("Skipped existing county: {}", name);
                }
            } catch (Exception e) {
                logger.error("Failed to create county: {} - {}", name, e.getMessage());
            }
        }
        
        logger.info("County initialization complete. Created: {}, Skipped: {}, Total: {}", 
                   created, skipped, countyDao.countCounties());
    }
}