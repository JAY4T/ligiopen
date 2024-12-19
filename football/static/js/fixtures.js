document.addEventListener("DOMContentLoaded", function () {
  // Function to toggle visibility of fixtures and results sections
  const fixturesLink = document.getElementById("fixtures-link");
  const resultsLink = document.getElementById("results-link");
  const fixturesSection = document.getElementById("fixtures-section");
  const resultsSection = document.getElementById("results-section");

  //   fixturesLink.addEventListener("click", function () {
  //     console.log("Fixtures navigated");
  //     fixturesLink.classList.add("active");
  //     fixturesLink.classList.remove("active");
  //   });

  //   resultsLink.addEventListener("click", function () {
  //     console.log("Results navigated");
  //     fixturesLink.classList.remove("active");
  //     resultsLink.classList.add("active");
  //   });
  // Add active class to fixtures link

  function toggleSection(sectionToShow) {
    const sections = [fixturesSection, resultsSection];
    sections.forEach((section) => {
      section.style.display = "none";
    });
    sectionToShow.style.display = "block";
  }

  // Event listeners for toggling content sections
  fixturesLink.addEventListener("click", function (event) {
    event.preventDefault();
    console.log("Clicking Fixtires");
    resultsLink.classList.remove("active");
    fixturesLink.classList.add("active");
    toggleSection(fixturesSection);
  });

  resultsLink.addEventListener("click", function (event) {
    event.preventDefault();
    fixturesLink.classList.remove("active");
    resultsLink.classList.add("active");
    toggleSection(resultsSection);
  });

  // By default, show fixtures section
  toggleSection(fixturesSection);

  fixturesLink.click();
});
