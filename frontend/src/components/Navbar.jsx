import React from "react";
import { NavLink } from "react-router-dom";
import logo from "../assets/ligiopen-icon.png";

const Navbar = () => {
  return (
    <nav className="navbar navbar-expand-lg bg-white sticky-top shadow-sm custom-navbar px-3">
      <NavLink className="navbar-brand" to="/">
        <img src={logo} alt="LigiOpen Logo" className="navbar-logo-img mx-2" />
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

      <div className="collapse navbar-collapse col-nav" id="navbarNav">
        <ul className="navbar-nav mx-auto">
          <li className="nav-item">
            <NavLink
              to="/"
              className={({ isActive }) => "nav-link" + (isActive ? " active" : "")}
              end
            >
              Home
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              to="/players"
              className={({ isActive }) => "nav-link" + (isActive ? " active" : "")}
            >
              Players
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              to="/clubs"
              className={({ isActive }) => "nav-link" + (isActive ? " active" : "")}
            >
              Clubs
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              to="/fixtures"
              className={({ isActive }) => "nav-link" + (isActive ? " active" : "")}
            >
              Fixtures
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              to="/news"
              className={({ isActive }) => "nav-link" + (isActive ? " active" : "")}
            >
              News
            </NavLink>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;