import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faInstagram, faFacebook } from "@fortawesome/free-brands-svg-icons";
import 'bootstrap/dist/css/bootstrap.min.css';

const ClubFooter = () => (
  <div className="footer shadow-sm">
    <div className="container">
      <footer className="d-flex flex-wrap justify-content-between align-items-center py-3 border-top">
        <div className="col-md-4 d-flex align-items-center">
          <a
            href="/"
            className="mb-3 me-2 mb-md-0 text-body-secondary text-decoration-none lh-1"
            aria-label="Club Logo"
          >
            <img
              src="https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/logo/Tusker_FC_logo-removebg-preview.png?raw=true"
              alt="Club Logo"
              className="navbar-logo-img mx-2 foot-img"
            />
          </a>
          <span className="mb-3 mb-md-0 text-body-secondary">© 2025 Tusker FC</span>
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

export default ClubFooter;