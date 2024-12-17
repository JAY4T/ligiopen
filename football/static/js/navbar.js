document.addEventListener("DOMContentLoaded", function () {
  const menuBars = document.getElementById("menu-bars");
  const navbar = document.querySelector(".navbar-main-navigation");
  const accountButtons = document.querySelector(".navbar-account-buttons");

  // Toggle menu
  menuBars.addEventListener("click", () => {
    navbar.classList.toggle("navbar-main-navigation-active");
    menuBars.classList.toggle("fa-bars");
    menuBars.classList.toggle("fa-xmarks");
  });

  // Handle responsive state changes
  const mediaQuery = window.matchMedia("(min-width: 768px)");

  function handleScreenChange(e) {
    if (e.matches) {
      // If screen is wider than 768px
      navbar.classList.remove("navbar-main-navigation-active");
      navbar.style.display = "flex";
      accountButtons.style.display = "flex";
      // Reset to hamburger icon
      menuBars.classList.add("fa-bars");
      menuBars.classList.remove("fa-times");
    } else {
      // If screen is smaller than 768px
      navbar.style.display = "none";
      accountButtons.style.display = "none";
    }
  }

  // Add listener for screen size changes
  mediaQuery.addListener(handleScreenChange);

  // Initial check
  handleScreenChange(mediaQuery);
});
