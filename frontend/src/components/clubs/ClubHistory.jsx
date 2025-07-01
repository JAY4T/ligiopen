import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ClubNavbar from "./ClubNav";
import ClubTrophy from "./ClubTrophy";
import ClubKit from "./ClubKit";
import ClubFooter from "./ClubFooter";
import SPORTSDB from "../../services/sportsdb";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendarDays, faLocationDot } from "@fortawesome/free-solid-svg-icons";
import stadi from "../../assets/stadi-removebg-preview.png";

function ClubHistory() {
  const { "club-name": clubName } = useParams();
  const [clubData, setClubData] = useState({});

  useEffect(() => {
    SPORTSDB.get(`/searchteams.php?t=${clubName}`)
      .then((res) => {
        const club = res.data.teams?.[0];
        if (club) {
          setClubData({
            badge: club.strBadge || null,
            description: club.strDescriptionEN || null,
            formedYear: club.intFormedYear || null,
            stadium: club.strStadium || null,
            location: club.strLocation || null,
            website: club.strWebsite || null,
          });
        }
      })
      .catch((err) => {
        console.error("Failed to load club data", err);
      });
  }, [clubName]);

  return (
    <>
      <ClubNavbar />

      <div
        className="club-hist position-relative text-white club-hist"
        style={{
          backgroundImage: `url("${clubData.badge}/medium")`,
          backgroundRepeat: "no-repeat",
          backgroundPosition: "center center",
          backgroundSize: "contain",
          display: "flex",
          alignItems: "center",
        }}
      >
        <div
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            height: "100%",
            width: "100%",
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            zIndex: 1,
          }}
        ></div>

        <div className="container position-relative" style={{ zIndex: 2 }}>
          <span className="display-3 fw-bold hist-top pb-2">
            The History of {clubName || "No data available."}
          </span>
        </div>
      </div>

      <div className="d-flex flex-row justify-content-around p-5 bg-white club-det">
        <div className="d-flex flex-column justify-content-center align-items-center">
          <FontAwesomeIcon icon={faCalendarDays} className="hist-icon p-4" />
          <span className="fw-bold">Est. Year</span>
          <p className="text-body-secondary">{clubData.formedYear}</p>
        </div>
        <div className="d-flex flex-column justify-content-center align-items-center">
          <img src={stadi} alt="Stadium Logo" className="hist-img" />
          <span className="fw-bold">Stadium</span>
          <p className="text-body-secondary">{clubData.stadium}</p>
        </div>
        <div className="d-flex flex-column justify-content-center align-items-center">
          <FontAwesomeIcon icon={faLocationDot} className="hist-icon p-4" />
          <span className="fw-bold">Location</span>
          <p className="text-body-secondary">{clubData.location}</p>
        </div>
      </div>

      <div className="container d-flex align-items-center hist-text">
        <div className="m-5 in-hist">
          <p className="hist-content fw-bold">
            {clubData.description ||
              "No history data available."}
          </p>
          <p className="hist-content">
            There have been upsets and difficult challenges along the way that only add to the intrigue, and by visiting The Story of{" "}
            {clubName} below, each twist and turn is recalled chapter by chapter.
          </p>
        </div>
      </div>

      {/* <div className="bg-whit py-5">
        <ClubTrophy />
      </div>

      <div className="py-5">
        <ClubKit />
      </div> */}

      <ClubFooter />
    </>
  );
}

export default ClubHistory;