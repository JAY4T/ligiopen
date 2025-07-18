package com.jabulani.ligiopen.service.match;

import com.jabulani.ligiopen.model.match.entity.dto.PostMatchAnalysisStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.PostMatchAnalysisDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface MatchService {
    MatchLocationDto createMatchLocation(MatchLocationCreationDto matchLocationCreationDto);
    MatchLocationDto updateMatchLocation(MatchLocationUpdateDto matchLocationUpdateDto);

    MatchLocationDto uploadMatchLocationFiles(Integer locationId, MultipartFile[] files) throws IOException;
    MatchLocationDto removeMatchLocationFile(Integer locationId, Integer fileId);
    MatchLocationDto getMatchLocationById(Integer locationId);
    MatchLocationDto getMatchLocationByName(String name);
    List<MatchLocationDto> getAllMatchLocations(String venueName, String locationName);
    MatchFixtureDto createMatchFixture(MatchFixtureCreationDto matchFixtureCreationDto);
    MatchFixtureDto updateMatchFixture(MatchFixtureUpdateDto matchFixtureUpdateDto);
    MatchFixtureDto updateMatchFixtureStatus(MatchFixtureStatusUpdateDto matchFixtureStatusUpdateDto);

    MatchFixtureDto uploadMatchFixtureFiles(Integer fixtureId, MultipartFile[] files) throws IOException;
    MatchFixtureDto removeMatchFixtureFile(Integer fixtureId, Integer fileId);

    MatchFixtureDto getMatchFixtureById(Integer id);
    List<MatchFixtureDto> getAllMatchFixtures(String status, List<Integer> clubIds, LocalDateTime matchDateTime);
    MatchCommentaryDto createMatchCommentary(MatchCommentaryCreationDto matchCommentaryCreationDto);


    MatchCommentaryDto updateMatchCommentary(MatchCommentaryUpdateDto matchCommentaryUpdateDto);
    MatchCommentaryDto uploadEventFiles(Integer commentaryId, MultipartFile[] files) throws IOException;

    MatchCommentaryDto getMatchCommentary(Integer commentaryId);
    List<MatchCommentaryDto> getAllMatchCommentaries();

    PostMatchAnalysisDto getPostMatchDetails(Integer postMatchAnalysisId);

    PostMatchAnalysisDto updatePostMatchAnalysisStatus(PostMatchAnalysisStatusUpdateDto postMatchAnalysisStatusUpdateDto);
}
