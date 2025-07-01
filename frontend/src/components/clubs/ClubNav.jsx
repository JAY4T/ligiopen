import React, { useEffect, useState } from "react";
import { NavLink, useParams } from "react-router-dom";
import logo from "../../assets/inval_logo.png";
import SPORTSDB from "../../services/sportsdb";

const ClubNavbar = () => {
  const { "club-name": clubName, "id-team": idTeam } = useParams();
  const [badge, setBadge] = useState(null); 
  const [error, setError] = useState(null);

  useEffect(() => {
    SPORTSDB.get(`/searchteams.php?t=${clubName}`)
      .then((res) => {
        const club = res.data.teams?.[0];
        if (club && club.strBadge) {
          setBadge(club.strBadge); 
        } else {
          setBadge(null);
        }
        setError(null);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load club badge.");
      });
  }, [clubName]);

  return (
    <nav className="navbar navbar-expand-lg bg-white sticky-top shadow-sm custom-navbar px-5 club-nav">
      <NavLink className="navbar-brand" to={`/${idTeam}/${clubName}`}>
        <img
          src={badge || logo}
          alt="Club Badge"
          className="navbar-logo-img mx-2"
          onError={(e) => {
            if (e.target.src !== logo) {
              e.target.src = logo;
            }
          }}
        />
      </NavLink>

      <button
        className="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>

      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav mx-auto">
          <li className="nav-item">
            <NavLink
              to={`/${idTeam}/${clubName}`}
              state={{ idTeam }}
              className={({ isActive }) =>
                "nav-link" + (isActive ? " active" : "")
              }
              end
            >
              Team
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              to={`/${idTeam}/${clubName}/fixtures`}
              state={{ idTeam }}
              className={({ isActive }) =>
                "nav-link" + (isActive ? " active" : "")
              }
            >
              Fixtures
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              to={`/${clubName}/history`}
              state={{ idTeam }}
              className={({ isActive }) =>
                "nav-link" + (isActive ? " active" : "")
              }
            >
              History
            </NavLink>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default ClubNavbar;