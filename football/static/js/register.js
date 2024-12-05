document.addEventListener("DOMContentLoaded", function () {
  const registerForm = document.querySelector("form");
  console.log("Form found:", registerForm);

  if (registerForm) {
    registerForm.addEventListener("submit", function (event) {
      console.log("Form submitted");
      event.preventDefault();

      let hasError = false;
      // Select username, role select, and both password fields
      const inputs = this.querySelectorAll(
        'input[type="text"], select, input[type="password"]'
      );
      console.log("Inputs found:", inputs.length);

      inputs.forEach((input) => {
        console.log("Input:", input.name, "Value:", input.value);
        if (!input.value.trim()) {
          console.log("Adding error border to:", input.name);
          input.classList.add("error-border");
          hasError = true;
        } else {
          input.classList.remove("error-border");
        }
      });

      // Additional validation for password matching
      const password = document.querySelector('input[name="password"]');
      const confirmPassword = document.querySelector(
        'input[name="confirm_password"]'
      );

      if (password.value !== confirmPassword.value) {
        password.classList.add("error-border");
        confirmPassword.classList.add("error-border");
        hasError = true;
        console.log("Passwords do not match");
      }

      if (!hasError) {
        this.submit();
      }
    });
  }
});
