import React from "react";

function ResultCard({ result }) {
  return (
    <div className="card shadow-sm mb-5">
      <div className="card-body text-center">
        <h5 className="text-uppercase fw-bold">
          {new Date(result.date).toDateString()}
        </h5>
        <p className="text-muted">{result.competition}</p>
        <div className="d-flex justify-content-around align-items-center my-3">
          <div>
            <img
              src={result.teamA.logo}
              alt={result.teamA.name}
              className="img-fluid"
              style={{ height: 100 }}
            />
            <p className="mt-1 fw-bold">{result.teamA.name}</p>
          </div>
          <strong className="fs-3">
            {result.teamA.score} - {result.teamB.score}
          </strong>
          <div>
            <img
              src={result.teamB.logo}
              alt={result.teamB.name}
              className="img-fluid"
              style={{ height: 100 }}
            />
            <p className="mt-1 fw-bold">{result.teamB.name}</p>
          </div>
        </div>
        <p className="text-muted">{result.venue}</p>
      </div>
    </div>
  );
}

export default ResultCard;