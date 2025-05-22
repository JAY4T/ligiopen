import React from "react";
import ClubNavbar from "./ClubNav";
import ClubStory from "./ClubStory";

function ClubHistory() {
  return (
    <>
      <ClubNavbar />
      <div
        className="club-hist position-relative text-white club-hist"
        style={{
          backgroundImage:
            'url("https://images.ctfassets.net/i7hqck7o1wu1/2LW8xDaPSM7X9z2fSf4qtU/d9ff778d97cf49dc76c615f0aac9ee65/1969.jpg")',
          backgroundRepeat: "no-repeat",
          backgroundPosition: "center center",
          backgroundSize: "cover",
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
            backgroundColor: "rgba(0, 0, 0, 0.5)", // dark overlay
            zIndex: 1,
          }}
        ></div>

        <div className="container position-relative" style={{ zIndex: 2 }}>
          <span className="display-3 fw-bold hist-top pb-2">The History of Tusker FC</span>
        </div>
      </div>

      <div className="container d-flex align-items-center px-5">
        <div className="container px-5 m-5">
          <p className="hist-content fw-bold">Tusker FC, based in Nairobi, Kenya, was formed in 1969 and is sponsored by East African Breweries. Initially, the club was known as Kenya Breweries before adopting its current name in 1999. They are known for their success in the Kenyan Premier League, including winning the league in 2016. Tusker FC plays their home matches at Ruaraka Stadium. The team's history is intertwined with the East African Breweries, as the club's name and identity are closely linked to the Tusker Lager brand. 
          </p>
          <p className="hist-content">There have been upsets and difficult challenges along the way as well that only add to the intrigue, and by visiting The Story of Tusker below, each twist and turn is recalled chapter by chapter.</p>
        </div>
      </div>

      <div className="bg-whit py-5">
        <ClubStory />
      </div>
    </>
  );
}

export default ClubHistory;