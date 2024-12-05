document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.querySelector("form");
  console.log("Form found:", loginForm);

  if (loginForm) {
    loginForm.addEventListener("submit", function (event) {
      console.log("Form submitted");
      event.preventDefault();

      let hasError = false; // Add this flag
      const inputs = this.querySelectorAll(
        "input[type='email'],input[type='password'"
      );
      console.log("Required inputs found:", inputs.length);

      inputs.forEach((input) => {
        console.log("Input:", input.name, "Value:", input.value);
        if (!input.value.trim()) {
          // Check if value is empty after trimming whitespace
          console.log("Adding error border to:", input.name);
          input.classList.add("error-border");
          hasError = true;
        } else {
          input.classList.remove("error-border");
        }
      });

      // Only submit if no errors
      if (!hasError) {
        this.submit();
      }
    });
  }
});
