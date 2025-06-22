import React from "react";

function FixtureCard({ fixture }) {
  return (
    <div className="card shadow-sm mb-5">
                <div className="card-body text-center">
                  <h5 className="text-uppercase fw-bold">
                    {new Date(fixture.strTimestamp).toDateString()}
                  </h5>
                  <p className="text-muted">{fixture.strLeague}</p>
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
                    <strong className="fix-time">{fixture.strTimeLocal} PM</strong>
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
                  <p className="text-muted">{fixture.strVenue}</p>
                </div>
              </div>
  );
}
export default FixtureCard;