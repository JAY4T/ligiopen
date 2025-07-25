import React, { useState, useEffect } from "react";
// import API from "../services/api";
import LoadingSpinner from "./Loading";
import SearchBar from "./SearchBar";
import { Link } from "react-router-dom";
import Navbar from "./Navbar";
import Footer from "./Footer";
import SPORTSDB from "../services/sportsdb";

function Clubs() {
  const [clubs, setClubs] = useState([]);
  const [filteredClubs, setFilteredClubs] = useState([]);
  const [noMatch, setNoMatch] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    
    const fetchClubs = async () => {
      setLoading(true);
      setError(null);
      try {
        const clubRes = await SPORTSDB.get("/search_all_teams.php?l=Kenyan Premier League");
        setClubs(clubRes.data.teams);
        setFilteredClubs(clubRes.data.teams || []);
      } catch (err) {
        console.error("Club error:", err);
        setError("Failed to load league Clubs. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchClubs();

  }, []);

  return (
    <>
      <Navbar />
      <div className="container py-5 web-section clubs">
        <h2 className="text-center mb-4">Clubs</h2>

        <div className="d-flex justify-content-center mb-4">
          <SearchBar
            data={clubs}
            filterKeys={["strTeam"]}
            onFilter={setFilteredClubs}
            setNoMatch={setNoMatch}
            placeholder="Search clubs..."
          />
        </div>

        {loading && (
          <div className="alert alert-info text-center">
            <LoadingSpinner />
          </div>
        )}

        {error && (
          <div className="alert alert-danger text-center">{error}</div>
        )}

        {!loading && !error && clubs.length === 0 && (
          <div className="alert alert-warning text-center">
            No clubs available.
          </div>
        )}

        {!loading && !error && clubs.length > 0 && filteredClubs.length === 0 && noMatch && (
          <div className="alert alert-warning text-center">
            No club found.
          </div>
        )}

        {!loading && !error && (
          <div className="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-6 g-4">
            {filteredClubs.map((club) => (
              <Link key={club.idTeam} to={`/${club.idTeam}/${club.strTeam}`} state={{ img: club.strBadge }} className="custom-link">
                <div className="col text-center">
                  <div className="p-2">
                    <img
                      src={club.strBadge}
                      alt={club.strTeam}
                      className="img-fluid mb-2"
                      style={{ maxHeight: "60px" }}
                    />
                    <p className="mb-0 fw-bold">{club.strTeam}</p>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
      <Footer />
    </>
  );
}

export default Clubs;