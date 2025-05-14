import React, { useState, useEffect } from "react";
import API from "../services/api";
import LoadingSpinner from "./Loading";
import SearchBar from "./SearchBar";
import { Link } from "react-router-dom";
import Navbar from "./Navbar";
import Footer from "./Footer";

function Clubs() {
  const [clubs, setClubs] = useState([]);
  const [filteredClubs, setFilteredClubs] = useState([]);
  const [noMatch, setNoMatch] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    API.get("clubs/")
      .then((res) => {
        setClubs(res.data);
        setFilteredClubs(res.data);
        setError(null);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load clubs. Please try again later.");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <>
      <Navbar />
      <div className="container py-5 web-section">
        <h2 className="text-center mb-4">Clubs</h2>

        <div className="d-flex justify-content-center mb-4">
          <SearchBar
            data={clubs}
            filterKeys={["name"]}
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

        {!loading && !error && filteredClubs.length === 0 && (
          <div className="alert alert-warning text-center">
            No clubs available.
          </div>
        )}

        {!loading && !error && filteredClubs.length === 0 && noMatch && (
          <div className="alert alert-warning text-center">
            No match found.
          </div>
        )}

        {!loading && !error && (
          <div className="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-6 g-4">
            {filteredClubs.map((club) => (
              <Link to={`/${club.name}`} className="custom-link">
                <div className="col text-center" key={club.id}>
                  <div className="p-2">
                    <img
                      src={club.logo}
                      alt={club.name}
                      className="img-fluid mb-2"
                      style={{ maxHeight: "60px" }}
                    />
                    <p className="mb-0">{club.name}</p>
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