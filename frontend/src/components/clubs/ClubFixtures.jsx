import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ClubNavbar from "./ClubNav";
import Tabs from "../ReusableTab";
import FixtureCard from "./ClubFixtureCard";
import ResultCard from "./ClubResultsCard";
import LeagueStandings from "./ClubTable";
import ClubFooter from "./ClubFooter";
import axios from "axios";
import LoadingSpinner from "../Loading";
import SearchBar from "../SearchBar";

function ClubFixtures() {
    const { "id-team": idTeam } = useParams();
    
    const [fixtures, setFixtures] = useState([]);
    const [results, setResults] = useState([]);
    // const [table, setTable] = useState([]);
    
    // const [filteredResults, setFilteredResults] = useState([]);
    // const [noMatchRes, setNoMatchRes] = useState(false);
    
    const [loadingFixtures, setLoadingFixtures] = useState(true);
    // const [loadingTable, setLoadingTable] = useState(true);
    
    const [errorFixtures, setErrorFixtures] = useState(null);
    // const [errorTable, setErrorTable] = useState(null);

    useEffect(() => {
        if (!idTeam) return;

        const fetchFixtures = async () => {
            setLoadingFixtures(true);
            setErrorFixtures(null);
            try {
                const upcoming = (await axios.get(`https://www.thesportsdb.com/api/v1/json/123/eventsnext.php?id=${idTeam}`)).data.events;
                const past = (await axios.get(`https://www.thesportsdb.com/api/v1/json/123/eventslast.php?id=${idTeam}`)).data.results;

                setFixtures(upcoming || []);
                setResults(past || []);
            } catch (err) {
                console.error("Fixtures error:", err);
                setErrorFixtures("Failed to load fixtures. Please try again later.");
            } finally {
                setLoadingFixtures(false);
            }
        };

        fetchFixtures();
    }, [idTeam]);


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
            {fixtures.map((fixture, index) => (
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
            {results.map((result, index) => (
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

        <ClubFooter />
        </>
    );
}

export default ClubFixtures;