import React, { useState, useEffect } from "react";
import API from "../services/api";
import LoadingSpinner from "./Loading";

function Fixtures() {
  const [fixtures, setFixtures] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    API.get("fixtures/")
      .then((res) => {
        setFixtures(res.data);
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

  return (
    <div className="container py-5 fix-hol web-section">
      <h2 className="row fix-t">Fixtures</h2>

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
          No fixtures available.
        </div>
      )}

      {!loading && !error && (
        <div className="row">
          {fixtures.map((fixture) => (
            <div className="card shadow-sm mb-5">
              <div className="card-body text-center">
                <h5 className="text-uppercase fw-bold">
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
                    <p className="mt-1">{fixture.home_team}</p>
                  </div>
                  <strong className="fix-time">{fixture.match_time}</strong>
                  <div>
                    <img
                      src={fixture.away_team_logo}
                      alt={fixture.away_team}
                      className="img-fluid"
                      style={{ height: 100 }}
                    />
                    <p className="mt-1">{fixture.away_team}</p>
                  </div>
                </div>
                <p className="text-muted">{fixture.venue}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Fixtures;