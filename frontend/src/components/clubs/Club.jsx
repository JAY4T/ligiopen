// src/clubs/club-pages/AFCLeopards.jsx
import React from "react";

function Club() {
  return (
    <div className="max-w-5xl mx-auto px-4 py-10">
      <div className="flex flex-col items-center text-center">
        <img
          src="/assets/clubs/afc-leopards-logo.png"
          alt="AFC Leopards logo"
          className="w-32 h-32 mb-4"
        />
        <h1 className="text-4xl font-bold mb-2">AFC Leopards</h1>
        <p className="text-gray-600 mb-4">The Pride of Kenya Football</p>
      </div>

      <div className="mt-6">
        <h2 className="text-2xl font-semibold mb-2">About the Club</h2>
        <p className="text-gray-700">
          AFC Leopards is one of the oldest and most successful football clubs in Kenya.
          With a rich history, passionate fan base, and a commitment to excellence,
          Leopards have been a pillar in Kenyan football for decades.
        </p>
      </div>

      <div className="mt-6 grid grid-cols-2 sm:grid-cols-3 gap-4">
        <div>
          <h3 className="text-lg font-semibold">Founded</h3>
          <p>1964</p>
        </div>
        <div>
          <h3 className="text-lg font-semibold">Location</h3>
          <p>Nairobi, Kenya</p>
        </div>
        <div>
          <h3 className="text-lg font-semibold">Stadium</h3>
          <p>Nyayo National Stadium</p>
        </div>
        <div>
          <h3 className="text-lg font-semibold">Colors</h3>
          <p>Blue, White</p>
        </div>
        <div>
          <h3 className="text-lg font-semibold">Website</h3>
          <p><a href="https://afcleopards.co.ke" className="text-blue-600" target="_blank" rel="noopener noreferrer">afcleopards.co.ke</a></p>
        </div>
      </div>
    </div>
  );
}

export default Club;