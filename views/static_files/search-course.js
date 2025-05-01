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

// Fetch session data and then proceed to search for courses
getSessionData()
    .then(() => {
        // Now userId is available, proceed to handle course search and rendering
        const urlParams = new URLSearchParams(window.location.search);
        const searchQuery = urlParams.get('query');

        if (searchQuery) {
            console.log("Search Query from URL:", searchQuery);

            // Send a POST request to the servlet with the search string
            fetch('coursesearch', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'searchString=' + encodeURIComponent(searchQuery) // URL-encoded search string
            })
            .then(response => response.json()) // Parse response as JSON
            .then(courses => {
                console.log(courses);
                // You can now handle rendering in your existing function
                // Call your existing rendering function here to display the courses
                generateCourseHTML(courses);
            })
            .catch(error => {
                console.error('Error fetching courses:', error);
                document.getElementById("courseResults").innerHTML = '<p>Failed to fetch courses. Please try again later.</p>';
            });
        } else {
            console.log("No search query in the URL.");
        }
    })
    .catch(error => {
        console.error("Error during initialization:", error);
    });

// Modify the generateCourseHTML function to use the dynamically fetched userId
function generateCourseHTML(courses) {
    const coursesContainer = document.querySelector('.courses-container');
    if (courses.length == 0) {
        coursesContainer.innerHTML = `
        <div class='no-course'>
          <h1>No courses found!</h1>
          <p>Explore other courses using search bar.</p>
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
                <div><button class='add-to-favourites-btn btn' id="${courseId}">Add to favourites</button></div>
                <div><button class='add-to-cart-btn btn' id="${courseId}">Add to cart</button></div>
              </div>
            </div>
        `;

        // Append the generated HTML to the courses container
        coursesContainer.innerHTML += courseHTML;

        // Event listener for "Add to favourites" button
        document.querySelectorAll('.add-to-favourites-btn').forEach(button => {
            button.addEventListener('click', (event) => {
                const courseId = event.target.id; // Get course ID from button's ID attribute
                addToFavourites(courseId);
                window.location.reload();
            });
        });

        // Event listener for "Add to cart" button
        document.querySelectorAll('.add-to-cart-btn').forEach(button => {
            button.addEventListener('click', (event) => {
                const courseId = event.target.id; // Get course ID from button's ID attribute
                addToCart(courseId);
            });
        });

    });
}

// Redirecting to the preview course page
function redirectCourse(courseId) {
    window.location.href = `show-course-preview.html?courseId=${courseId}`;
}

// Function to send a request to add course to favourites
function addToFavourites(courseId) {
    console.log('I am called');
    if (!userId) {
        alert('Please log in to add to favourites.');
        return;
    }
    const courseType = 'favourite'; // Hardcoding course type as 'favourites'

    const data = {
        courseid: courseId,
        userid: userId,
        course_type: courseType
    };

    // Sending POST request to the servlet
    fetch('addtofavourites', {
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

// Function to send a request to add course to cart
function addToCart(courseId) {
    if (!userId) {
        alert('Please log in to add to cart.');
        return;
    }
    const courseType = 'cart'; // Hardcoding course type as 'cart'

    const data = {
        courseid: courseId,
        userid: userId,
        course_type: courseType
    };

    // Sending POST request to the servlet
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
