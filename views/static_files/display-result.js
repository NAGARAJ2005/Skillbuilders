// Fetch the current course ID
let userId = null;
let courseId = null;

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
            console.log("Current Course ID:", data.currentCourseId);
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

// Fetch the current user ID
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
            console.log("User ID from session:", data.userId);
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

// Fetch the user progress based on userId and courseId
async function fetchUserProgress() {
    const url = "fetchuserprogress"; // URL of the servlet

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `userid=${encodeURIComponent(userId)}&courseid=${encodeURIComponent(courseId)}`
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        if (data.progress !== undefined) {
            console.log("User progress:", data.progress);
            return data.progress;
        } else if (data.error) {
            console.error("Error:", data.error);
            return null;
        }
    } catch (error) {
        console.error("Error fetching user progress:", error);
        return null;
    }
}

// Initialize the progress container and populate content based on progress
async function initializeProgress() {
    userId = await fetchCurrentUserId();
    courseId = await fetchCurrentCourseId();

    if (!userId || !courseId) {
        console.error("User ID or Course ID is missing. Cannot fetch progress.");
        return;
    }

    const progress = await fetchUserProgress(userId, courseId);

    // Get reference to the progress container
    const progressContainer = document.getElementById('progress-container');

    // Generate content based on the progress
    if (progress === 100) {
        progressContainer.innerHTML = `
            <h1>You have successfully completed the course!</h1>
            <div class="button-container">
                <button id="feedback-btn" class="action-button">Give Feedback</button>
                <button id="certificate-btn" class="action-button">Get Certificate</button>
            </div>
        `;

        // Add button event listeners
        document.getElementById('feedback-btn').addEventListener('click', () => {
            window.location.href = 'user-review.html';
        });

        document.getElementById('certificate-btn').addEventListener('click', () => {
            generateCertificate();
        });

    } else if (progress < 100) {
        progressContainer.innerHTML = `
            <h1>You haven't completed the course yet!</h1>
            <p>Complete all the lessons to mark this course as complete.</p>
            <a href="user-page.html" class="go-back-link">Go back to the course page</a>
        `;
    }
}

// Generate the certificate PDF
async function generateCertificate() {
    if (!userId || !courseId) {
        console.error("User ID or Course ID is missing. Cannot generate certificate.");
        return;
    }

    // Fetch the user name and course name asynchronously
    const userName = await fetchUserName(userId);
		
    const courseName = await fetchCourseName(courseId);
		
		console.log(userName, ' ', courseName);

    // Configure PDF options
    const options = {
        margin: [0.5, 0.5, 0.5, 0.5],
        filename: `${userName}_certificate.pdf`,
        image: { type: 'jpeg', quality: 1 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: 'in', format: 'a4', orientation: 'landscape' }
    };
		
		let certificateContainer = document.querySelector(".certificate-container");
		const currentDate = new Date();
    const formattedDate = currentDate.toLocaleDateString("en-US", {
        year: "numeric",
        month: "long",
        day: "numeric",
    });
		document.querySelector(".date").textContent = formattedDate;
		document.querySelector(".username").innerHTML = userName;
		document.querySelector(".course-name").innerHTML = courseName;
		document.querySelector(".certificate-body").style.display = 'block';

    // Generate and save the certificate PDF
    html2pdf().from(certificateContainer).set(options).save();
}

// Fetch the user name based on the user ID
async function fetchUserName(userId) {
    const url = `fetchuserinformation?userId=${userId}`;

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Failed to fetch user name: ${response.status}`);
        }

        const data = await response.json();
        if (data.name) {
            return data.name; // Return the user name
        } else {
            console.error("Error fetching user name:", data.error);
            return '';
        }
    } catch (error) {
        console.error("Error fetching user name:", error);
        return '';
    }
}

async function fetchCourseName(courseId) {
    const url = "fetchcoursebyid"; // Adjust the path if necessary

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `courseid=${encodeURIComponent(courseId)}`, // Send courseId in the request body
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch course name: ${response.status}`);
        }

        const data = await response.json(); // Parse the JSON response
        if (data.name) {
            return data.name; // Return the course name
        } else {
            console.error("Error fetching course name:", data.error);
            return '';
        }
    } catch (error) {
        console.error("Error fetching course name:", error);
        return '';
    }
}


// Call the initializeProgress function
initializeProgress();
