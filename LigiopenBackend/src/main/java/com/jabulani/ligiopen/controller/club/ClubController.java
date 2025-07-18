package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClubController {
    ResponseEntity<Response> addClub(AddClubDto addClubDto, MultipartFile logo) throws IOException;
    ResponseEntity<Response> addClubsFromAirtable() throws Exception;
    ResponseEntity<Response> updateClubDetails(UpdateClubDto updateClubDto);

    ResponseEntity<Response> updateClubLogo(Integer clubId, MultipartFile logo) throws IOException;

    ResponseEntity<Response> setClubMainPhoto(Integer clubId, MultipartFile photo) throws IOException;

    ResponseEntity<Response> uploadClubFiles(Integer clubId, MultipartFile[] files) throws IOException;
    ResponseEntity<Response> getClubById(Integer id);
    ResponseEntity<Response> getClubs(String clubName, Integer divisionId, Boolean favorite, Integer userId, String status);

    ResponseEntity<Response> addPlayer(AddPlayerDto addPlayerDto, MultipartFile mainPhoto) throws IOException;
    ResponseEntity<Response> getPlayerById(Integer playerId);

    ResponseEntity<Response> signPlayer(SignPlayerDto signPlayerDto);

    ResponseEntity<Response> removePlayerFromTeam(UnsignPlayerDto unsignPlayerDto);

    ResponseEntity<Response> updatePlayerDetails(UpdatePlayerDto updatePlayerDto);
    ResponseEntity<Response> updatePlayerMainPhoto(Integer playerId, MultipartFile mainPhoto) throws IOException;
    ResponseEntity<Response> uploadPlayerFiles(Integer playerId, MultipartFile[] files) throws IOException;
    ResponseEntity<Response> getPlayers();

    ResponseEntity<Response> bookmarkClub(BookmarkClubDto bookmarkClubDto);

    ResponseEntity<Response> getUserFavoriteClubs(Integer userId);
    ResponseEntity<Response> updateClubStatus(ClubStatusUpdateDto clubStatusUpdateDto);
    ResponseEntity<Response> makeAllClubsPending();
}
