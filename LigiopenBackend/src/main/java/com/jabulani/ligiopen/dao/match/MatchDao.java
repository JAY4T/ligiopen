package com.jabulani.ligiopen.dao.match;

import com.jabulani.ligiopen.model.match.entity.MatchCommentary;
import com.jabulani.ligiopen.model.match.entity.MatchFixture;
import com.jabulani.ligiopen.model.match.entity.MatchLocation;
import com.jabulani.ligiopen.model.match.entity.PostMatchAnalysis;
import com.jabulani.ligiopen.model.match.entity.events.MatchEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchDao {
    MatchLocation createMatchLocation(MatchLocation matchLocation);
    MatchLocation updateMatchLocation(MatchLocation matchLocation);
    MatchLocation getMatchLocationById(Integer id);
    MatchLocation getMatchLocationByName(String name);
    List<MatchLocation> getMatchLocations(String venueName, String locationName);
    MatchFixture createMatchFixture(MatchFixture matchFixture);
    MatchFixture updateMatchFixture(MatchFixture matchFixture);
    MatchFixture getMatchFixtureById(Integer id);
    List<MatchFixture> getMatchFixtures(String status, List<Integer> clubIds, LocalDateTime matchDateTime);

    Boolean matchLocationExists(String name);

    MatchEvent createMatchEvent(MatchEvent matchEvent);
    MatchEvent updateMatchEvent(MatchEvent matchEvent);

    MatchEvent getMatchEventById(Integer id);
    List<MatchEvent> getMatchEvents();

    PostMatchAnalysis createPostMatchAnalysis(PostMatchAnalysis postMatchAnalysis);

    PostMatchAnalysis updatePostMatchAnalysis(PostMatchAnalysis postMatchAnalysis);
    PostMatchAnalysis getPostMatchAnalysisById(Integer id);

    List<PostMatchAnalysis> getAllPostMatchAnalysis();
    MatchCommentary createMatchCommentary(MatchCommentary matchCommentary);
    MatchCommentary updateMatchCommentary(MatchCommentary matchCommentary);
    MatchCommentary getMatchCommentaryById(Integer id);
    List<MatchCommentary> getAllMatchCommentaries();

}
