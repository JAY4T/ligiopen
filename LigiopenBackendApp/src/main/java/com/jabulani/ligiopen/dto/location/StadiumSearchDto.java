package com.jabulani.ligiopen.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StadiumSearchDto {
    
    private Long id;
    private String name;
    private String city;
    private String town;
    private String county;
    private Integer capacity;
    private String surfaceType;
    private Boolean hasFloodlights;
    private Boolean hasParking;
    private Boolean hasConcessions;
    private String address;
    private Double latitude;
    private Double longitude;
    
    // Search-specific fields
    private Double distanceKm; // Distance from search point
    private Integer clubsUsingCount; // Number of clubs using this stadium
    private List<String> clubsUsing; // Names of clubs using this stadium
    private String verificationStatus; // APPROVED, PENDING, etc.
    
    // Admin fields
    private String submittedByName; // Name of user who submitted
    private String submissionDate;
    private String approvalStatus;
}