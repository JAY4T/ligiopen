import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleRight } from "@fortawesome/free-solid-svg-icons";

function ClubStory() {
  return (
    <div className="container my-5">
      <div className="row align-items-center">
        {/* Text Column */}
        <div className="col-md-6 mb-4 mb-md-0">
          <h2 className="fw-bold mb-4">The Story of Tusker</h2>
          <p className="text-muted hist-content" style={{ lineHeight: "1.8" }}>
            Across nine chapters, the story of the Brewers (or the Founders as the team was originally nicknamed) is
            revealed in words, images and videos – from the dreams of the founders realised in the best stadium
            in the land, to drawing big crowds with big stars, to modernising the club and winning trophies
            before a financial collapse and memorable recovery, and then the upward path to the current era.
          </p>
          <a href="/history" className="fw-bold text-primary text-decoration-none">
            Read More <FontAwesomeIcon icon={faAngleRight} size="lg" />
          </a>
        </div>

        {/* Image Column */}
        <div className="col-md-6">
          <img
            src="https://images.ctfassets.net/i7hqck7o1wu1/2LW8xDaPSM7X9z2fSf4qtU/d9ff778d97cf49dc76c615f0aac9ee65/1969.jpg"
            alt="Tusker celebration"
            className="img-fluid rounded shadow"
          />
        </div>
      </div>
    </div>
  );
}

export default ClubStory;