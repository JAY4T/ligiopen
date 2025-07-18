package com.jabulani.ligiopen.dao.club;

import com.jabulani.ligiopen.model.club.entity.PlayerClub;

import java.util.List;

public interface PlayerClubDao {
    PlayerClub addPlayerClub(PlayerClub playerClub);
    PlayerClub updatePlayerClub(PlayerClub playerClub);
    PlayerClub getPlayerClubById(Integer id);
    List<PlayerClub> getPlayerClubs();
}
