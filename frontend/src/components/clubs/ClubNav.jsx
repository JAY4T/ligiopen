import React from "react";
import { NavLink, useParams } from "react-router-dom";

const ClubNavbar = () => {
  const { "club-name": clubName } = useParams(); // get dynamic route

  return (
    <nav className="navbar navbar-expand-lg bg-white sticky-top shadow-sm custom-navbar px-3">
      <NavLink className="navbar-brand" to={`/${clubName}`}>
        <img
          src="https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/logo/Tusker_FC_logo-removebg-preview.png?raw=true"
          alt="Club Logo"
          className="navbar-logo-img mx-2"
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
              to={`/${clubName}`}
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
              to={`/${clubName}/fixtures`}
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