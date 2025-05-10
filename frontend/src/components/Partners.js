import { useEffect, useState } from "react";
import axios from "axios";

const Partners = () => {
  const [partners, setPartners] = useState([]);

  useEffect(() => {
    axios.get("http://127.0.0.1:8000/api/partners/") // Replace with your actual API endpoint
      .then(response => {
        console.log("Partners Data:", response.data);
        setPartners(response.data);
      })
      .catch(error => console.log(error));
  }, []);

  return (
    <section className="partners">
      <h2>Our Partners</h2>
      <p>These are some of our partners and sponsors that make everything possible.</p>
      
      <div className="partners-list">
        {partners.map(partner => (
          <div key={partner.id} className="partner-item">
            <a href={partner.website} target="_blank" rel="noopener noreferrer">
              <img src={partner.logo} alt="Partner Logo" className="partner-logo" />
            </a>
          </div>
        ))}
      </div>
    </section>
  );
};

export default Partners;
