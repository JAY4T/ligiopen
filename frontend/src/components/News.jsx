import React, { useEffect, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleRight } from "@fortawesome/free-solid-svg-icons";
import { Link } from 'react-router-dom';
import LoadingSpinner from "./Loading";
import MOCK_NEWS_RESPONSE from "../services/news";

function News() {
  const [newsItems, setNewsItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchNews = async () => {
      try {
        const response = MOCK_NEWS_RESPONSE;
        setNewsItems(response.data);
        setError(null);
      } catch (err) {
        console.error(err);
        setError("Failed to load news. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchNews();
  }, []);

  const featuredNews = newsItems[0];
  const additionalNews = newsItems.slice(1, 3);

  // Helper to extract first paragraph text
  const getSummary = (content) => {
    const paragraph = content.find((block) => block.type === "paragraph");
    return paragraph ? paragraph.children[0]?.text : "";
  };

  return (
    <main className="px-5 news">
      <div className="d-flex justify-content-between align-items-center head-cust mb-3">
        <h2 className="my-heading-black mb-0">Latest News</h2>
        <Link to="/news" className="text-decoration-underline d-flex justify-content-between align-items-center arrow-btn">
          More News <FontAwesomeIcon icon={faAngleRight} size="lg" />
        </Link>
      </div>

      {loading && <div className="alert alert-info text-center"><LoadingSpinner /></div>}
      {error && <div className="alert alert-danger text-center">{error}</div>}
      {!loading && !error && newsItems.length === 0 && (
        <div className="alert alert-warning text-center">No news available.</div>
      )}

      {/* Featured News */}
      {!loading && !error && featuredNews && (
        <div
          className="p-4 p-md-5 mb-4 text-white rounded"
          style={{
            backgroundImage: `url(${featuredNews.coverimage.url})`,
            backgroundRepeat: "no-repeat",
            backgroundPosition: "center center",
            backgroundSize: "cover",
            position: "relative",
          }}
        >
          <svg className="bd-placeholder-img svg" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" preserveAspectRatio="xMidYMid slice" focusable="false">
            <rect width="100%" height="100%" fill="var(--bs-secondary-color)" />
          </svg>

          <div className="col-md-6 px-0 news-top">
            <h1 className="display-4 fst-italic fw-bold">{featuredNews.title}</h1>
            <p className="lead my-3">
              {getSummary(featuredNews.content).slice(0, 120)}...
            </p>
            <p className="lead mb-0">
              <Link to={`/news/${featuredNews.documentId}/${featuredNews.slug}`} className="fw-bold arrow-btn text-white-mine">
                Continue reading...
              </Link>
            </p>
          </div>
        </div>
      )}

      {/* Additional News Cards */}
      <div className="row mb-2">
        {additionalNews.map((item) => (
          <div className="col-md-6" key={item.id}>
            <div className="row g-0 border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
              <div className="col p-4 d-flex flex-column position-static">
                <strong className="d-inline-block mb-2 text-primary">News</strong>
                <h3 className="mb-0">{item.title}</h3>
                <div className="mb-1 text-muted">
                  {new Date(item.publishedAt).toDateString()}
                </div>
                <p className="card-text mb-auto">
                  {getSummary(item.content).slice(0, 100)}...
                </p>
                <Link to={`/news/${featuredNews.documentId}/${featuredNews.slug}`} className="fw-bold arrow-btn text-white-mine">
                  Continue reading...
                </Link>
              </div>
              <div className="col-auto d-none d-lg-block feat-article-img">
                <img
                  src={`${item.coverimage.url}`}
                  alt={item.title}
                  width="200"
                  height="100%"
                  style={{ objectFit: "cover" }}
                />
              </div>
            </div>
          </div>
        ))}
      </div>
    </main>
  );
}

export default News;