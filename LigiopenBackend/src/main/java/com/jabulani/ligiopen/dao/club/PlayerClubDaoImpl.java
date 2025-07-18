package com.jabulani.ligiopen.dao.club;

import com.jabulani.ligiopen.model.club.entity.PlayerClub;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerClubDaoImpl implements PlayerClubDao {
    private final EntityManager entityManager;
    @Autowired
    public PlayerClubDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PlayerClub addPlayerClub(PlayerClub playerClub) {
        entityManager.persist(playerClub);
        return playerClub;
    }

    @Override
    public PlayerClub updatePlayerClub(PlayerClub playerClub) {
        entityManager.merge(playerClub);
        return playerClub;
    }

    @Override
    public PlayerClub getPlayerClubById(Integer id) {
        TypedQuery<PlayerClub> query = entityManager.createQuery("from PlayerClub where id = :id", PlayerClub.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<PlayerClub> getPlayerClubs() {
        TypedQuery<PlayerClub> query = entityManager.createQuery("from PlayerClub", PlayerClub.class);
        return query.getResultList();
    }
}
