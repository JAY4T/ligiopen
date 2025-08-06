import React from 'react';

const PlayerCard = ({ number, name, position, image }) => (
  <div className="col-md-4 text-center mb-4 p-2 card">
    <div className="position-relative">
      <img src={image} alt={name} className="img-fluid club-player-img" />
      <h1 className="position-absolute end-0 translate-middle-y text-secondary opacity-25 display-1 me-3 mt-2" style={{ top: '10%' }}>
        {number}
      </h1>
    </div>
    <div className='h-auto'>
        <h5 className="mt-3 fw-bold">{name}</h5>
        <p className="text-success fw-bold mb-0">{number} <span className="text-muted">|</span> <span className="text-muted text-uppercase">{position}</span></p>
    </div>
  </div>
);

export default PlayerCard;