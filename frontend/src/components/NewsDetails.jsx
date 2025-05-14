import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import API from "../services/api";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleLeft } from "@fortawesome/free-solid-svg-icons";
import LoadingSpinner from "./Loading";
import Navbar from "./Navbar";
import Footer from "./Footer";

const NewsDetails = () => {
  const { id } = useParams();
  const [article, setArticle] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.get(`/news/${id}`)
      .then((res) => {
        setArticle(res.data);
        setError(null);
      })
      .catch(() => {
        setError("Failed to load article.");
      })
      .finally(() => setLoading(false));
  }, [id]);

  return (
    <>
      <Navbar />
      {loading && <div className="alert alert-info"><LoadingSpinner /></div>}
      {error && <div className="container my-5 alert alert-danger">{error}</div>}
      {!loading && !error && !article && (
        <div className="alert alert-warning text-center">
          Article Unavailable.
        </div>
      )}
      <main className="container my-5">
        {!loading && !error && article && (
          <div className="card shadow border-0">
            <img
              src={article.news_photo}
              alt={article.headline}
              className="card-img-top"
              style={{ height: "500px", objectFit: "cover" }}
            />
            <div className="card-body">
              <h2 className="card-title mb-3">{article.headline}</h2>
              <p className="text-muted mb-4">
                Published on {new Date(article.created_at).toDateString()}
              </p>
              <div className="card-text news-content">
                {article.news || "No content available."}
              </div>
              <div className="mt-4">
                <Link to="/news" className="btn btn-outline-primary sec-btn">
                  <FontAwesomeIcon icon={faAngleLeft} size="lg" /> Back to News
                </Link>
              </div>
            </div>
          </div>
        )}
      </main>
      <Footer />
    </>
  );
};

export default NewsDetails;