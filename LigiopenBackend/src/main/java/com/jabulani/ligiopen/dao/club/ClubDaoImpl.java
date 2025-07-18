package com.jabulani.ligiopen.dao.club;

import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.club.entity.ClubStatus;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClubDaoImpl implements ClubDao{
    private final EntityManager entityManager;
    @Autowired
    public ClubDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Club addClub(Club club) {
        entityManager.persist(club);
        return club;
    }

    @Override
    public Club updateClub(Club club) {
        entityManager.merge(club);
        return club;
    }

    @Override
    public Club getClubById(Integer id) {
        TypedQuery<Club> query = entityManager.createQuery("from Club where id = :id", Club.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Club getClubByName(String name) {
        TypedQuery<Club> query = entityManager.createQuery("from Club where name = :name", Club.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public Boolean clubExists(String name) {
        try {
            TypedQuery<Club> query = entityManager.createQuery("from Club where name = :name", Club.class);
            query.setParameter("name", name);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }

    }


    @Override
    public List<Club> getClubs(String clubName, Integer divisionId, Boolean favorite, Integer userId, String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Club> cq = cb.createQuery(Club.class);
        Root<Club> club = cq.from(Club.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filter by club name (partial match, case-insensitive)
        if (clubName != null && !clubName.isEmpty()) {
            predicates.add(cb.like(cb.lower(club.get("name")), "%" + clubName.toLowerCase() + "%"));
        }

        // Filter by division/league ID
        if (divisionId != null) {
            predicates.add(cb.equal(club.get("league").get("id"), divisionId));
        }

        // Filter by favorite clubs
        if (favorite != null && favorite && userId != null) {
            Join<Club, UserAccount> userJoin = club.join("bookmarkedBy", JoinType.INNER);
            predicates.add(cb.equal(userJoin.get("id"), userId));
        }

        // Filter by club status
        if (status != null && !status.isEmpty()) {
            try {
                ClubStatus clubStatusEnum = ClubStatus.valueOf(status.toUpperCase());
                predicates.add(cb.equal(club.get("clubStatus"), clubStatusEnum));
            } catch (IllegalArgumentException e) {
                // Optional: log or handle invalid status string
                // For now, ignore the filter if the status is invalid
            }
        }

        // Apply all predicates
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        // Order by club name
        cq.orderBy(cb.asc(club.get("name")));

        return entityManager.createQuery(cq).getResultList();
    }


    @Override
    public List<Club> getUserFavoriteClubs(Integer userId) {
        TypedQuery<Club> query = entityManager.createQuery(
                "SELECT c FROM Club c JOIN c.bookmarkedBy u WHERE u.id = :userId",
                Club.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Club> getAllClubs() {
        TypedQuery<Club> query = entityManager.createQuery("from Club", Club.class);
        return query.getResultList();
    }

}
