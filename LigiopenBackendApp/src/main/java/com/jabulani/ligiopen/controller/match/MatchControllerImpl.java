package com.jabulani.ligiopen.controller.match;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.match.entity.dto.PostMatchAnalysisStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryUpdateDto;
import com.jabulani.ligiopen.service.match.MatchService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class MatchControllerImpl implements MatchController{
    private final BuildResponse buildResponse;
    private final EntityManager entityManager;
    private final MatchService matchService;
    @Autowired
    public MatchControllerImpl(
            BuildResponse buildResponse,
            EntityManager entityManager,
            MatchService matchService
    ) {
        this.buildResponse = buildResponse;
        this.entityManager = entityManager;
        this.matchService = matchService;
    }
    @PostMapping("match-location")
    @Override
    public ResponseEntity<Response> createMatchLocation(
            @RequestBody MatchLocationCreationDto matchLocationCreationDto
    ) {
        System.out.println("RECEIVED-REQUEST"+matchLocationCreationDto);
        return buildResponse.createResponse("match", matchService.createMatchLocation(matchLocationCreationDto), "Match location created", HttpStatus.CREATED);
    }
    @PutMapping("match-location")
    @Override
    public ResponseEntity<Response> updateMatchLocation(
            @RequestBody MatchLocationUpdateDto matchLocationUpdateDto
    ) {
        return buildResponse.createResponse("match", matchService.updateMatchLocation(matchLocationUpdateDto), "Match location updated", HttpStatus.OK);
    }
    @PutMapping("match-location/files/{locationId}")
    @Override
    public ResponseEntity<Response> uploadMatchLocationFiles(
            @PathVariable(name = "locationId") Integer locationId,
            @RequestPart("file") MultipartFile[] files
    ) throws IOException {
        if(files.length < 1) {
            return buildResponse.createResponse(null, null, "No files uploaded", HttpStatus.BAD_REQUEST);
        }
        return buildResponse.createResponse("match", matchService.uploadMatchLocationFiles(locationId, files), "Match location files uploaded", HttpStatus.OK);
    }
    @PutMapping("match-location/file-removal/{locationId}/{fileId}")
    @Override
    public ResponseEntity<Response> removeMatchLocationFile(
            @PathVariable(name = "locationId") Integer locationId,
            @PathVariable(name = "fileId") Integer fileId
    ) {
        return buildResponse.createResponse("match", matchService.removeMatchLocationFile(locationId, fileId), "Match location file removed", HttpStatus.OK);
    }
    @GetMapping("match-location/{locationId}")
    @Override
    public ResponseEntity<Response> getMatchLocationById(@PathVariable("locationId") Integer locationId) {
        return buildResponse.createResponse("match", matchService.getMatchLocationById(locationId), "Match location fetched", HttpStatus.OK);
    }
    @GetMapping("match-location/all")
    @Override
    public ResponseEntity<Response> getAllMatchLocations(
            @RequestParam(name = "venueName", required = false) String venueName,
            @RequestParam(name = "locationName", required = false) String locationName
    ) {
        return buildResponse.createResponse("match", matchService.getAllMatchLocations(venueName, locationName), "Match locations fetched", HttpStatus.OK);
    }
    @PostMapping("match-fixture")
    @Override
    public ResponseEntity<Response> createMatchFixture(
            @RequestBody MatchFixtureCreationDto matchFixtureCreationDto
    ) {
        return buildResponse.createResponse("match", matchService.createMatchFixture(matchFixtureCreationDto), "Match fixture created", HttpStatus.CREATED);
    }
    @PutMapping("match-fixture")
    @Override
    public ResponseEntity<Response> updateMatchFixture(
            @RequestBody MatchFixtureUpdateDto matchFixtureUpdateDto
    ) {
        return buildResponse.createResponse("match", matchService.updateMatchFixture(matchFixtureUpdateDto), "Match fixture updated", HttpStatus.OK);
    }
    @PutMapping("match-fixture/status")
    @Override
    public ResponseEntity<Response> updateMatchFixtureStatus(
            @RequestBody MatchFixtureStatusUpdateDto matchFixtureStatusUpdateDto
    ) {
        return buildResponse.createResponse("match", matchService.updateMatchFixtureStatus(matchFixtureStatusUpdateDto), "Match fixture updated", HttpStatus.OK);
    }

    @PutMapping("match-fixture/file-remove/{fixtureId}/{fileId}")
    @Override
    public ResponseEntity<Response> removeMatchFixtureFile(
            @PathVariable("fixtureId") Integer fixtureId,
            @PathVariable("fileId") Integer fileId
    ) {
        return buildResponse.createResponse("match", matchService.removeMatchFixtureFile(fixtureId, fileId), "Match fixture file removed", HttpStatus.OK);
    }
    @GetMapping("match-fixture/{id}")
    @Override
    public ResponseEntity<Response> getMatchFixtureById(
            @PathVariable("id") Integer id
    ) {
        return buildResponse.createResponse("match", matchService.getMatchFixtureById(id), "Match fixture fetched", HttpStatus.OK);
    }
    @GetMapping("match-fixture/all")
    @Override
    public ResponseEntity<Response> getAllMatchFixtures(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "clubIds", required = false) List<Integer> clubIds,
            @RequestParam(name = "matchDateTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate matchDateTime
    ) {
        LocalDateTime formattedMatchDateTime = matchDateTime != null ? matchDateTime.atStartOfDay() : null;

        return buildResponse.createResponse("match", matchService.getAllMatchFixtures(status, clubIds, formattedMatchDateTime), "Match fixtures fetched", HttpStatus.OK);
    }
    @PostMapping("match-commentary")
    @Override
    public ResponseEntity<Response> createMatchCommentary(
            @RequestBody MatchCommentaryCreationDto matchCommentaryCreationDto
    ) {
        return buildResponse.createResponse("match", matchService.createMatchCommentary(matchCommentaryCreationDto), "Match commentary created", HttpStatus.CREATED);
    }
    @PutMapping("match-commentary")
    @Override
    public ResponseEntity<Response> updateMatchCommentary(
            @RequestBody MatchCommentaryUpdateDto matchCommentaryUpdateDto
    ) {
        return buildResponse.createResponse("match", matchService.updateMatchCommentary(matchCommentaryUpdateDto), "Match commentary updated", HttpStatus.OK);
    }
    @PutMapping("match-event/files/{commentaryId}")
    @Override
    public ResponseEntity<Response> uploadEventFiles(
            @PathVariable("commentaryId") Integer commentaryId,
            @RequestPart("file") MultipartFile[] files
    ) throws IOException {
        if(files.length < 1) {
            return buildResponse.createResponse(null, null, "No files uploaded", HttpStatus.BAD_REQUEST);
        }
        return buildResponse.createResponse("match", matchService.uploadEventFiles(commentaryId, files), "Match event files uploaded", HttpStatus.OK);
    }
    @GetMapping("match-commentary/{commentaryId}")
    @Override
    public ResponseEntity<Response> getMatchCommentary(@PathVariable("commentaryId") Integer commentaryId) {
        return buildResponse.createResponse("match", matchService.getMatchCommentary(commentaryId), "Match commentary fetched", HttpStatus.OK);
    }
    @GetMapping("match-commentary/all")
    @Override
    public ResponseEntity<Response> getAllMatchCommentaries() {
        return buildResponse.createResponse("match", matchService.getAllMatchCommentaries(), "Match commentaries fetched", HttpStatus.OK);
    }
    @GetMapping("post-match-details/{postMatchAnalysisId}")
    @Override
    public ResponseEntity<Response> getPostMatchDetails(@PathVariable("postMatchAnalysisId") Integer postMatchAnalysisId) {
        return buildResponse.createResponse("match", matchService.getPostMatchDetails(postMatchAnalysisId), "Post match details fetched", HttpStatus.OK);
    }

    @PutMapping("post-match-analysis/status-update")
    @Override
    public ResponseEntity<Response> updatePostMatchAnalysisStatus(@RequestBody PostMatchAnalysisStatusUpdateDto postMatchAnalysisStatusUpdateDto) {
        return buildResponse.createResponse("match", matchService.updatePostMatchAnalysisStatus(postMatchAnalysisStatusUpdateDto), "Match analysis status updated", HttpStatus.OK);
    }
}
