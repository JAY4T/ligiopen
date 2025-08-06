import React from "react";

function FixtureCard({ fixture }) {
  return (
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
              {
                (() => {
                  const date = new Date(fixture.strTimestamp);
                  date.setHours(date.getHours() + 3);
                  return date.toLocaleTimeString('en-GB', {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: false
                  });
                })()
              } PM
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
  );
}
export default FixtureCard;