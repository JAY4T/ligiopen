import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
//import NEWS_API from "../services/news";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleLeft } from "@fortawesome/free-solid-svg-icons";
import LoadingSpinner from "./Loading";
import Navbar from "./Navbar";
import Footer from "./Footer";
import MOCK_NEWS_RESPONSE from "../services/news";

const NewsDetails = () => {
  const { "id-news": id } = useParams();
  const [article, setArticle] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  // useEffect(() => {
  //   // NEWS_API.get(`/blogs/${id}?populate=*`)
  //   //   .then((res) => {
  //   //     setArticle(res.data.data);
  //   //     setError(null);
  //   //   })
  //   //   .catch(() => {
  //   //     setError("Failed to load article.");
  //   //   })
  //   //   .finally(() => setLoading(false));
  //   try {
  //       const article = MOCK_NEWS_RESPONSE.data.find((item) => item.documentId === Number(id));
  //       setArticle(article);
  //       setError(null);
  //     } catch (err) {
  //       console.error(err);
  //       setError("Failed to load news. Please try again later.");
  //     } finally {
  //       setLoading(false);
  //   }
  // }, [id]);

  useEffect(() => {
    const foundArticle = MOCK_NEWS_RESPONSE.data.find(
      (item) => item.documentId === id
    );

    if (foundArticle) {
      setArticle(foundArticle);
      setError(null);
    } else {
      setArticle(null);
      setError("Article not found.");
    }

    setLoading(false);
  }, [id]);

  const renderContent = (contentArray) => {
    return contentArray.map((block, index) => {
      if (block.type === "heading") {
        const HeadingTag = `h${block.level}`;
        return (
          <HeadingTag key={index} className="mt-4">
            {block.children.map((child, i) => child.text)}
          </HeadingTag>
        );
      } else if (block.type === "paragraph") {
        return (
          <p key={index} className="mt-3">
            {block.children.map((child, i) =>
              child.italic ? <em key={i}>{child.text}</em> : child.text
            )}
          </p>
        );
      } else if (block.type === "list") {
        const ListTag = block.format === "ordered" ? "ol" : "ul";
        return (
          <ListTag key={index} className="mt-3">
            {block.children.map((item, i) => (
              <li key={i}>
                {item.children.map((child, j) => child.text)}
              </li>
            ))}
          </ListTag>
        );
      }
      return null;
    });
  };

  return (
    <>
      <Navbar />
      {loading && <div className="alert alert-info"><LoadingSpinner /></div>}
      {error && <div className="container my-5 alert text-center alert-danger">{error}</div>}
      {!loading && !error && !article && (
        <div className="alert alert-warning text-center">
          Article Unavailable.
        </div>
      )}

      {!loading && !error && article && (
        <>
          {/* Header Section with Cover Image */}
          <div
            className="club-hist position-relative text-white club-hist"
            style={{
              backgroundImage: `url("${article.coverimage.url}")`,
              backgroundRepeat: "no-repeat",
              backgroundPosition: "center center",
              backgroundSize: "cover",
              display: "flex",
              alignItems: "center",
              height: "500px",
            }}
          >
            <div
              style={{
                position: "absolute",
                top: 0,
                left: 0,
                height: "100%",
                width: "100%",
                backgroundColor: "rgba(0, 0, 0, 0.5)",
                zIndex: 1,
              }}
            ></div>

            <div className="container position-relative" style={{ zIndex: 2 }}>
              <span className="display-3 fw-bold pb-2">
                {article.title}
              </span>
            </div>
          </div>

          {/* Main Content */}
          <main className="container my-5">
            <p className="text-muted mb-4 d-flex align-items-center gap-2">
              <span>
                Published on {new Date(article.publishedat).toDateString()} by {article.author}
              </span>
              {article.author_img && (
                <img
                  src={`${article.author_img.url}`}
                  alt={article.author}
                  className="rounded-circle"
                  style={{ width: "30px", height: "30px", objectFit: "cover" }}
                />
              )}
            </p>

            <div className="card-text news-content">
              {renderContent(article.content)}
            </div>
            <div className="mt-4">
              <Link to="/news" className="btn btn-outline-primary sec-btn">
                <FontAwesomeIcon icon={faAngleLeft} size="lg" /> Back to News
              </Link>
            </div>
          </main>
        </>
      )}

      <Footer />
    </>
  );
};

export default NewsDetails;