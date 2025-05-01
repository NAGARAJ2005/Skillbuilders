let userId = null;

// Function to get session data (e.g., userId) from the server
async function getSessionData() {
    try {
        const response = await fetch('getcurrentuser');
        const data = await response.json();

        if (data.userId) {
            console.log("User ID from session:", data.userId);
            userId = data.userId;
            return userId;
        } else {
            throw new Error(data.error || "No user in session.");
        }
    } catch (error) {
        console.error("Error fetching session data:", error);
        throw error;
    }
}

async function fetchCompletedCourses() {
    if (!userId) {
        console.error("User ID is null. Cannot fetch courses.");
        return;
    }

    const requestData = new URLSearchParams();
    requestData.append("type", "completed");
    requestData.append("userid", userId);

    try {
        const response = await fetch('fetchusercourses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: requestData.toString(),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const responseData = await response.json();
        console.log("Received course data:", responseData);
        generateCourseHTML(responseData);
    } catch (error) {
        console.error("Error fetching enrolled courses:", error);
        document.querySelector('.courses-container').innerHTML = 
            '<h1>Error fetching courses. Please try again later.</h1>';
    }
}

function generateCourseHTML(courses) {
    const coursesContainer = document.querySelector('.courses-container');

    // Check if there are no courses
    if (courses.length === 0) {
			coursesContainer.innerHTML = `
			<div class='no-course'>
			  <h1>You haven't completed any courses!</h1>
			  <p>If you have enrolled in any courses, go and complete the courese. You can explore more courses using search bar.</p>
			</div>
			`;
        return;
    }

    // Build HTML content for all courses
    let coursesHTML = '';
    courses.forEach(course => {
        const { 
            courseId, name, instructorId, instructorName, price, rating, ratingCount, 
            duration, moduleCount, enrolledCount, timeDate, thumbnail 
        } = course;

        //const instructorName = "John Doe"; // Replace with dynamic logic if needed

        coursesHTML += `
            <div class='course-and-btns-container'>
                <div class="course-container">
                    <div class="thumbnail-container">
                        <img class="course-thumbnail" src="images/course-thumbnails/${thumbnail}" alt="${name}">
                    </div>
                    <div class="course-details-container" onclick="redirectCourse(${courseId})">
                        <div class="course-title">${name}</div>
                        <div class="course-instructor">Instructor: ${instructorName}</div>
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
                    <button class='start-course-btn btn' id="${courseId}">Start course</button>
                    <button class='get-certificate-btn btn' id="${courseId}">Get Certificate</button>
                </div>
            </div>
        `;
    });

    // Update the DOM once after the loop
    coursesContainer.innerHTML = coursesHTML;

    // Add event listeners to buttons
    document.querySelectorAll('.start-course-btn').forEach(button => {
        button.addEventListener('click', (event) => {
            const courseId = event.target.id;
            window.location.href = `course-modules.html?courseId=${courseId}`;
        });
    });

    document.querySelectorAll('.get-certificate-btn').forEach(button => {
        button.addEventListener('click', (event) => {
            const courseId = event.target.id;

            fetch('updatecourseid', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ courseId }),
            })
                .then(response => response.json())
                .then(data => {
                    console.log('Course ID updated:', data);
                    window.location.href = `display-result.html`;
                })
                .catch(error => {
                    console.error('Error updating course ID:', error);
                });
        });
    });
}

function redirectCourse(courseId) {
    window.location.href = `show-course-preview.html?courseId=${courseId}`;
}

// Fetch session data and then fetch courses
getSessionData()
    .then(() => fetchCompletedCourses())
    .catch(error => {
        console.error("Error during initialization:", error);
    });
