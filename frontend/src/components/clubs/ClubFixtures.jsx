import React from "react";
import ClubNavbar from "./ClubNav";
import Tabs from "../ReusableTab";
import FixtureCard from "./ClubFixtureCard";
import ResultCard from "./ClubResultsCard";
import LeagueStandings from "./ClubTable";

function ClubFixtures() {
    const mockFixtures = [
    {
        date: "Sun 25 May 2025",
        competition: "Kenya Premier League",
        time: "16:00",
        teamA: {
        name: "Tusker FC",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Tusker_FC_logo-removebg-preview.png"
        },
        teamB: {
        name: "Gor Mahia",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Gor_Mahia_FC_logo.png"
        },
        broadcaster: "Sky Sports"
    },
    {
        date: "Sun 1 June 2025",
        competition: "COSAFA",
        time: "18:30",
        teamA: {
        name: "Gor Mahia",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Gor_Mahia_FC_logo.png"
        },
        teamB: {
        name: "Tusker",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Tusker_FC_logo-removebg-preview.png"
        },
        broadcaster: "BT Sport"
    }
    ];

    const mockResults = [
    {
        date: "2025-05-25T16:00:00Z",
        competition: "Kenya Premier League",
        venue: "Nyayo Stadium",
        teamA: {
        name: "Tusker FC",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Tusker_FC_logo-removebg-preview.png",
        score: 2,
        },
        teamB: {
        name: "Gor Mahia",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Gor_Mahia_FC_logo.png",
        score: 2,
        }
    },
    {
        date: "2025-05-18T15:00:00Z",
        competition: "Kenya Premier League",
        venue: "Moi International Sports Centre",
        teamA: {
        name: "Tusker FC",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Tusker_FC_logo-removebg-preview.png",
        score: 1,
        },
        teamB: {
        name: "AFC Leopards",
        logo: "https://upload.wikimedia.org/wikipedia/en/2/25/AFC_Leopards_logo.png", // Replace with your actual logo if needed
        score: 0,
        }
    }
    ];

    const standingsData = [
    {
        name: "Tusker FC",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Tusker_FC_logo-removebg-preview.png",
        played: 30,
        won: 20,
        drawn: 5,
        lost: 5,
        goalsFor: 45,
        goalsAgainst: 20,
        goalDifference: 25,
        points: 65,
    },
    {
        name: "Gor Mahia",
        logo: "https://8381-41-81-186-94.ngrok-free.app/media/club_logos/Gor_Mahia_FC_logo.png",
        played: 30,
        won: 19,
        drawn: 6,
        lost: 5,
        goalsFor: 42,
        goalsAgainst: 22,
        goalDifference: 20,
        points: 63,
    },
    {
        name: "AFC Leopards",
        logo: "https://upload.wikimedia.org/wikipedia/en/2/25/AFC_Leopards_logo.png",
        played: 30,
        won: 18,
        drawn: 7,
        lost: 5,
        goalsFor: 40,
        goalsAgainst: 25,
        goalDifference: 15,
        points: 61,
    },
    ];

    const fixturesContent = (
        <div className="container pb-4">
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2 className="fw-bold mb-0">Fixtures</h2>
                <select className="form-select w-auto">
                    <option>
                    Kenya Premier League
                    </option>
                    <option>
                    CECAFA
                    </option>
                </select>
            </div>
            {mockFixtures.map((fixture, index) => (
            <FixtureCard key={index} fixture={fixture} />
            ))}
        </div>
    );

    const resultsContent = (
        <div className="container pb-4">
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2 className="fw-bold mb-0">Results</h2>
                <select className="form-select w-auto">
                    <option>
                    Kenya Premier League
                    </option>
                    <option>
                    CECAFA
                    </option>
                </select>
            </div>
            {mockResults.map((result, index) => (
            <ResultCard key={index} result={result} />
            ))}
        </div>
    );

    const tableContent = (
        <div className="container pb-4">
            <LeagueStandings standings={standingsData} />
        </div>
    );

    const tabs = [
        { id: "Fixtures", label: "Fixtures", content: fixturesContent },
        { id: "Results", label: "Results", content: resultsContent },
        { id: "Table", label: "Table", content: tableContent },
    ];
    return(
        <>
        <ClubNavbar />
        
        <main>
            <Tabs tabs={tabs} />
        </main>
        </>
    );
}

export default ClubFixtures;