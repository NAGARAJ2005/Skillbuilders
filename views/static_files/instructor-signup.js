document.getElementById("signupForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    // Get form and input values
    const form = event.target;
    const name = form.name.value.trim();
    const email = form.email.value.trim();
    const password = form.password.value.trim();
    const messageDiv = document.querySelector(".message"); // Message div for feedback

    // Clear previous messages
    messageDiv.innerHTML = "";

    // Validate inputs
    if (!name) {
        messageDiv.innerHTML = `<span style="color: red;">Name cannot be empty.</span>`;
        return;
    }
    if (!email) {
        messageDiv.innerHTML = `<span style="color: red;">Email cannot be empty.</span>`;
        return;
    }
    if (!password) {
        messageDiv.innerHTML = `<span style="color: red;">Password cannot be empty.</span>`;
        return;
    }

    // Prepare form data using FormData
    const formData = new URLSearchParams();
    formData.append("name", name);
    formData.append("email", email);
    formData.append("password", password);

    // Send AJAX request using Fetch API
    fetch("registerinstructor", { // Change the endpoint for instructor registration
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.result === "success") {
                messageDiv.innerHTML = `<span style="color: green;">${data.message}</span>`;
                document.querySelector("form").reset(); // Clear the form after successful signup
            } else {
                messageDiv.innerHTML = `<span style="color: red;">${data.message}</span>`;
            }
        })
        .catch(error => {
            console.error("Error:", error);
            messageDiv.innerHTML = `<span style="color: red;">An error occurred: ${error.message}</span>`;
        });
});
