document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    const form = event.target;
    if (!form) return;

    const email = form.email.value.trim();
    const password = form.password.value.trim();
    const messageDiv = document.querySelector(".message");

    // Clear previous messages
    messageDiv.innerHTML = "";

    // Basic validation
    if (!email) {
        messageDiv.innerHTML = `<span style="color: red;">Email cannot be empty.</span>`;
        return;
    }
    if (!password) {
        messageDiv.innerHTML = `<span style="color: red;">Password cannot be empty.</span>`;
        return;
    }

    // Prepare form data
    const formData = new URLSearchParams();
    formData.append("email", email);
    formData.append("password", password);

    // Send POST request to server
    fetch("loginuser", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData.toString(), // Ensure proper URL-encoded format
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server responded with ${response.status}: ${response.statusText}`);
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {
            if (data.result === "success") {
                // Redirect user to index.html
                window.location.href = "user-index.html";
            } else {
                // Display error message from server
                messageDiv.innerHTML = `<span style="color: red;">${data.message}</span>`;
            }
        })
        .catch(error => {
            console.error("Error:", error);
            messageDiv.innerHTML = `<span style="color: red;">An error occurred: ${error.message}</span>`;
        });
});
