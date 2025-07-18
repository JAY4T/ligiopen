package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.dto.*;
import com.jabulani.ligiopen.service.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class ClubControllerImpl implements ClubController{
    private final ClubService clubService;
    private final BuildResponse buildResponse;
    @Autowired
    public ClubControllerImpl(
            ClubService clubService,
            BuildResponse buildResponse
    ) {
        this.clubService = clubService;
        this.buildResponse = buildResponse;
    }
    @PostMapping("club")
    @Override
    public ResponseEntity<Response> addClub(
            @RequestPart("data") AddClubDto addClubDto,
            @RequestPart("file") MultipartFile logo
    ) throws IOException {
        return buildResponse.createResponse("club", clubService.addClub(addClubDto, logo), "Club added", HttpStatus.CREATED);
    }

    @PostMapping("club/airtable/save")
    @Override
    public ResponseEntity<Response> addClubsFromAirtable() throws Exception {
        return buildResponse.createResponse("club", clubService.addClubsFromAirtable(), "Club added", HttpStatus.CREATED);
    }

    @PutMapping("club/details")
    @Override
    public ResponseEntity<Response> updateClubDetails(
            @RequestBody UpdateClubDto updateClubDto
    ) {
        return buildResponse.createResponse("club", clubService.updateClubDetails(updateClubDto), "CLub updated", HttpStatus.OK);
    }
    @PutMapping("club/logo/{clubId}")
    @Override
    public ResponseEntity<Response> updateClubLogo(
            @PathVariable("clubId") Integer clubId,
            @RequestPart("file") MultipartFile logo
    ) throws IOException {
        return buildResponse.createResponse("club", clubService.updateClubLogo(clubId, logo), "Club logo updated", HttpStatus.OK);
    }
    @PutMapping("club/main-photo/{clubId}")
    @Override
    public ResponseEntity<Response> setClubMainPhoto(
            @PathVariable("clubId") Integer clubId,
            @RequestPart("file") MultipartFile photo
    ) throws IOException {
        return buildResponse.createResponse("club", clubService.setClubMainPhoto(clubId, photo), "Club photo set", HttpStatus.OK);
    }

    @PutMapping("club/file-upload/{clubId}")
    @Override
    public ResponseEntity<Response> uploadClubFiles(
            @PathVariable("clubId") Integer clubId,
            @RequestPart("file") MultipartFile[] files
    ) throws IOException {
        return buildResponse.createResponse("club", clubService.uploadClubFiles(clubId, files), "Club files uploaded", HttpStatus.OK);
    }
    @GetMapping("club/{id}")
    @Override
    public ResponseEntity<Response> getClubById(
            @PathVariable("id") Integer id
    ) {
        return buildResponse.createResponse("club", clubService.getClubById(id), "Club fetched", HttpStatus.OK);
    }
    @GetMapping("club")
    @Override
    public ResponseEntity<Response> getClubs(
            @RequestParam(value = "clubName", required = false) String clubName,
            @RequestParam(value = "divisionId", required = false) Integer divisionId,
            @RequestParam(value = "favorite", required = false) Boolean favorite,
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "status", required = false) String status
    ) {
        return buildResponse.createResponse("club", clubService.getClubs(clubName, divisionId, favorite, userId, status), "Clubs fetched", HttpStatus.OK);
    }

    @PostMapping("player")
    @Override
    public ResponseEntity<Response> addPlayer(
            @RequestPart("data") AddPlayerDto addPlayerDto,
            @RequestPart("file") MultipartFile mainPhoto
    ) throws IOException {
        return buildResponse.createResponse("player", clubService.addPlayer(addPlayerDto, mainPhoto), "Player added", HttpStatus.OK);
    }
    @GetMapping("player/{id}")
    @Override
    public ResponseEntity<Response> getPlayerById(@PathVariable("id") Integer playerId) {
        return buildResponse.createResponse("player", clubService.getPlayerById(playerId), "Player fetched", HttpStatus.OK);
    }

    @PutMapping("player/sign")
    @Override
    public ResponseEntity<Response> signPlayer(
            @RequestBody SignPlayerDto signPlayerDto
    ) {
        return buildResponse.createResponse("player", clubService.signPlayer(signPlayerDto), "Player signed", HttpStatus.OK);
    }
    @PutMapping("player/remove-from-club")
    @Override
    public ResponseEntity<Response> removePlayerFromTeam(
            @RequestBody UnsignPlayerDto unsignPlayerDto
    ) {
        return buildResponse.createResponse("player", clubService.removePlayerFromTeam(unsignPlayerDto), "Player removed from team", HttpStatus.OK);
    }
    @PutMapping("player")
    @Override
    public ResponseEntity<Response> updatePlayerDetails(
            @RequestBody UpdatePlayerDto updatePlayerDto
    ) {
        return buildResponse.createResponse("player", clubService.updatePlayerDetails(updatePlayerDto), "Player details updated", HttpStatus.OK);
    }
    @PutMapping("player/main-photo/{playerId}")
    @Override
    public ResponseEntity<Response> updatePlayerMainPhoto(
            @PathVariable("playerId") Integer playerId,
            @RequestPart("file") MultipartFile mainPhoto
    ) throws IOException {
        return buildResponse.createResponse("player", clubService.updatePlayerMainPhoto(playerId, mainPhoto), "Player photo updated", HttpStatus.OK);
    }
    @PutMapping("player/file-upload/{playerId}")
    @Override
    public ResponseEntity<Response> uploadPlayerFiles(
            @PathVariable("playerId") Integer playerId,
            @RequestPart("file") MultipartFile[] files
    ) throws IOException {
        return buildResponse.createResponse("player", clubService.uploadPlayerFiles(playerId, files), "Player files uploaded", HttpStatus.OK);
    }
    @GetMapping("player")
    @Override
    public ResponseEntity<Response> getPlayers() {
        return buildResponse.createResponse("player", clubService.getPlayers(), "Players fetched", HttpStatus.OK);
    }

    @PutMapping("club/bookmark")
    @Override
    public ResponseEntity<Response> bookmarkClub(@RequestBody BookmarkClubDto bookmarkClubDto) {
        try {
            return buildResponse.createResponse("bookmark club", clubService.bookmarkClub(bookmarkClubDto), "Club bookmarked by the user", HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse.createResponse("null", null, e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("club/favorite/{userId}")
    @Override
    public ResponseEntity<Response> getUserFavoriteClubs(@PathVariable("userId") Integer userId) {
        return buildResponse.createResponse("favorite clubs", clubService.getUserFavoriteClubs(userId), "User favorites fetched", HttpStatus.OK);
    }

    @PutMapping("club/status-update")
    @Override
    public ResponseEntity<Response> updateClubStatus(@RequestBody ClubStatusUpdateDto clubStatusUpdateDto) {
        return buildResponse.createResponse("club", clubService.updateClubStatus(clubStatusUpdateDto), "Club status updated", HttpStatus.OK);
    }

    @PutMapping("club/all/status-update")
    @Override
    public ResponseEntity<Response> makeAllClubsPending() {
        return buildResponse.createResponse("clubs", clubService.makeAllClubsPending(), "All clubs status changed", HttpStatus.OK);
    }
}
