function fetchInprogressCourses() {
    const requestData = new URLSearchParams();
    requestData.append("status", "inprogress");

    fetch('fetchinstructorcourses', {
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
						courses=responseData;
            console.log("Received course data:", responseData);
						generateCourseHTML(courses);
        })
        .catch((error) => {
            console.error("Error fetching enrolled courses:", error);
        });
}


function generateCourseHTML(courses) {
  if (courses.length == 0) {
		coursesContainer.innerHTML = `
		<div class='no-course'>
		  <h1>You haven't enrolled in any courses!</h1>
		  <p>Explore courses using search bar.</p>
		</div>
		`;
    return;
  }

  const coursesContainer = document.querySelector('.courses-container');
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
		
		//let instructorName = "John Doe";

    // Create the HTML for each course
    const courseHTML = `
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
    `;

    // Append the generated HTML to the courses container
    coursesContainer.innerHTML += courseHTML;
  });
}

fetchInprogressCourses();

function redirectCourse(courseId) {
	window.location.href = `show-course-preview.html?courseId=${courseId}`;
}