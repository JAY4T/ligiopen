import React from 'react';
import PlayerCard from './PlayerCard';

const PositionSection = ({ title, players }) => (
  <div className="my-5">
    <h4 className="text-success fw-bold mb-4 border-bottom border-3 border-success d-inline-block">{title.toUpperCase()}</h4>
    <div className="row">
      {players.map((player, index) => (
        <PlayerCard key={index} {...player} />
      ))}
    </div>
  </div>
);

export default PositionSection;