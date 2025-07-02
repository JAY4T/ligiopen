import React, { useState, useEffect } from "react";
import API from "../services/api";

import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import LoadingSpinner from "./Loading";

function FeaturedPlayers() {
  const [featPlayers, setFeatPlayers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    API.get("players/")
      .then((res) => {
        const featured = res.data.filter((player) => player.is_featured);
        setFeatPlayers(featured);
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

  return (
    <div className="px-5 pb-5 animated-gradient">
      <h2 className="my-heading-white pt-5 pb-2">Featured Players</h2>

      {loading && <div className="alert alert-info text-center"><LoadingSpinner /></div>}
      {error && <div className="alert alert-danger text-center">{error}</div>}
      {!loading && !error && featPlayers.length === 0 && (
        <div className="alert alert-warning text-center">No Featured Players available.</div>
      )}

      <Swiper
        modules={[Autoplay, Pagination]}
        autoplay={{ delay: 3000, disableOnInteraction: false }}
        pagination={{ clickable: true }}
        spaceBetween={20}
        breakpoints={{
          320: { slidesPerView: 1 },
          768: { slidesPerView: 2 },
          1024: { slidesPerView: 3 },
        }}
      >
        {featPlayers.map((player) => (
          <SwiperSlide key={player.id}>
            <div className="card featured-player-card p-3">
              <div className="player-details">
                <div className="stats text-center">
                  <img
                    src="https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/logo/Gor_Mahia_FC_(logo).png?raw=true"
                    alt={`${player.club}`}
                  />
                  <div className="stat">
                    <span>Appearances</span>
                    <span className="value">{player.matches_played}</span>
                  </div>
                  <div className="stat">
                    <span>Goals</span>
                    <span className="value">{player.goals_scored}</span>
                  </div>
                  <div className="stat">
                    <span>Assists</span>
                    <span className="value">0</span>
                  </div>
                </div>
                <div className="player-image">
                  <img src={player.image} alt={`${player.name}`} />
                </div>
              </div>

              <div className="player-info text-center">
                <h3>{player.name}</h3>
                <p>{player.position}</p>
                <div className="country">
                  <img
                    src="https://github.com/JemoGithirwa4/Dimba-Itambe/blob/main/public/images/flag.png?raw=true"
                    alt={`${player.nationality} flag`}
                  />
                  <span>{player.nationality}</span>
                </div>
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}

export default FeaturedPlayers;