// ReusableTab.js
import React, { useState } from "react";

function ReusableTab({ tabs }) {
  const [activeTab, setActiveTab] = useState(tabs[0].id);

  return (
    <>
      <ul className="d-flex align-items-center justify-content-center gap-4 mb-4 custom-nav">
        {tabs.map((tab) => (
          <li className="custom-nav-item" key={tab.id}>
            <a
              href={`#${tab.id}`}
              className={`nav-link nav-link-2 ${activeTab === tab.id ? "active" : ""}`}
              onClick={(e) => {
                e.preventDefault();
                setActiveTab(tab.id);
              }}
            >
              {tab.label}
            </a>
          </li>
        ))}
      </ul>

      <div className="tab-content px-5 tab-small">
        {tabs.map((tab) => (
          <div
            key={tab.id}
            id={tab.id}
            className="tab-pane"
            style={{ display: activeTab === tab.id ? "block" : "none" }}
          >
            {tab.content}
          </div>
        ))}
      </div>
    </>
  );
}

export default ReusableTab;