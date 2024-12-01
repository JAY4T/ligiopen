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
