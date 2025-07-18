package com.jabulani.ligiopen.dao.match;

import com.jabulani.ligiopen.model.match.MatchStatus;
import com.jabulani.ligiopen.model.match.entity.MatchCommentary;
import com.jabulani.ligiopen.model.match.entity.MatchFixture;
import com.jabulani.ligiopen.model.match.entity.MatchLocation;
import com.jabulani.ligiopen.model.match.entity.PostMatchAnalysis;
import com.jabulani.ligiopen.model.match.entity.events.MatchEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class MatchDaoImpl implements MatchDao{
    private final EntityManager entityManager;
    @Autowired
    public MatchDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public MatchLocation createMatchLocation(MatchLocation matchLocation) {
        entityManager.persist(matchLocation);
        return matchLocation;
    }

    @Override
    public MatchLocation updateMatchLocation(MatchLocation matchLocation) {
        entityManager.merge(matchLocation);
        return matchLocation;
    }

    @Override
    public MatchLocation getMatchLocationById(Integer id) {
        TypedQuery<MatchLocation> query = entityManager.createQuery("from MatchLocation where id = :id", MatchLocation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public MatchLocation getMatchLocationByName(String name) {
        TypedQuery<MatchLocation> query = entityManager.createQuery("from MatchLocation where venueName = :name", MatchLocation.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public List<MatchLocation> getMatchLocations(String venueName, String locationName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MatchLocation> cq = cb.createQuery(MatchLocation.class);
        Root<MatchLocation> root = cq.from(MatchLocation.class);

        List<Predicate> predicates = new ArrayList<>();

        // Case-insensitive LIKE on venueName
        if (venueName != null && !venueName.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("venueName")),
                            "%" + venueName.toLowerCase() + "%"
                    )
            );
        }

        // Case-insensitive LIKE across country, county, or town
        if (locationName != null && !locationName.isBlank()) {
            String pattern = "%" + locationName.toLowerCase() + "%";
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(root.get("country")), pattern),
                            cb.like(cb.lower(root.get("county")),  pattern),
                            cb.like(cb.lower(root.get("town")),    pattern)
                    )
            );
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getResultList();
    }



    @Override
    public MatchFixture createMatchFixture(MatchFixture matchFixture) {
        entityManager.persist(matchFixture);
        return matchFixture;
    }

    @Override
    public MatchFixture updateMatchFixture(MatchFixture matchFixture) {
        entityManager.merge(matchFixture);
        return matchFixture;
    }

    @Override
    public MatchFixture getMatchFixtureById(Integer id) {
        TypedQuery<MatchFixture> query = entityManager.createQuery("from MatchFixture where id = :id", MatchFixture.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<MatchFixture> getMatchFixtures(String status, List<Integer> clubIds, LocalDateTime matchDateTime) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MatchFixture> cq = cb.createQuery(MatchFixture.class);
        Root<MatchFixture> root = cq.from(MatchFixture.class);

        List<Predicate> predicates = new ArrayList<>();

        // 1) Filter by status (with special handling for "LIVE")
        if (status != null && !status.isEmpty()) {
            if ("LIVE".equalsIgnoreCase(status)) {
                List<MatchStatus> liveStatuses = Arrays.asList(
                        MatchStatus.FIRST_HALF,
                        MatchStatus.HALF_TIME,
                        MatchStatus.SECOND_HALF,
                        MatchStatus.EXTRA_TIME_FIRST_HALF,
                        MatchStatus.EXTRA_TIME_HALF_TIME,
                        MatchStatus.EXTRA_TIME_SECOND_HALF,
                        MatchStatus.PENALTY_SHOOTOUT
                );
                predicates.add(root.get("status").in(liveStatuses));
            } else {
                predicates.add(cb.equal(
                        root.get("status"),
                        MatchStatus.valueOf(status.toUpperCase())
                ));
            }
        }

        // 2) Filter by any of the given club IDs (home OR away)
        if (clubIds != null && !clubIds.isEmpty()) {
            Predicate homeIn = root.get("homeClub").get("id").in(clubIds);
            Predicate awayIn = root.get("awayClub").get("id").in(clubIds);
            predicates.add(cb.or(homeIn, awayIn));
        }

        // 3) Filter by matchDateTime (e.g. upcoming from this timestamp)
        if (matchDateTime != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    root.get("matchDateTime"),
                    matchDateTime
            ));
        }

        // Combine and execute
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.desc(root.get("matchDateTime")));
        TypedQuery<MatchFixture> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Boolean matchLocationExists(String name) {
        try {
            TypedQuery<MatchLocation> query = entityManager.createQuery("from MatchLocation where venueName = :name", MatchLocation.class);
            query.setParameter("name", name);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }


    @Override
    public MatchEvent createMatchEvent(MatchEvent matchEvent) {
        entityManager.persist(matchEvent);
        return matchEvent;
    }

    @Override
    public MatchEvent updateMatchEvent(MatchEvent matchEvent) {
        entityManager.merge(matchEvent);
        return matchEvent;
    }

    @Override
    public MatchEvent getMatchEventById(Integer id) {
        TypedQuery<MatchEvent> query = entityManager.createQuery("from MatchEvent where id = :id", MatchEvent.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<MatchEvent> getMatchEvents() {
        TypedQuery<MatchEvent> query = entityManager.createQuery("from MatchEvent", MatchEvent.class);
        return query.getResultList();
    }

    @Override
    public PostMatchAnalysis createPostMatchAnalysis(PostMatchAnalysis postMatchAnalysis) {
        entityManager.persist(postMatchAnalysis);
        return postMatchAnalysis;
    }

    @Override
    public PostMatchAnalysis updatePostMatchAnalysis(PostMatchAnalysis postMatchAnalysis) {
        return entityManager.merge(postMatchAnalysis);
    }

    @Override
    public PostMatchAnalysis getPostMatchAnalysisById(Integer id) {
        TypedQuery<PostMatchAnalysis> query = entityManager.createQuery("from PostMatchAnalysis where id = :id", PostMatchAnalysis.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<PostMatchAnalysis> getAllPostMatchAnalysis() {
        TypedQuery<PostMatchAnalysis> query = entityManager.createQuery("from PostMatchAnalysis", PostMatchAnalysis.class);
        return query.getResultList();
    }

    @Override
    public MatchCommentary createMatchCommentary(MatchCommentary matchCommentary) {
        entityManager.persist(matchCommentary);
        return matchCommentary;
    }

    @Override
    public MatchCommentary updateMatchCommentary(MatchCommentary matchCommentary) {
        entityManager.merge(matchCommentary);
        return matchCommentary;
    }

    @Override
    public MatchCommentary getMatchCommentaryById(Integer id) {
        TypedQuery<MatchCommentary> query = entityManager.createQuery("from MatchCommentary where id = :id", MatchCommentary.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<MatchCommentary> getAllMatchCommentaries() {
        TypedQuery<MatchCommentary> query = entityManager.createQuery("from MatchCommentary", MatchCommentary.class);
        return query.getResultList();
    }
}
