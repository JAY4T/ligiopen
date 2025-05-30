import React, { useState, useEffect } from "react";
import API from "../services/api";
import LoadingSpinner from "./Loading";
import Tabs from "./ReusableTab";
import Navbar from "./Navbar";
import Footer from "./Footer";

function Fixtures() {
  const [fixtures, setFixtures] = useState([]);
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    API.get("fixtures/")
      .then((res) => {
        const allFixtures = res.data;
        const today = new Date();
        const upcoming = allFixtures.filter(fix => fix.is_completed === false);
        const past = allFixtures.filter(fix => new Date(fix.match_date) < today && fix.is_completed === true);

        setFixtures(upcoming);
        setResults(past);
        setError(null);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load fixtures. Please try again later.");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

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

  const fixturesContent = (
    <>
      {loading && (
        <div className="alert alert-info text-center">
          <LoadingSpinner />
        </div>
      )}
      {error && (
        <div className="alert alert-danger text-center">{error}</div>
      )}
      {!loading && !error && fixtures.length === 0 && (
        <div className="alert alert-warning text-center">
          No upcoming fixtures available.
        </div>
      )}
      {!loading && !error && renderMatches(fixtures)}
    </>
  );

  const resultsContent = (
    <>
      {loading && (
        <div className="alert alert-info text-center">
          <LoadingSpinner />
        </div>
      )}
      {error && (
        <div className="alert alert-danger text-center">{error}</div>
      )}
      {!loading && !error && results.length === 0 && (
        <div className="alert alert-warning text-center">
          No past results available.
        </div>
      )}
      {!loading && !error && (
        <div className="row">
          {results.map((fixture) => (
            <div className="card shadow-sm mb-5 fixtures" key={fixture.id}>
              <div className="card-body text-center">
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
                    <strong className="fix-time">{fixture.home_score} - {fixture.away_score}</strong>
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
      )}
    </>
  );

  const tableContent = (
    <div className="text-center p-4">
      <h4>Table</h4>
      <p>No table data available.</p>
    </div>
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