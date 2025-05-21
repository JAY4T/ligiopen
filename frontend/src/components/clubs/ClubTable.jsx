import React from "react";

function LeagueStandings({ standings }) {
  return (
    <>
    <div className="d-flex justify-content-between align-items-center mb-3">
        <h4 className="fw-bold mb-0">League Standings</h4>
        <select className="form-select w-auto">
            <option>
              Kenya Premier League
            </option>
            <option>
              CECAFA
            </option>
        </select>
    </div>
    <div className="table-responsive">
      <table className="table table-striped table-bordered shadow-sm">
        <thead className="table-dark text-center">
          <tr>
            <th>#</th>
            <th>Team</th>
            <th>MP</th>
            <th>W</th>
            <th>D</th>
            <th>L</th>
            <th>GF</th>
            <th>GA</th>
            <th>GD</th>
            <th>Pts</th>
          </tr>
        </thead>
        <tbody className="text-center align-middle">
          {standings.map((team, index) => (
            <tr key={team.name}>
              <td>{index + 1}</td>
              <td className="text-start fw-bold">
                <img
                  src={team.logo}
                  alt={team.name}
                  className="me-2 table-img"
                />
                {team.name}
              </td>
              <td>{team.played}</td>
              <td>{team.won}</td>
              <td>{team.drawn}</td>
              <td>{team.lost}</td>
              <td>{team.goalsFor}</td>
              <td>{team.goalsAgainst}</td>
              <td>{team.goalDifference}</td>
              <td>{team.points}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </>
  );
}

export default LeagueStandings;