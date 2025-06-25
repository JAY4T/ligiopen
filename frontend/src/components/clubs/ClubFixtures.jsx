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
import ClubLeagueTable from "./ClubStandings";

function ClubFixtures() {
    const { "id-team": idTeam } = useParams();
    
    const [fixtures, setFixtures] = useState([]);
    const [results, setResults] = useState([]);
    const [table, setTable] = useState([]);
    
    const [filteredResults, setFilteredResults] = useState([]);
    const [noMatchRes, setNoMatchRes] = useState(false);
    
    const [loadingFixtures, setLoadingFixtures] = useState(true);
    const [loadingTable, setLoadingTable] = useState(true);
    
    const [errorFixtures, setErrorFixtures] = useState(null);
    const [errorTable, setErrorTable] = useState(null);

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

        // Fetch league table
        const fetchTable = async () => {
            setLoadingTable(true);
            setErrorTable(null);
            try {
                const tableRes = await axios.get("https://www.thesportsdb.com/api/v1/json/123/lookuptable.php?l=4745");
                setTable(tableRes.data.table || []);
            } catch (err) {
                console.error("Table error:", err);
                setErrorTable("Failed to load league table. Please try again later.");
            } finally {
                setLoadingTable(false);
            }
        };
        
        fetchTable();
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
        <>
            {loadingFixtures && (
                <div className="alert alert-info text-center">
                <LoadingSpinner />
                </div>
            )}
            {errorFixtures && (
                <div className="alert alert-danger text-center">{errorFixtures}</div>
            )}
            {!loadingFixtures && !errorFixtures && fixtures.length === 0 && (
                <div className="min-vh-100">
                    <div className="alert alert-warning text-center">
                        No upcoming fixtures available.
                    </div>
                </div>
            )}
            {!loadingFixtures && !errorFixtures && fixtures.length != 0 && (
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
            )}
        </>
    );

    const resultsContent = (
        <>
            {loadingFixtures && (
                <div className="alert alert-info text-center">
                <LoadingSpinner />
                </div>
            )}
            {errorFixtures && (
                <div className="alert alert-danger text-center">{errorFixtures}</div>
            )}
            {!loadingFixtures && !errorFixtures && results.length === 0 && (
                <div className="alert alert-warning text-center">
                No past results available.
                </div>
            )}
            
            {!loadingFixtures && !errorFixtures && results.length != 0 && (
                <div className="row">
                <div className="d-flex justify-content-center mb-4">
                    <SearchBar
                    data={results}
                    filterKeys={["strHomeTeam", "strAwayTeam"]}
                    onFilter={setFilteredResults}
                    setNoMatch={setNoMatchRes}
                    placeholder="Search Results..."
                    />
                </div>
                {filteredResults.slice().reverse().map((fixture, index) => (
                    <ResultCard key={index} result={fixture} />

                ))}
                </div>
            )}

            {!loadingFixtures && !errorFixtures && results.length > 0 && filteredResults.length === 0 && noMatchRes && (
                <div className="alert alert-warning text-center">
                No match found.
                </div>
            )}
        </>
    );

    const tableContent = (
        <>
            {loadingTable && (
            <div className="alert alert-info text-center">
                <LoadingSpinner />
            </div>
            )}
            
            {errorTable && (
            <div className="alert alert-danger text-center">{errorTable}</div>
            )}
            
            {!loadingTable && !errorTable && table.length === 0 && (
            <div className="alert alert-warning text-center">
                No table data available.
            </div>
            )}
            
            {!loadingTable && !errorTable && table.length !== 0 && (
            <>
                <h2 className="fw-bold mb-4">League Standings</h2>
                <ClubLeagueTable table={table} currentClub={idTeam} />
            </>
            )}
        </>
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