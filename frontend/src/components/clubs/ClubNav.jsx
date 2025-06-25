import React from "react";
import { NavLink, useParams, useLocation } from "react-router-dom";
import logo from "../../assets/inval_logo.png";

const ClubNavbar = () => {
  const { "club-name": clubName } = useParams(); // get dynamic route
  const { "id-team": idTeam } = useParams();
  const location = useLocation();
  const img = location.state?.img;

  return (
    <nav className="navbar navbar-expand-lg bg-white sticky-top shadow-sm custom-navbar px-3">
      <NavLink className="navbar-brand" to={`/${idTeam}/${clubName}`}>
        <img
          src={img || logo}
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