// Get the search input and button elements
const searchInput = document.querySelector('.search-box');
const searchButton = document.querySelector('.search-button');

// Function to handle redirection with search query
function redirectToSearchPage() {
    const query = searchInput.value.trim();
    if (query) {
        // Redirect to search-course.html with the query parameter
        window.location.href = `search-courses.html?query=${encodeURIComponent(query)}`;
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
		console.log('You fuck');
    redirectToSearchPage();
});
