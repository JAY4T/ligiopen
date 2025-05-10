import React from "react";
import loadingGif from "../assets/ZC9Y.gif"; // Adjust path based on your project structure
import loadingGif2 from "../assets/football-player-1--unscreen.gif";

function LoadingSpinner() {
  return (
    <div className="loading-container">
      <img src={loadingGif2} alt="Loading..." className="loading-gif" />
      <p>Loading...</p>
    </div>
  );
}

export default LoadingSpinner;