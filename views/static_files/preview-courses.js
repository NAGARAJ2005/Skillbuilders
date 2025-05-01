document.addEventListener("DOMContentLoaded", fetchCourses); // Call servlet on page load

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
            displayCourses(data);
        })
        .catch(error => {
            console.error("Error fetching courses:", error);
            document.getElementById("coursesContainer").innerHTML = 
                "<p>Failed to load courses. Please try again later.</p>";
        });
}

function displayCourses(courses) {
    const coursesContainer = document.getElementById("coursesContainer");
    coursesContainer.innerHTML = ""; // Clear the "Loading courses..." message

    if (courses.length === 0) {
        coursesContainer.innerHTML = "<p>No unapproved courses found.</p>";
        return;
    }

    courses.forEach(course => {
        const courseDiv = document.createElement("div");
        courseDiv.className = "course";

        courseDiv.innerHTML = `
            <h2>${course.name}</h2>
            <p><strong>Instructor ID:</strong> ${course.instructorId}</p>
            <p><strong>Price:</strong> $${course.price.toFixed(2)}</p>
            <p><strong>Rating:</strong> ${course.rating} (${course.ratingCount} reviews)</p>
            <p><strong>Duration:</strong> ${course.duration} hours</p>
            <p><strong>Description:</strong> ${course.description}</p>
            <p><strong>Streams:</strong> ${course.streams.join(", ")}</p>
            <p><strong>Prerequisites:</strong> ${course.prerequisites.join(", ")}</p>
        `;

        coursesContainer.appendChild(courseDiv);
    });
}
