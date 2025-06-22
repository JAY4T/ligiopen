import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./components/HomePage";
import NewsList from "./components/NewsList";
import NewsDetails from "./components/NewsDetails";
import Players from "./components/Players";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import "./styles/styles.css"; // Ensure this matches your actual file path
import "./styles/App.css";
import '@fortawesome/fontawesome-free/css/all.min.css';
import Clubs from "./components/Clubs";
import Fixtures from "./components/Fixtures";
import Club from "./components/clubs/Club";
import ClubFixtures from "./components/clubs/ClubFixtures";
import ClubHistory from "./components/clubs/ClubHistory";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/players" element={<Players />} />
        <Route path="/clubs" element={<Clubs />} />
        <Route path="/fixtures" element={<Fixtures />} />
        <Route path="/news" element={<NewsList />} />
        <Route path="/news/:id" element={<NewsDetails />} />
        <Route path="/:club-name" element={<Club />} />        
        <Route path="/:id-team/:club-name" element={<Club />} />
        <Route path="/:id-team/:club-name/fixtures" element={<ClubFixtures />} />
        <Route path="/:club-name/history" element={<ClubHistory />} />

      </Routes>
    </Router>
  );
};

export default App;
