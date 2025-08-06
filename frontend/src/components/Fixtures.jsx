import React, { useState, useEffect } from "react";
import API from "../services/api";
import axios from "axios";
import LoadingSpinner from "./Loading";
import Tabs from "./ReusableTab";
import Navbar from "./Navbar";
import Footer from "./Footer";
import SearchBar from "./SearchBar";
import SPORTSDB from "../services/sportsdb";
import Champions from "./Champions";

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

  const [activeTab, setActiveTab] = useState("fixtures");
  const [showChampions, setShowChampions] = useState(false);

  useEffect(() => {
    const fetchFixtures = async () => {
      setLoadingFixtures(true);
      setErrorFixtures(null);
      try {
        const fixturesRes = await SPORTSDB.get("/eventsseason.php?id=4745&s=2024-2025");
        const allFixtures = fixturesRes.data.events;

        const upcoming = await SPORTSDB.get("/eventsnextleague.php?id=4745");
        const upcoming_2 = upcoming.data.events;

        const past = allFixtures.filter((fix) => fix.strStatus === "Match Finished");
        setFilteredResults(past);
        setFixtures(upcoming_2 || []);
        setResults(past || []);
      } catch (err) {
        console.error("Fixtures error:", err);
        setErrorFixtures("Failed to load fixtures. Please try again later.");
      } finally {
        setLoadingFixtures(false);
      }
    };

    const fetchTable = async () => {
      setLoadingTable(true);
      setErrorTable(null);
      try {
        const tableRes = await SPORTSDB.get("/lookuptable.php?l=4745");
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

  const handleTableTab = () => {
    setActiveTab("table");
    setShowChampions(true);
    setTimeout(() => setShowChampions(false), 5000);
  };

  const renderMatches = (matches) => (
    <div className="row">
      {matches.map((fixture) => (
        <div className="card shadow-sm mb-5 fixtures" key={fixture.id}>
          <div className="card-body text-center fix-card">
            <h5 className="text-uppercase fw-bold mt-3">
              {new Date(fixture.dateEvent).toDateString()}
            </h5>
            <div className="d-flex justify-content-around align-items-center my-3">
              <div className="fix-div">
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
                  {(() => {
                    const date = new Date(fixture.strTimestamp);
                    date.setHours(date.getHours() + 3);
                    return date.toLocaleTimeString("en-GB", {
                      hour: "2-digit",
                      minute: "2-digit",
                      hour12: false,
                    });
                  })()}{" "}
                  PM
                </strong>
                <p className="text-muted mt-3">{fixture.strVenue}</p>
              </div>
              <div className="fix-div">
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
  );

  const renderTable = () => (
    <div className="table-resp">
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
                <div className="d-flex align-items-center gap-1">
                  <img
                    src={team.strBadge}
                    alt={team.strTeam}
                    className="me-2 table-img"
                  />
                  <span>{team.strTeam}</span>
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
    </div>
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
          No fixtures available.
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
                  <div className="fix-div">
                    <img
                      src={fixture.strHomeTeamBadge}
                      alt={fixture.strHomeTeam}
                      className="img-fluid"
                      style={{ height: 100 }}
                    />
                    <p className="mt-1 fw-bold text-wrap">
                      {fixture.strHomeTeam}
                    </p>
                  </div>
                  <div>
                    <strong className="fix-time">
                      {fixture.intHomeScore} - {fixture.intAwayScore}
                    </strong>
                    <p className="text-muted mt-3">{fixture.strVenue}</p>
                  </div>
                  <div className="fix-div">
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

      {!loadingFixtures &&
        !errorFixtures &&
        results.length > 0 &&
        filteredResults.length === 0 &&
        noMatchRes && (
          <div className="alert alert-warning text-center">No match found.</div>
        )}
    </>
  );

  const tableWithOverlay = () => (
    <div style={{ position: "relative" }}>
      {showChampions && (
        <div
          style={{
            position: "absolute",
            inset: 0,
            zIndex: 10,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Champions />
        </div>
      )}

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
    </div>
  );

  const tabs = [
    {
      id: "fixtures",
      label: <span onClick={() => setActiveTab("fixtures")}>Fixtures</span>,
      content: fixturesContent,
    },
    {
      id: "results",
      label: <span onClick={() => setActiveTab("results")}>Results</span>,
      content: resultsContent,
    },
    {
      id: "table",
      label: <span onClick={handleTableTab}>Table</span>,
      content: tableWithOverlay(),
    },
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