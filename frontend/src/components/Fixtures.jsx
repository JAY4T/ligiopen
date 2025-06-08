import React, { useState, useEffect } from "react";
import API from "../services/api";
import axios from "axios";
import LoadingSpinner from "./Loading";
import Tabs from "./ReusableTab";
import Navbar from "./Navbar";
import Footer from "./Footer";
import SearchBar from "./SearchBar";

function Fixtures() {
  const [fixtures, setFixtures] = useState([]);
  const [results, setResults] = useState([]);
  const [table, setTable] = useState([]);

  const [filteredResults, setFilteredResults] = useState([]);
  const [noMatchRes, setNoMatchRes] = useState(false);

  const [loadingFixtures, setLoadingFixtures] = useState(true);
  const [loadingTable, setLoadingTable] = useState(true);

  const [errorFixtures, setErrorFixtures] = useState(null);
  const [errorTable, setErrorTable] = useState(null);

  useEffect(() => {
    // Fetch fixtures and results
    const fetchFixtures = async () => {
      setLoadingFixtures(true);
      setErrorFixtures(null);
      try {
        const fixturesRes = await API.get("https://www.thesportsdb.com/api/v1/json/123/eventsseason.php?id=4745&s=2024-2025");
        const allFixtures = fixturesRes.data.events;

        const upcoming = allFixtures.filter(fix => fix.strStatus === "Not Started");
        const past = allFixtures.filter(fix => fix.strStatus === "Match Finished");
        setFilteredResults(past);


        setFixtures(upcoming);
        setResults(past);
      } catch (err) {
        console.error("Fixtures error:", err);
        setErrorFixtures("Failed to load fixtures. Please try again later.");
      } finally {
        setLoadingFixtures(false);
      }
    };

    // Fetch league table
    const fetchTable = async () => {
      setLoadingTable(true);
      setErrorTable(null);
      try {
        const tableRes = await axios.get("https://www.thesportsdb.com/api/v1/json/123/lookuptable.php?l=4745");
        setTable(tableRes.data.table || []);
      } catch (err) {
        console.error("Table error:", err);
        setErrorTable("Failed to load league table. Please try again later.");
      } finally {
        setLoadingTable(false);
      }
    };

    fetchFixtures();
    fetchTable();
  }, []);

  // Render functions...

  const renderMatches = (matches) => (
    <div className="row">
      {matches.map((fixture) => (
        <div className="card shadow-sm mb-5 fixtures" key={fixture.id}>
          <div className="card-body text-center fix-card">
            <h5 className="text-uppercase fw-bold mt-2">
              {new Date(fixture.match_date).toDateString()}
            </h5>
            <div className="d-flex justify-content-around align-items-center my-3">
              <div>
                <img
                  src={fixture.home_team_logo}
                  alt={fixture.home_team}
                  className="img-fluid"
                  style={{ height: 100 }}
                />
                <p className="mt-1 fw-bold">{fixture.home_team}</p>
              </div>
              <div>
                <strong className="fix-time">{fixture.match_time}</strong>
                <p className="text-muted mt-3">{fixture.venue}</p>
              </div>
              <div>
                <img
                  src={fixture.away_team_logo}
                  alt={fixture.away_team}
                  className="img-fluid"
                  style={{ height: 100 }}
                />
                <p className="mt-1 fw-bold">{fixture.away_team}</p>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );

  const renderTable = () => (
    <table className="table table-striped table-responsive table-hover table-bordered shadow-sm">
      <thead className="table-dark text-center">
        <tr>
          <th>#</th>
          <th>Team</th>
          <th>Played</th>
          <th>W</th>
          <th>D</th>
          <th>L</th>
          <th>GF</th>
          <th>GA</th>
          <th>GD</th>
          <th>Points</th>
        </tr>
      </thead>
      <tbody className="text-center align-middle">
        {table.map((team, index) => (
          <tr key={team.idTeam || index}>
            <td>{team.intRank}</td>
            <td className="fw-bold">
              <div className="d-flex gap-1">
                <img
                  src={team.strBadge}
                  alt={team.strTeam}
                  className="me-2 table-img"
                />
                {team.strTeam}
              </div>
            </td>
            <td>{team.intPlayed}</td>
            <td>{team.intWin}</td>
            <td>{team.intDraw}</td>
            <td>{team.intLoss}</td>
            <td>{team.intGoalsFor}</td>
            <td>{team.intGoalsAgainst}</td>
            <td>{team.intGoalDifference}</td>
            <td>{team.intPoints}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );

  const fixturesContent = (
    <>
      {loadingFixtures && (
        <div className="alert alert-info text-center">
          <LoadingSpinner />
        </div>
      )}
      {errorFixtures && (
        <div className="alert alert-danger text-center">{errorFixtures}</div>
      )}
      {!loadingFixtures && !errorFixtures && fixtures.length === 0 && (
        <div className="alert alert-warning text-center">
          No upcoming fixtures available.
        </div>
      )}
      {!loadingFixtures && !errorFixtures && renderMatches(fixtures)}
    </>
  );

  const resultsContent = (
    <>
      {loadingFixtures && (
        <div className="alert alert-info text-center">
          <LoadingSpinner />
        </div>
      )}
      {errorFixtures && (
        <div className="alert alert-danger text-center">{errorFixtures}</div>
      )}
      {!loadingFixtures && !errorFixtures && results.length === 0 && (
        <div className="alert alert-warning text-center">
          No past results available.
        </div>
      )}

      {!loadingFixtures && !errorFixtures && results.length > 0 && filteredResults.length === 0 && noMatchRes && (
        <div className="alert alert-warning text-center">
          No match found.
        </div>
      )}
      
      {!loadingFixtures && !errorFixtures && (
        <div className="row">
          <div className="d-flex justify-content-center mb-4">
            <SearchBar
              data={results}
              filterKeys={["strHomeTeam", "strAwayTeam"]}
              onFilter={setFilteredResults}
              setNoMatch={setNoMatchRes}
              placeholder="Search Results..."
            />
          </div>
          {filteredResults.slice().reverse().map((fixture) => (
            <div className="card shadow-sm mb-5 fixtures" key={fixture.idEvent}>
              <div className="card-body text-center">
                <h5 className="text-uppercase fw-bold mt-2">
                  {new Date(fixture.strTimestamp).toDateString()}
                </h5>
                <div className="d-flex justify-content-around align-items-center my-3">
                  <div>
                    <img
                      src={fixture.strHomeTeamBadge}
                      alt={fixture.strHomeTeam}
                      className="img-fluid"
                      style={{ height: 100 }}
                    />
                    <p className="mt-1 fw-bold">{fixture.strHomeTeam}</p>
                  </div>
                  <div>
                    <strong className="fix-time">
                      {fixture.intHomeScore} - {fixture.intAwayScore}
                    </strong>
                    <p className="text-muted mt-3">{fixture.strVenue}</p>
                  </div>
                  <div>
                    <img
                      src={fixture.strAwayTeamBadge}
                      alt={fixture.strAwayTeam}
                      className="img-fluid"
                      style={{ height: 100 }}
                    />
                    <p className="mt-1 fw-bold">{fixture.strAwayTeam}</p>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );
  const tableContent = (
    <>
      {loadingTable && (
        <div className="alert alert-info text-center">
          <LoadingSpinner />
        </div>
      )}
      {errorTable && (
        <div className="alert alert-danger text-center">{errorTable}</div>
      )}
      {!loadingTable && !errorTable && table.length === 0 && (
        <div className="alert alert-warning text-center">
          No table data available.
        </div>
      )}
      {!loadingTable && !errorTable && renderTable()}
    </>
  );

  const tabs = [
    { id: "fixtures", label: "Fixtures", content: fixturesContent },
    { id: "results", label: "Results", content: resultsContent },
    { id: "table", label: "Table", content: tableContent },
  ];

  return (
    <>
      <Navbar />
      <div className="pb-5 web-section">
        <Tabs tabs={tabs} />
      </div>
      <Footer />
    </>
  );
}

export default Fixtures;