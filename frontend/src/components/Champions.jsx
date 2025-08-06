import React from "react";
import celeGif from "../assets/74US.gif";

function Champions() {
  return (
    <div
      className="d-flex flex-column justify-content-center align-items-center text-center p-4"
      style={{
        maxWidth: "500px",
        background: "white",
        borderRadius: "20px",
        boxShadow: "0 0 20px rgba(0,0,0,0.2)",
        animation: "fadeIn 1s ease",
      }}
    >
        <div>
            <img
                src={celeGif}
                alt="Congratulations to the champs..."
                className="cele-gif mb-3"
                style={{ maxWidth: "250px", height: "auto" }}
            />

            <img
                src={celeGif}
                alt="Congratulations to the champs..."
                className="cele-gif mb-3"
                style={{ maxWidth: "250px", height: "auto" }}
            />
        </div>
      <p className="fw-bold fs-5 text-success">
        Congratulations Kenya Police, KPL 2024-2025 champions! 🎉🏆
      </p>
    </div>
  );
}

export default Champions;