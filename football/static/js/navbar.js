document.addEventListener("DOMContentLoaded", function () {
  const menuBars = document.getElementById("menu-bars");
  const navContent = document.getElementById("navbarContent");
  const accountButtons = document.querySelector(".navbar-account-buttons");

  menuBars.addEventListener("click", function (e) {
    e.stopPropagation();
    navContent.classList.toggle("active");
    accountButtons.classList.toggle("active");

    // Toggle menu icon animation
    this.classList.toggle("fa-times");
  });

  // Close menu when clicking outside
  document.addEventListener("click", function (event) {
    if (!event.target.closest(".navbar-main")) {
      navContent.classList.remove("active");
      accountButtons.classList.remove("active");
      menuBars.classList.remove("fa-times");
      menuBars.classList.add("fa-bars");
    }
  });
});
/**
 * Handles the toggling of the navigation menu and link activation behavior.
 *
 * This script controls the interaction between the menu icon and the navigation links. It:
 * - Toggles the visibility of the navbar by adding or removing the 'active' class.
 * - Adds the 'active' class to the selected link while removing it from others.
 * - Implements smooth scrolling to the top of the page when a link is clicked.
 * - Closes the menu once a link is selected.
 */
let menu = document.querySelector("#menu-bars");
let navbar = document.querySelector(".navbar");
var links = document.querySelectorAll(".navbar a");
var sections = document.querySelectorAll("section");

/**
 * Toggles the 'fa-times' class on the menu icon and the 'active' class on the navbar.
 * This function is triggered by clicking the menu icon to open or close the navbar.
 */
menu.onclick = () => {
  menu.classList.toggle("fa-times");
  navbar.classList.toggle("active");
};

/**
 * Adds click event listeners to each navbar link.
 * When a link is clicked:
 * - The 'active' class is removed from all links and applied to the clicked link.
 * - The page scrolls to the top smoothly.
 * - The navbar closes and the menu icon is reverted to the default state.
 */
links.forEach(function (link) {
  link.addEventListener("click", function (event) {
    //event.preventDefault();

    links.forEach(function (link) {
      link.classList.remove("active");
    });

    this.classList.add("active");

    // Scroll to the top of the page
    window.scrollTo({ top: 0, behavior: "smooth" });

    // Close the navbar after a link is clicked
    menu.classList.remove("fa-times");
    navbar.classList.remove("active");
  });
});
