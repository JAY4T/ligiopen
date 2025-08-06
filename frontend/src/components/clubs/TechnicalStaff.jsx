import React from "react";

function TechnicalStaff({ name, position, image}) {
    return(
        <div className="col-md-4 text-center mt-4 mb-4 p-2 card">
            <div className="left-img">
                <img src={image} alt={name} className="img-fluid club-player-img" />
            </div>
            <div className='right-details'>
                <h5 className="mt-3 fw-bold">{name}</h5>
                <p className="text-success fw-bold mb-0">{position}</p>
            </div>
        </div>
    );
}
export default TechnicalStaff;