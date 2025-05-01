// Wait for the document to be ready
document.addEventListener('DOMContentLoaded', function() {
    // Get the form and message div
    const loginForm = document.getElementById('loginForm');
    const messageDiv = document.querySelector('.message');

    // Listen for form submission
    loginForm.addEventListener('submit', function(event) {
        // Prevent the form from submitting the usual way
        event.preventDefault();

        // Get the form data
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Create the payload (JSON object)
        const payload = {
            email: email,
            password: password
        };

        // Send the data using the fetch API
        fetch('loginadmin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload)  // Convert the payload to a JSON string
        })
        .then(response => response.json())  // Parse the response as JSON
        .then(data => {
            // Handle the response based on the 'result'
            if (data.result === 'success') {
                // Display success message
                messageDiv.textContent = 'Login successful';
                messageDiv.style.color = 'green';
								window.location.href = "admin-handle.html";
            } else {
                // Display failure message
                messageDiv.textContent = data.message || 'Error occurred during login';
                messageDiv.style.color = 'red';
            }
        })
        .catch(error => {
            // Handle errors in the fetch request
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again.';
            messageDiv.style.color = 'red';
        });
    });
});
