package com.jabulani.ligiopen.dao.club;

import com.jabulani.ligiopen.model.club.entity.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerDaoImpl implements PlayerDao{
    private final EntityManager entityManager;
    @Autowired
    public PlayerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Player addPlayer(Player player) {
        entityManager.persist(player);
        return player;
    }

    @Override
    public Player updatePlayer(Player player) {
        entityManager.merge(player);
        return player;
    }

    @Override
    public Player getPlayerById(Integer id) {
        TypedQuery<Player> query = entityManager.createQuery("from Player where id = :id", Player.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Player> getPlayers() {
        TypedQuery<Player> query = entityManager.createQuery("from Player", Player.class);
        return query.getResultList();
    }
}
