
function fetchCourses() {
    const url = "previewcourses"; // Replace with the actual path to your servlet

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            generateCourseHTML(data);
						console.log(data);
        })
        .catch(error => {
            console.error("Error fetching courses:", error);
            document.getElementById("coursesContainer").innerHTML = 
                "<p>Failed to load courses. Please try again later.</p>";
        });
}

fetchCourses();

function generateCourseHTML(courses) {
  const coursesContainer = document.querySelector('.courses-container'); // Initialize here first

  // Check if there are no courses
  if (courses.length === 0) {
    coursesContainer.innerHTML = `
			<div class='no-course'>
		    <h1>No courses to preview!</h1>
		    <p>This page will show you the courses uploaded by instructors for verification.</p>
			</div>
		`;
    return;
  }

  coursesContainer.innerHTML = ''; // Clear any existing content

  courses.forEach(course => {
    // Destructure course properties
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

    // Create the HTML for each course
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
					<div><button class='preview-btn btn' id="${courseId}">Preview</button></div>
					<div><button class='start-btn btn' id="${courseId}">Start</button></div>
		      <div><button class='accept-btn btn' id="${courseId}">Accept</button></div>
		      <div><button class='reject-btn' id="${courseId}">Reject</button></div>
		      <!-- Additional buttons for Preview and Start -->
		      
		    </div>

		    <!-- New div for message textarea -->
		    <div class="message-container">
		      <textarea class="message-input" placeholder="Enter message here..." id="message-${courseId}"></textarea>
		    </div>
		  </div>
		`;


    // Append the generated HTML to the courses container
    coursesContainer.innerHTML += courseHTML;
		
		document.querySelectorAll('.accept-btn').forEach(button => {
				    button.addEventListener('click', (event) => {
				        const courseId = event.target.id;  // Assuming courseId is stored in the button's id
								let message = document.querySelector(`textarea[id="${courseId}"]`);
				        acceptButtonHandler(courseId, message);
				    });
				});
				
		document.querySelectorAll('.reject-btn').forEach(button => {
				    button.addEventListener('click', (event) => {
				        const courseId = event.target.id;  // Assuming courseId is stored in the button's id
								let message = document.querySelector(`textarea[id="${courseId}"]`);
								rejectButtonHandler(courseId, message);
				    });
				});
				
		document.querySelectorAll('.preview-btn').forEach(button => {
				    button.addEventListener('click', (event) => {
				        const courseId = event.target.id;  // Assuming courseId is stored in the button's id
				        window.location.href = `show-course-preview.html?courseId=${courseId}`;
				    });
				});
				
		document.querySelectorAll('.start-btn').forEach(button => {
				    button.addEventListener('click', (event) => {
				        const courseId = event.target.id;  // Assuming courseId is stored in the button's id
				        window.location.href = `course-modules.html?courseId=${courseId}`;
				    });
				});
				
				// Function to handle Accept button (Approve Course and Add Message)
				
  });
}


function redirectCourse(courseId) {
	window.location.href = `show-course-preview.html?courseId=${courseId}`;
}

// Fetch session data and then fetch
getSessionData()
    .then(() => fetchCompletedCourses())
    .catch(error => {
        console.error("Error during initialization:", error);
    });
		
async function acceptButtonHandler(courseId, message) {
    try {
        // Step 1: Approve the course
        const approveResponse = await fetch('approvecourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                courseId: courseId
            })
        });

        if (!approveResponse.ok) {
            throw new Error('Failed to approve course');
        }

        const approveData = await approveResponse.json();
        if (approveData.result !== 'success') {
            throw new Error('Course approval failed: ' + approveData.message);
        }

        // Step 2: Fetch the course name
        const courseName = await fetchCourseName(courseId);
        console.log(courseName);  // This logs the course name, ensure it is fetched before proceeding

        // If course name is empty or failed to fetch, stop the operation
        if (!courseName) {
            throw new Error('Failed to fetch course name');
        }
				
				let acceptanceMessage = message;
        if (!message) {
            acceptanceMessage = `Course with ID '${courseId}' has been accepted.`;
        }

        // Step 3: Add the message
        const messageResponse = await fetch('addmessage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                courseId: courseId,
                name: courseName,  // course name should be available here
                message: acceptanceMessage
            })
        });

        if (!messageResponse.ok) {
            throw new Error('Failed to add message');
        }

        const messageData = await messageResponse.json();
        if (messageData.result !== 'success') {
            throw new Error('Message addition failed: ' + messageData.message);
        }

        // Step 4: Success alert and redirection
        alert('Course approved and message added successfully!');
        window.location.reload();

    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred: ' + error.message);
    }
}

						
// Function to handle Reject button (Delete Course and Add Message)
async function rejectButtonHandler(courseId, message) {
    try {
        // Delete the course
        const deleteResponse = await fetch('deletecourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                courseId: courseId
            })
        });

        if (!deleteResponse.ok) {
            throw new Error('Failed to delete course');
        }

        const deleteData = await deleteResponse.json();
        if (deleteData.result !== 'success') {
            throw new Error('Course deletion failed: ' + deleteData.message);
        }

        // Add the rejection message
        let rejectionMessage = message;
        if (!message) {
            rejectionMessage = `Course with ID '${courseId}' has been rejected and deleted.`;
        }
				
				const courseName = await fetchCourseName(courseId);
        const messageResponse = await fetch('addmessage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                courseId: courseId,
								name: courseName,
                message: rejectionMessage
            })
        });

        if (!messageResponse.ok) {
            throw new Error('Failed to add rejection message');
        }

        const messageData = await messageResponse.json();
        if (messageData.result !== 'success') {
            throw new Error('Message addition failed: ' + messageData.message);
        }

        // Success
        alert('Course rejected and message added successfully!');
        window.location.reload(); // Redirect to course list or relevant page
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred: ' + error.message);
    }
}


						// Helper function to fetch the course name
async function fetchCourseName(courseId) {
    const url = 'fetchcoursebyid';

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `courseid=${encodeURIComponent(courseId)}`,
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch course name: ${response.status}`);
        }

        const data = await response.json();
        if (data.name) {
						console.log(data, ' ', data.name);
            return data.name;
        } else {
            console.error('Error fetching course name:', data.error);
            return '';
        }
    } catch (error) {
        console.error('Error fetching course name:', error);
        return '';
    }
}

