import React, { useState, useEffect } from "react";
import API from '../services/api';
import axios from "axios";
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
    const fetchUpcomingFixtures = async () => {
      setLoading(true);
      setError(null);
      try {
        const upcoming = (await axios.get("https://www.thesportsdb.com/api/v1/json/123/eventsnextleague.php?id=4745")).data.events;
        setMatches(upcoming || []);
      } catch (err) {
        console.error("Fixtures error:", err);
        setError("Failed to load fixtures. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchUpcomingFixtures();
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
            <div className="card shadow-sm card-up" style={{ minWidth: '300px' }}>
              <div className="card-body text-center">
                <h5 className="text-uppercase fw-bold">
                  {new Date(match.dateEvent).toDateString()}
                </h5>
                <div className="d-flex justify-content-around align-items-center my-3">
                  <div className="fix-up">
                    <img
                      src={match.strHomeTeamBadge}
                      alt={match.strHomeTeam}
                      className="img-fluid"
                      style={{ height: 50 }}
                    />
                    <p className="mt-1 fw-bold">{match.strHomeTeam}</p>
                  </div>
                  <strong className="fix-time">
                    {
                      (() => {
                        const date = new Date(match.strTimestamp);
                        date.setHours(date.getHours() + 3);
                        return date.toLocaleTimeString('en-GB', {
                          hour: '2-digit',
                          minute: '2-digit',
                          hour12: false
                        });
                      })()
                    } PM
                  </strong>
                  <div className="fix-up">
                    <img
                      src={match.strAwayTeamBadge}
                      alt={match.strAwayTeam}
                      className="img-fluid"
                      style={{ height: 50 }}
                    />
                    <p className="mt-1 fw-bold">{match.strAwayTeam}</p>
                  </div>
                </div>
                <p className="text-muted">{match.strVenue}</p>
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
