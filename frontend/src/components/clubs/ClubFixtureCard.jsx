import React from "react";

function FixtureCard({ fixture }) {
  return (
    <div className="card shadow-sm mb-5">
                <div className="card-body text-center">
                  <h5 className="text-uppercase fw-bold">
                    {new Date(fixture.date).toDateString()}
                  </h5>
                  <p className="text-muted">{fixture.competition}</p>
                  <div className="d-flex justify-content-around align-items-center my-3">
                    <div>
                      <img
                        src={fixture.teamA.logo}
                        alt={fixture.teamA.name}
                        className="img-fluid"
                        style={{ height: 100 }}
                      />
                      <p className="mt-1 fw-bold">{fixture.teamA.name}</p>
                    </div>
                    <strong className="fix-time">{fixture.time}</strong>
                    <div>
                      <img
                        src={fixture.teamB.logo}
                        alt={fixture.teamB.name}
                        className="img-fluid"
                        style={{ height: 100 }}
                      />
                      <p className="mt-1 fw-bold">{fixture.teamB.name}</p>
                    </div>
                  </div>
                  <p className="text-muted">{fixture.broadcaster}</p>
                </div>
              </div>
  );
}
export default FixtureCard;