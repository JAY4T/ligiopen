package com.jabulani.ligiopen.dao.league;

import com.jabulani.ligiopen.model.club.entity.League;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LeagueDaoImpl implements LeagueDao {
    private final EntityManager entityManager;

    @Autowired
    public LeagueDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public League createLeague(League league) {
        entityManager.persist(league);
        return league;
    }

    @Override
    public League updateLeague(League league) {
        entityManager.merge(league);
        return league;
    }

    @Override
    public League getLeagueById(Integer id) {
        TypedQuery<League> query = entityManager.createQuery("from League where id = :id", League.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public League getLeagueByName(String name) {
        TypedQuery<League> query = entityManager.createQuery("from League where name = :name", League.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public Boolean leagueExists(String name) {
        try {
            TypedQuery<League> query = entityManager.createQuery("from League where name = :name", League.class);
            query.setParameter("name", name);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public List<League> getLeagues() {
        TypedQuery<League>  query = entityManager.createQuery("from League", League.class);
        return query.getResultList();
    }
}
