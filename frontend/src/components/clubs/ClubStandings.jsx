import React from "react";

function ClubLeagueTable ({ table, currentClub }) {
  return (
    <div className="table-resp">
        <table className="table table-striped table-hover table-bordered shadow-sm">
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
                <tr
                    key={team.idTeam || index}
                    className={team.idTeam === currentClub ? "table-primary fw-bold" : ""}
                >
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
};

export default ClubLeagueTable;