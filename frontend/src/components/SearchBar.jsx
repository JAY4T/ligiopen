import React, { useState, useEffect, useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

function SearchBar({
  data,
  filterKeys = [],
  onFilter,
  setNoMatch,
  placeholder = "Search...",
}) {
  const [searchTerm, setSearchTerm] = useState("");
  const debounceRef = useRef(null);

  useEffect(() => {
    if (debounceRef.current) {
      clearTimeout(debounceRef.current);
    }

    debounceRef.current = setTimeout(() => {
      const value = searchTerm.toLowerCase().trim();

      const filtered = data.filter((item) =>
        filterKeys.some((key) =>
          item[key]?.toLowerCase().includes(value)
        )
      );

      onFilter(filtered);
      if (setNoMatch) {
        setNoMatch(filtered.length === 0 && value !== "");
      }
    }, 300); // debounce delay

    return () => clearTimeout(debounceRef.current);
  }, [searchTerm, data, filterKeys, onFilter, setNoMatch]);

  return (
    <div className="search-bar mb-4 d-flex align-items-center gap-2">
      <input
        type="text"
        className="form-control"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        placeholder={placeholder}
      />
      <button type="button" className="btn btn-outline-secondary">
        <FontAwesomeIcon icon={faMagnifyingGlass} size="lg" />
      </button>
    </div>
  );
}

export default SearchBar;