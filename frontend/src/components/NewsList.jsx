import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import NEWS_API from "../services/news"; 
import LoadingSpinner from "./Loading";
import Navbar from "./Navbar";
import Footer from "./Footer";

const BASE_URL = process.env.REACT_APP_BASE_URL; // Replace with live API domain

const NewsList = () => {
  const [articles, setArticles] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    NEWS_API.get("/blogs?populate=*")
      .then((res) => {
        setArticles(res.data.data);
        setError(null);
      })
      .catch((err) => {
        setError("Failed to load news articles.");
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Navbar />
      <main className="container my-5 d-flex flex-column web-section">
        <h2 className="mb-4">All News</h2>

        {loading && <div className="alert alert-info"><LoadingSpinner /></div>}
        {error && <div className="alert alert-danger">{error}</div>}
        {!loading && !error && articles.length === 0 && (
          <div className="alert alert-warning text-center">
            No articles available.
          </div>
        )}

        <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          {articles.map((article) => (
            <div className="col news-all" key={article.id}>
              <div className="card shadow-sm h-100">
                
                {article.coverimage && (
                  <img
                    src={`${BASE_URL}${article.coverimage.formats?.medium?.url || article.coverimage.url}`}
                    alt={article.title}
                    className="card-img-top artic-im"
                  />
                )}

                <div className="card-body d-flex flex-column justify-content-between">
                  <p className="card-text">{article.title}</p>
                  <div className="mb-1 text-muted">{new Date(article.createdAt).toDateString()}</div>
                  <div className="d-flex justify-content-between align-items-center mt-auto">
                    <Link to={`/news/${article.documentId}/${article.slug}`} className="my-btn">Read News</Link>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </main>
      <Footer />
    </>
  );
};

export default NewsList;