let userId = null;
console.log('Good morning...');

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

function fetchFavouriteCourses() {
    const requestData = new URLSearchParams();
    requestData.append("type", "favourite");
		requestData.append("userid", userId);

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
						courses=responseData;
            console.log("Received course data:", responseData);
						generateCourseHTML(courses);
        })
        .catch((error) => {
            console.error("Error fetching enrolled courses:", error);
        });
}

function generateCourseHTML(courses) {
  const coursesContainer = document.querySelector('.courses-container'); // Initialize here first

  // Check if there are no courses
  if (courses.length === 0) {
		coursesContainer.innerHTML = `
		<div class='no-course'>
		  <h1>You haven't added any courses to the favourites!</h1>
		  <p>Explore courses using search bar.</p>
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
          <div><button class='remove-from-favourite-btn btn' id="${courseId}">Remove from favourite</button></div>
					<div><button class='move-to-cart-btn btn' id="${courseId}">Move to cart</button></div>
        </div>
      </div>
    `;

    // Append the generated HTML to the courses container
    coursesContainer.innerHTML += courseHTML;

    // Event listener for "Remove from cart" button
    document.querySelectorAll('.remove-from-favourite-btn').forEach(button => {
        button.addEventListener('click', (event) => {
            const courseId = event.target.id; // Get course ID from button's ID attribute
            removeFromFavourite(courseId);
        });
    });
		
		document.querySelectorAll('.move-to-cart-btn').forEach(button => {
		    button.addEventListener('click', (event) => {
		        const courseId = event.target.id; // Get course ID from button's ID attribute
		        moveToCart(courseId);
		    });
		});
		
  });
}

function redirectCourse(courseId) {
	window.location.href = `show-course-preview.html?courseId=${courseId}`;
}


// Function to send a request to add course to cart
function removeFromFavourite(courseId) {
    const data = {
        courseid: courseId,
        userid: userId
    };

    // Sending POST request to the servlet
    fetch('removefromfavourites', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
		.then(res => res.json())  // Parse the response as JSON
		.then(data => {
		    console.log(data); // Print the entire parsed object
				window.location.reload();
		})
		.catch(error => {
		    console.error('Error:', error);
		});
}

function moveToCart(courseId) {
    let data = {
        courseid: courseId,
        userid: userId
    };

    // Sending POST request to the servlet
    fetch('removefromfavourites', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
		.then(res => res.json())  // Parse the response as JSON
		.then(data => {
		    console.log(data); // Print the entire parsed object
				window.location.reload();
		})
		.catch(error => {
		    console.error('Error:', error);
		});
		
		// Adding to cart
		data = {
		    courseid: courseId,
		    userid: userId,
		    course_type: 'cart'
		};
		fetch('addtocart', {
		    method: 'POST',
		    headers: {
		        'Content-Type': 'application/json',
		    },
		    body: JSON.stringify(data)
		})
		.then(res => res.json())  // Parse the response as JSON
		.then(data => {
		    alert(data.message); // Display the message if it's in the 'message' field of the response
		    console.log(data); // Print the entire parsed object
		})
		.catch(error => {
		    console.error('Error:', error);
		});
}

// Fetch session data and then fetch 
getSessionData()
    .then(() => fetchFavouriteCourses())
    .catch(error => {
        console.error("Error during initialization:", error);
    });
