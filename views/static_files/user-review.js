document.addEventListener("DOMContentLoaded", async () => {
    // Fetch the current user ID and course ID asynchronously
    let userId = null;
    let courseId = null;

    try {
        const [fetchedUserId, fetchedCourseId] = await Promise.all([
            fetchCurrentUserId(),
            fetchCurrentCourseId()
        ]);

        userId = fetchedUserId;
        courseId = fetchedCourseId;

        if (!userId || !courseId) {
            alert("Failed to retrieve user or course information.");
            return;
        }

        console.log("User ID:", userId);
        console.log("Course ID:", courseId);
    } catch (error) {
        console.error("Error initializing user or course ID:", error);
        alert("An error occurred while initializing the page.");
        return;
    }

    // Select the rating buttons and hidden input
    const starButtons = document.querySelectorAll(".star-btn");
    const ratingInput = document.getElementById("rating");

    // Add click event listener to star buttons
    starButtons.forEach((button) => {
        button.addEventListener("click", () => {
            const selectedValue = button.getAttribute("data-value");

            // Update the hidden input value with the selected rating
            ratingInput.value = selectedValue;

            // Highlight the selected stars
            starButtons.forEach((btn) => {
                btn.style.color = btn.getAttribute("data-value") <= selectedValue ? "gold" : "gray";
            });
        });
    });

    // Handle form submission
    const reviewForm = document.querySelector(".review-form");
    reviewForm.addEventListener("submit", (event) => {
        event.preventDefault(); // Prevent default form submission

        // Collect form data
        const rating = ratingInput.value;
        const reviewText = document.getElementById("review").value;

        // Ensure rating is provided
        if (rating === "0") {
            alert("Please select a rating.");
            return;
        }

        // Prepare data payload
        const payload = { userid:userId, courseid:courseId, rating, review: reviewText };

        // Send data to the server using Fetch API
        fetch("insertreview", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Server error: " + response.status);
                }
                return response.text();
            })
            .then((data) => {
                // Display success message
                alert("Review submitted successfully!");
                console.log(data);
            })
            .catch((error) => {
                // Handle errors
                alert("Error submitting review: " + error.message);
                console.error(error);
            });
    });

    // Functions to fetch current user ID and course ID
    async function fetchCurrentUserId() {
        const url = "getcurrentuser"; // URL of the servlet

        try {
            const response = await fetch(url, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            if (data.userId) {
                return data.userId;
            } else if (data.error) {
                console.error("Error:", data.error);
                return null;
            }
        } catch (error) {
            console.error("Error fetching user ID:", error);
            return null;
        }
    }

    async function fetchCurrentCourseId() {
        const url = "getcurrentcourse"; // URL of the servlet

        try {
            const response = await fetch(url, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            if (data.currentCourseId) {
                return data.currentCourseId;
            } else if (data.error) {
                console.error("Error:", data.error);
                return null;
            }
        } catch (error) {
            console.error("Error fetching current course ID:", error);
            return null;
        }
    }
});
