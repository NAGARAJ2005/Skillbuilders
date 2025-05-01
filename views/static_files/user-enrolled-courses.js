let userId = null;

// Function to get session data (e.g., userId) from the server
function getSessionData() {
    return fetch('getcurrentuser') // Make a GET request to the /getSessionData servlet
        .then(response => response.json()) // Parse the JSON response
        .then(data => {
            if (data.userId) {
                console.log("User ID from session:", data.userId);
                userId = data.userId; // Set the userId
                return userId; // Return the userId
            } else {
                console.log(data.error || "No user in session.");
                throw new Error("No user in session.");
            }
        })
        .catch(error => {
            console.error("Error fetching session data:", error);
            throw error;
        });
}

function fetchEnrolledCourses() {
    if (!userId) {
        console.error("User ID is not available. Cannot fetch enrolled courses.");
        return;
    }

    const requestData = new URLSearchParams();
    requestData.append("type", "enrolled");
    requestData.append("userid", userId);

    console.log("Sending userId in request:", userId);

    fetch('fetchusercourses', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: requestData.toString(), // Send as URL-encoded form data
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((responseData) => {
            console.log("Received course data:", responseData);
            generateCourseHTML(responseData);
        })
        .catch((error) => {
            console.error("Error fetching enrolled courses:", error);
        });
}

// Function to generate course HTML
function generateCourseHTML(courses) {
    const coursesContainer = document.querySelector('.courses-container'); // Initialize here first

    // Check if there are no courses
    if (courses.length === 0) {
        coursesContainer.innerHTML = `
				<div class='no-course'>
				  <h1>You haven't enrolled in any courses!</h1>
				  <p>Explore courses using search bar.</p>
				</div>
				`;
        return;
    }

    coursesContainer.innerHTML = ''; // Clear any existing content

    courses.forEach(course => {
        const { 
            courseId,
            name, 
            instructorId, 
						instructorName,
            price, 
            rating, 
            ratingCount, 
            duration, 
            moduleCount, 
            enrolledCount, 
            timeDate, 
            thumbnail 
        } = course;

        //let instructorName = "John Doe"; // Set instructor name (could be dynamic based on `instructorId`)

        const courseHTML = `
          <div class='course-and-btns-container'>
            <div class="course-container">
              <div class="thumbnail-container">
                <img class="course-thumbnail" src="images/course-thumbnails/${thumbnail}" alt="${name}">
              </div>
              <div class="course-details-container" onclick="redirectCourse(${courseId})">
                <div class="course-title">${name}</div>
                <div class="course-instructor">Instructor : ${instructorName}</div>
                <div class="enrolled-count">${enrolledCount}+ enrollments till this date</div>
                <div class="course-rating">
                  <div class="rating">${(ratingCount > 0 ? (rating / ratingCount).toFixed(1) : 0.0)}</div>
                  <img class="ratings-star-image" src="images/rating-images/rating-star-image.png" alt="Rating Star">
                  <div class="rating-count">&#xb7; (${ratingCount}+ ratings)</div>
                </div>
                <div class="additional-details">${duration} hours &#xb7; ${moduleCount} modules</div>
                <div class="uploaded-date">Uploaded - ${timeDate}</div>
              </div>
              <div class="course-price-container">&#8377; ${price.toFixed(2)}</div>
            </div>
            <div class='btns-container'>
              <div><button class='start-course-btn btn' id="${courseId}">Start course</button></div>
            </div>
          </div>
        `;

        coursesContainer.innerHTML += courseHTML;
    });

    document.querySelectorAll('.start-course-btn').forEach(button => {
        button.addEventListener('click', (event) => {
            const courseId = event.target.id; // Assuming courseId is stored in the button's id
            window.location.href = `course-modules.html?courseId=${courseId}`;
        });
				
    });
		

}

function redirectCourse(courseId) {
	window.location.href = `show-course-preview.html?courseId=${courseId}`;
}

// Fetch session data and then fetch enrolled courses
getSessionData()
    .then(() => fetchEnrolledCourses())
    .catch(error => {
        console.error("Error during initialization:", error);
    });
