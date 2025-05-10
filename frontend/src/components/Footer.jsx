// Footer.js
const Footer = () => (
  <div className="footer shadow-sm">
    <div className="container">
      <footer className="py-3">
        <ul className="nav justify-content-center border-bottom pb-3 mb-3">
          <li className="nav-item"><a href="/" className="nav-link px-2 text-body-secondary">Home</a></li>
          <li className="nav-item"><a href="/players" className="nav-link px-2 text-body-secondary">Players</a></li>
          <li className="nav-item"><a href="/clubs" className="nav-link px-2 text-body-secondary">Clubs</a></li>
          <li className="nav-item"><a href="/fixtures" className="nav-link px-2 text-body-secondary">Fixtures</a></li>
          <li className="nav-item"><a href="/news" className="nav-link px-2 text-body-secondary">News</a></li>
        </ul>
        <p className="text-center text-body-secondary">© {new Date().getFullYear()} Ligi Open</p>
      </footer>
    </div>
  </div>
);

export default Footer;