import React, { useState, useEffect } from "react";
import API from '../services/api';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import News from "./News";
import FeaturedPlayers from "./FeaturedPlayers";
import LoadingSpinner from "./Loading";
import Navbar from "./Navbar";
import Footer from "./Footer";

// Top Section (Upcoming Matches Card)
const MatchList = () => {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.get("fixtures/")
      .then((res) => {
        setMatches(res.data);
        setError(null);
      })
      .catch((err) => {
        setError("Failed to load fixtures. Please try again later.");
        console.error(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <div className="upcoming px-5 mb-5 pb-3">
      <h2 className="my-heading-white pt-3">Upcoming Fixtures</h2>

      {loading && (
        <div className="alert alert-info text-center"><LoadingSpinner /></div>
      )}

      {error && (
        <div className="alert alert-danger text-center">{error}</div>
      )}

      {!loading && !error && matches.length === 0 && (
        <div className="alert alert-warning text-center">
          No upcoming fixtures available.
        </div>
      )}

      {/* Start Swiper Carousel */}
      <Swiper
        modules={[Navigation, Autoplay]}
        spaceBetween={20}
        slidesPerView={1}
        navigation
        autoplay={{ delay: 3000, disableOnInteraction: false }}
        breakpoints={{
          640: { slidesPerView: 1 },
          768: { slidesPerView: 2 },
          1024: { slidesPerView: 3 },
        }}
        className="py-1"
      >
        {matches.map((match) => (
          <SwiperSlide key={match.id}>
            <div className="card shadow-sm" style={{ minWidth: '300px' }}>
              <div className="card-body text-center">
                <h5 className="text-uppercase fw-bold">
                  {new Date(match.match_date).toDateString()}
                </h5>
                <div className="d-flex justify-content-around align-items-center my-3">
                  <div>
                    <img
                      src={match.home_team_logo}
                      alt={match.home_team}
                      className="img-fluid"
                      style={{ height: 50 }}
                    />
                    <p className="mt-1">{match.home_team}</p>
                  </div>
                  <strong className="fix-time">{match.match_time}</strong>
                  <div>
                    <img
                      src={match.away_team_logo}
                      alt={match.away_team}
                      className="img-fluid"
                      style={{ height: 50 }}
                    />
                    <p className="mt-1">{match.away_team}</p>
                  </div>
                </div>
                <p className="text-muted">{match.venue}</p>
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
      {/* End Swiper Carousel */}
    </div>
  );
};

// HomePage Component
const HomePage = () => {

  return (
    <>
      <Navbar />
      <main>
        <MatchList />
        <News />
        <FeaturedPlayers />

        {/* <ClubNewsSection /> */}
      </main>
      <Footer />
    </>
  );
};

export default HomePage;
