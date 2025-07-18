package com.jabulani.ligiopen.dao.club;

import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.club.entity.League;

import java.util.List;

public interface ClubDao {
    Club addClub(Club club);
    Club updateClub(Club club);
    Club getClubById(Integer id);
    Club getClubByName(String name);
    Boolean clubExists(String name);
    List<Club> getClubs(String clubName, Integer divisionId, Boolean favorite, Integer userId, String status);
    List<Club> getUserFavoriteClubs(Integer userId);
    List<Club> getAllClubs();
}
