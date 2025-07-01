import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faInstagram, faFacebook } from "@fortawesome/free-brands-svg-icons";
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useEffect, useState } from "react";
import { useParams, NavLink } from "react-router-dom";
import SPORTSDB from "../../services/sportsdb";
import logo from "../../assets/inval_logo.png";

const ClubFooter = () => {
  const { "club-name": clubName, "id-team": idTeam } = useParams();
  const [badge, setBadge] = useState(null);

  useEffect(() => {
    SPORTSDB.get(`/searchteams.php?t=${clubName}`)
      .then((res) => {
        const club = res.data.teams?.[0];
        if (club?.strBadge) {
          setBadge(club.strBadge);
        } else {
          setBadge(null);
        }
      })
      .catch((err) => {
        console.error("Failed to load club badge", err);
        setBadge(null);
      });
  }, [clubName]);

  return (
    <div className="footer shadow-sm">
      <div className="container">
        <footer className="d-flex flex-wrap justify-content-between align-items-center py-3 border-top">
          <div className="col-md-4 d-flex align-items-center">
            <NavLink className="navbar-brand" to={`/${idTeam}/${clubName}`}>
              <img
                src={badge || logo}
                alt="Club Badge"
                className="navbar-logo-img mx-2 foot-img"
                onError={(e) => {
                  if (e.target.src !== logo) {
                    e.target.src = logo;
                  }
                }}
              />
            </NavLink>
            <span className="mb-3 mb-md-0 text-body-secondary">© 2025 {clubName}</span>
          </div>

          <ul className="nav col-md-4 justify-content-end list-unstyled d-flex">
            <li className="ms-3">
              <a className="text-dark" href="#" aria-label="Instagram">
                <FontAwesomeIcon icon={faInstagram} size="xl" />
              </a>
            </li>
            <li className="ms-3">
              <a className="text-dark" href="#" aria-label="Facebook">
                <FontAwesomeIcon icon={faFacebook} size="xl" />
              </a>
            </li>
          </ul>
        </footer>
      </div>
    </div>
  );
};

export default ClubFooter;