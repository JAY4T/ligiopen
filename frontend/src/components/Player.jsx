import React from "react";

function Player({ name, position, nationality, flagImage, playerImage, stats }) {
    return (
        <div className="featured pb-5">
            <ul className="nav nav-tabs justify-content-center mb-4 my-head" id="matchTabs" role="tablist">
                <li className="nav-item" role="presentation">
                    <h1 className="gw">Featured players</h1>
                </li>
            </ul>
            <div className="container d-flex justify-content-center align-items-center my-5 gap-5">
                <div className="card featured-player-card">
                    <div className="player-details">
                        <div className="stats text-center">
                            <img src="" alt="Logo" />
                            <div className="stat">
                                <span className="value">{stats.appearances}</span>
                                <span>Appearances</span>
                            </div>
                            <div className="stat">
                                <span className="value">{stats.minutes}</span>
                                <span>Minutes</span>
                            </div>
                            <div className="stat">
                                <span className="value">{stats.goals}</span>
                                <span>Goals</span>
                            </div>
                            <div className="stat">
                                <span className="value">{stats.assists}</span>
                                <span>Assists</span>
                            </div>
                        </div>

                        <div className="player-image">
                            <img src={playerImage} alt="Player" />
                        </div>
                    </div>

                    <div className="player-info">
                        <h3>{name}</h3>
                        <p>{position}</p>
                        <div className="country">
                            <img src={flagImage} alt={`${nationality} Flag`} />
                            <span>{nationality}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Player;
