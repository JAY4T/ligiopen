import React from "react";

function ResultCard({ result }) {
  return (
    <div className="card shadow-sm mb-5">
      <div className="card-body text-center">
        <h5 className="text-uppercase fw-bold">
          {new Date(result.strTimestamp).toDateString()}
        </h5>
        <p className="text-muted">{result.strLeague}</p>
        <div className="d-flex justify-content-around align-items-center my-3">
          <div>
            <img
              src={result.strHomeTeamBadge}
              alt={result.strHomeTeam}
              className="img-fluid"
              style={{ height: 100 }}
            />
            <p className="mt-1 fw-bold">{result.strHomeTeam}</p>
          </div>
          <strong className="fs-3 fix-time">
            {result.intHomeScore} - {result.intAwayScore}
          </strong>
          <div>
            <img
              src={result.strAwayTeamBadge}
              alt={result.strAwayTeam}
              className="img-fluid"
              style={{ height: 100 }}
            />
            <p className="mt-1 fw-bold">{result.strAwayTeam}</p>
          </div>
        </div>
        <p className="text-muted">{result.strVenue}</p>
      </div>
    </div>
  );
}

export default ResultCard;