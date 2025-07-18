package com.jabulani.ligiopen.controller.match;

import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.match.entity.dto.PostMatchAnalysisStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.PostMatchAnalysisDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface MatchController {
    ResponseEntity<Response> createMatchLocation(MatchLocationCreationDto matchLocationCreationDto);
    ResponseEntity<Response> updateMatchLocation(MatchLocationUpdateDto matchLocationUpdateDto);

    ResponseEntity<Response> uploadMatchLocationFiles(Integer locationId, MultipartFile[] files) throws IOException;
    ResponseEntity<Response> removeMatchLocationFile(Integer locationId, Integer fileId);
    ResponseEntity<Response> getMatchLocationById(Integer locationId);
    ResponseEntity<Response> getAllMatchLocations(String venueName, String locationName);
    ResponseEntity<Response> createMatchFixture(MatchFixtureCreationDto matchFixtureCreationDto);
    ResponseEntity<Response> updateMatchFixture(MatchFixtureUpdateDto matchFixtureUpdateDto);
    ResponseEntity<Response> updateMatchFixtureStatus(MatchFixtureStatusUpdateDto matchFixtureStatusUpdateDto);

    ResponseEntity<Response> removeMatchFixtureFile(Integer fixtureId, Integer fileId);

    ResponseEntity<Response> getMatchFixtureById(Integer id);
    ResponseEntity<Response> getAllMatchFixtures(String status, List<Integer> clubIds, LocalDate matchDateTime);
    ResponseEntity<Response> createMatchCommentary(MatchCommentaryCreationDto matchCommentaryCreationDto);
    ResponseEntity<Response> updateMatchCommentary(MatchCommentaryUpdateDto matchCommentaryUpdateDto);
    ResponseEntity<Response> uploadEventFiles(Integer commentaryId, MultipartFile[] files) throws IOException;

    ResponseEntity<Response> getMatchCommentary(Integer commentaryId);
    ResponseEntity<Response> getAllMatchCommentaries();
    ResponseEntity<Response> getPostMatchDetails(Integer postMatchAnalysisId);

    ResponseEntity<Response> updatePostMatchAnalysisStatus(PostMatchAnalysisStatusUpdateDto postMatchAnalysisStatusUpdateDto);
}
