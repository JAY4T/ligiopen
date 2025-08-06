import React, { useState, useEffect } from "react";
import API from "../services/api";
import LoadingSpinner from "./Loading";
import Tabs from "./ReusableTab";
import SearchBar from "./SearchBar";
import Navbar from "./Navbar";
import Footer from "./Footer";

function Players() {
  const [players, setPlayers] = useState([]);
  const [filteredPlayers, setFilteredPlayers] = useState([]);
  const [noMatch, setNoMatch] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    API.get("playersprofile/")
      .then((res) => {
        setPlayers(res.data);
        setFilteredPlayers(res.data);
        setError(null);
      })
      .catch((err) => {
        console.error(err);
        setError("No featured players data available. Please come back later.");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const playersContent = (
    <>
      {loading && (
        <div className="alert alert-info text-center">
          <LoadingSpinner />
        </div>
      )}

      {error && (
        <div className="alert alert-danger text-center">{error}</div>
      )}

      {!loading && !error && players.length === 0 && (
        <div className="alert alert-warning text-center">
          No Players available.
        </div>
      )}

      {!loading && !error && filteredPlayers.length !== 0 && (
        <div className="row">
          <div className="top-search d-flex flex-column justify-content-center align-items-center">
            <h2 className="my-heading-whit pla">Players</h2>
            <SearchBar
              data={players}
              filterKeys={["name", "team"]}
              onFilter={setFilteredPlayers}
              setNoMatch={setNoMatch}
              placeholder="Search player or team..."
            />
          </div>
          {noMatch && (
            <div className="alert alert-warning text-center">
              No match found.
            </div>
          )}
          {filteredPlayers.map((player) => (
            <div className="col-lg-4 col-md-6 mb-4" key={player.id}>
              <div className="card featured-player-card p-3 h-100">
                <div className="player-details h-100">
                  <div className="stats d-flex justify-content-center align-items-center text-center">
                    <img
                      src="https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/logo/Tusker_FC_logo-removebg-preview.png?raw=true"
                      alt="Club logo"
                      style={{ width: "100px", height: "100px" }}
                    />
                  </div>
                  <div className="player-image text-center">
                    <img
                      src={player.image}
                      alt={player.name}
                      className="img-fluid rounded"
                      style={{ maxHeight: "200px" }}
                    />
                  </div>
                </div>

                <div className="player-info text-center mt-3">
                  <h3>{player.name}</h3>
                  <p>{player.position}</p>
                  <div className="country d-flex justify-content-center align-items-center gap-2">
                    <img
                      src="https://github.com/JemoGithirwa4/Dimba-Itambe/blob/main/public/images/flag.png?raw=true"
                      alt={`${player.nationality} flag`}
                      style={{ width: "20px" }}
                    />
                    <span>{player.nationality || "Kenya"}</span>
                    {/* <span>{player.club}</span> */}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );

  const statsContent = (
    <div className="text-center p-4 h-100">
      <div className="alert alert-warning text-center">
          No Player stats available.
        </div>
    </div>
  );

  const tabs = [
    { id: "players", label: "Players", content: playersContent },
    { id: "stats", label: "Stats", content: statsContent },
  ];

  return (
    <>
      <Navbar />
      <div className="pb-5 web-section">
        <Tabs tabs={tabs} />
      </div>
      <Footer />
    </>
  );
}

export default Players;