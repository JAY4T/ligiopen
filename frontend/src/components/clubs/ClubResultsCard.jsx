import React from "react";

function ResultCard({ result }) {
  return (
    <div className="card shadow-sm mb-5 fixtures" key={result.idEvent}>
      <div className="card-body text-center">
        <h5 className="text-uppercase fw-bold mt-2">
          {new Date(result.strTimestamp).toDateString()}
        </h5>
        <div className="d-flex justify-content-around align-items-center my-3">
          <div className="fix-div">
            <img
              src={result.strHomeTeamBadge}
              alt={result.strHomeTeam}
              className="img-fluid"
              style={{ height: 100 }}
            />
            <p className="mt-1 fw-bold text-wrap">{result.strHomeTeam}</p>
          </div>
        <div>
          <strong className="fix-time">
            {result.intHomeScore} - {result.intAwayScore}
          </strong>
          <p className="text-muted mt-3">{result.strVenue}</p>
        </div>
        <div className="fix-div">
          <img
            src={result.strAwayTeamBadge}
            alt={result.strAwayTeam}
            className="img-fluid"
            style={{ height: 100 }}
          />
          <p className="mt-1 fw-bold">{result.strAwayTeam}</p>
        </div>
        </div>
    </div>
    </div>
  );
}

export default ResultCard;