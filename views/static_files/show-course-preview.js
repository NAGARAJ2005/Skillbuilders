
// fetchcoursebyid
function fetchCourse() {
    // Extract the course ID from the URL
    const urlParams = new URLSearchParams(window.location.search);
    const courseId = urlParams.get("courseId"); // Fetch the 'courseId' query parameter

    if (!courseId) {
        console.error("Course ID not found in the URL!");
        return;
    }

    const requestData = new URLSearchParams();
    requestData.append("courseid", courseId); // Correct parameter name used

    fetch('fetchcoursebyid', { // Make sure the URL corresponds to the correct servlet URL pattern
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
            return response.json(); // Parse the JSON response from the servlet
        })
        .then((responseData) => {
            let courseData = responseData; // The course object returned by the servlet
            console.log("Received course data:", courseData);
            renderCourseContent(courseData);
        })
        .catch((error) => {
            console.error("Error fetching course details:", error);
        });
}

// Call the function to fetch course details
fetchCourse();

// Function to render the course details and modules dynamically
function renderCourseContent(course) {
  const {
		courseId,
    name,
    description,
    duration,
    enrolledCount,
    instructorId,
		instructorName,
    prerequisites,
    price,
    rating,
    ratingCount,
    streams,
    thumbnail,
    timeDate,
    moduleCount,
    lectures,
  } = course;

  // Render course overview
  document.querySelector(".course-title").innerHTML = name;
  document.querySelector(".course-description").innerHTML = description;
  document.querySelector(".rating-views").innerHTML = `
    <span style="color: gold; font-weight: bold;">
      ${(ratingCount > 0 ? (rating / ratingCount).toFixed(1) : 0.0)}  <span style="font-size: 20px;">&#10031;</span>
    </span>
    <span style="color: rgb(201, 195, 195);">(${ratingCount} ratings)</span>
    - ${enrolledCount} Students
  `;
  document.querySelector(".instructor").innerHTML = `
    &#9998; Created by <a style="text-decoration:underline; color: rgb(48, 48, 208);" href="#">Instructor : ${instructorName}</a>
  `;
  document.querySelector(".date").innerHTML = `&#9775; Uploaded / Last updated: ${timeDate}`;

  // Render features and properties
  let featureHTML = `
		<div class="heading">Course features and properties</div>
    <div class="feature">&#10003; ${moduleCount} modules</div>
    <div class="feature">&#10003; ${duration} hours of course duration</div>
    <div class="feature">&#10003; Certificate of completion</div>
    <div class="feature">&#10003; Each module has a unit test</div>
  `;
  document.querySelector(".features-properties").innerHTML = featureHTML;

  // Render streams
  let streamsHTML = streams
    .map((stream) => `<div class="stream">${stream}</div>`)
    .join("");
  document.querySelector(".streams").innerHTML = streamsHTML;

  // Render prerequisites
  let prerequisitesHTML = prerequisites
    .map((prerequisite) => `<div class="prerequisite">${prerequisite}</div>`)
    .join("");
  document.querySelector(".prerequisites").innerHTML = prerequisitesHTML;

  // Render course content (modules)
  const courseContentHTML = lectures
    .map(
      (lecture) => `
      <div class="module">
        &#10022; ${lecture.moduleName}
      </div>`
    )
    .join("");
  document.querySelector(".modules").innerHTML = courseContentHTML;
	
	document.querySelector(".price-container").innerHTML = `&#8377; ${price}`;
	document.querySelector('.course-thumbnail').src = `images/course-thumbnails/${thumbnail}`;
	document.querySelector('.course-thumbnail').alt = name;
	document.querySelector('.add-to-cart-btn').dataset.courseid = courseId;
	document.querySelector('.favourite-course-btn').dataset.courseid = courseId;

}

// Call the function to render the content on page load
document.addEventListener("DOMContentLoaded", () => {
  fetchCourse();
	
  const addToCartBtn = document.querySelector('.add-to-cart-btn');
	addToCartBtn.addEventListener('click', (event) => {
		addToCart(addToCartBtn.dataset.courseid); 
	})

  const favoriteBtn = document.querySelector('.favourite-course-btn');
	favoriteBtn.addEventListener('click', (event) => {
		addToFavourites(favoriteBtn.dataset.courseid); 
	})

});


// Function to send a request to add course to favourites
function addToFavourites(courseId) {
    const userId = 100005; // Replace with the actual logged-in user ID
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
    const userId = 100005; // Replace with the actual logged-in user ID
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

