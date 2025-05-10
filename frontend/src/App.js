import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar";
import HomePage from "./components/HomePage";
import NewsList from "./components/NewsList";
import NewsDetails from "./components/NewsDetails";
import Footer from "./components/Footer";
import Players from "./components/Players";

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import "./styles/styles.css"; // Ensure this matches your actual file path
import "./styles/App.css";
import '@fortawesome/fontawesome-free/css/all.min.css';
import Clubs from "./components/Clubs";
import Fixtures from "./components/Fixtures";

const App = () => {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/players" element={<Players />} />
        <Route path="/clubs" element={<Clubs />} />
        <Route path="/fixtures" element={<Fixtures />} />
        <Route path="/news" element={<NewsList />} />
        <Route path="/news/:id" element={<NewsDetails />} />        

      </Routes>
      <Footer />
    </Router>
  );
};

export default App;
