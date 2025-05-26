import React from "react";
import KitsCarousel from "./KitCarousel";

function ClubKit() {
    return(
        <>
        <div className="container">
            <div>
                <h2>Classic Kits</h2>
                <p className="p-mine">Take a look at our kits over the years.</p>
            </div>
            <KitsCarousel />
        </div>
        </>
    );
}

export default ClubKit;