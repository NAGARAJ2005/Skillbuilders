const images = [
  'images/body-images/image-1.jpg',
  'images/body-images/image-2.jpg',
  'images/body-images/image-3.jpg'
];

let currentIndex = 0;

/*
function changeImage() {
  const dynamicImage = document.querySelector('.dynamic-image');
  currentIndex = (currentIndex + 1) % images.length;
  dynamicImage.src = images[currentIndex];
}

setInterval(changeImage, 2000);
*/
/*
let userId = null;
// Function to get session data (e.g., userId) from the server
function getSessionData() {
    fetch('getcurrentuser')  // Make a GET request to the /getSessionData servlet
        .then(response => response.json())  // Parse the JSON response
        .then(data => {
            if (data.userId) {
                console.log("User ID from session:", data.userId);
								userId = data.userId;
            } else {
                console.log(data.error || "No user in session.");
            }
        })
        .catch(error => {
            console.error("Error fetching session data:", error);
        });
}
getSessionData();

// Get the search input and button elements
const searchInput = document.querySelector('.search-box');
const searchButton = document.querySelector('.search-button');

// Function to handle redirection with search query
function redirectToSearchPage() {
    const query = searchInput.value.trim();
    if (query) {
        // Redirect to search-course.html with the query parameter
        window.location.href = `search-course.html?query=${encodeURIComponent(query)}`;
    } else {
        alert("Please enter a search term.");
    }
}

// Handle "Enter" key press
searchInput.addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        redirectToSearchPage();
    }
});

// Handle search button click
searchButton.addEventListener('click', function () {
    redirectToSearchPage();
});
*/


