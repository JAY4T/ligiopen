package com.jabulani.ligiopen.controller.location;

import com.jabulani.ligiopen.dto.location.StadiumSearchDto;
import com.jabulani.ligiopen.dto.location.StadiumSubmissionDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface StadiumManagementController {

    // Stadium Search and Discovery
    ResponseEntity<Object> searchStadiums(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long countyId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(defaultValue = "10") Double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    ResponseEntity<Object> getStadiumsNearby(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "5") Double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

    ResponseEntity<Object> getStadiumById(@PathVariable Long stadiumId);

    ResponseEntity<Object> getStadiumsByCounty(
            @PathVariable Long countyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    // Stadium Submission
    ResponseEntity<Object> submitStadium(@Valid @RequestBody StadiumSubmissionDto submissionDto);

    ResponseEntity<Object> uploadStadiumPhoto(
            @PathVariable Long stadiumId,
            @RequestParam("file") MultipartFile file);

    // My Submissions
    ResponseEntity<Object> getMyStadiumSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

    // Stadium Management (Admin)
    ResponseEntity<Object> getPendingStadiumSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    ResponseEntity<Object> approveStadiumSubmission(
            @PathVariable Long stadiumId,
            @RequestBody(required = false) String approvalNotes);

    ResponseEntity<Object> rejectStadiumSubmission(
            @PathVariable Long stadiumId,
            @RequestBody String rejectionReason);

    ResponseEntity<Object> mergeStadiumSubmission(
            @PathVariable Long submissionId,
            @PathVariable Long existingStadiumId,
            @RequestBody(required = false) String mergeNotes);

    // Stadium Statistics
    ResponseEntity<Object> getStadiumUsageStats(@PathVariable Long stadiumId);
}