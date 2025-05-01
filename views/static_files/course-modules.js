let courseData;

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
            courseData = responseData; // The course object returned by the servlet
            console.log("Received course data:", courseData);
            generateSidebar(courseData);
        })
        .catch((error) => {
            console.error("Error fetching course details:", error);
        });
}

// Call the function to fetch course details
fetchCourse();


// Function to escape HTML special characters
function escapeHtml(text) {
  const div = document.createElement('div');
  div.innerText = text;
  return div.innerHTML;
}

// Generating sidebar with dynamic course modules
function generateSidebar(courseData) {
  let html = "";
  for (let i = 1; i < courseData.moduleCount; i++) {
    html += `
      <button class="module module-${i}" data-module="${i}">Module ${i}</button>
    `;
  }
  html += `<button class="module module-final" data-module="${courseData.moduleCount}">Final Exam</button>`;
  document.querySelector('.module-nav').innerHTML = html;

  // Event listeners for dynamically generated buttons
  document.querySelectorAll('.module').forEach((button) => {
    button.addEventListener('click', (event) => {
      const moduleNumber = event.target.getAttribute('data-module');
			console.log("Clicked module : " + moduleNumber);
      displayModule(courseData, parseInt(moduleNumber));
    });
  });
}

// Generating module contents
function displayModule(courseData, moduleNumber) {
  // Highlight the current module button in the sidebar
  document.querySelectorAll('.module').forEach((btn, idx) => {
    if (idx + 1 === moduleNumber) {
      btn.classList.add('current-module');
    } else {
      btn.classList.remove('current-module');
    }
  });

  const moduleDetails = courseData.lectures[moduleNumber - 1];
  let moduleTitleDiv = document.querySelector('.title-container');
  moduleTitleDiv.innerHTML = moduleDetails.moduleName;
  let testBtn = document.querySelector('.test-btn');

  // If it is a module with lecture-video and unit-test
  if (moduleNumber !== courseData.moduleCount) {
    if (testBtn.textContent === 'Submit Test') toggleTestbtn(testBtn);
		let html = `
			<iframe width="100%" height="100%" src="${moduleDetails.link}" 
			title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; 
			encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" 
			allowfullscreen></iframe>
		`;
    /*let html = `<iframe src="${moduleDetails.link}" frameborder="0" allowfullscreen></iframe>`;*/
    document.querySelector('.content-container').innerHTML = html;

    // Check if the buttons exist before adding event listeners
    testBtn = document.querySelector('.test-btn');
    const nextBtn = document.querySelector('.next-btn');

    testBtn.onclick = () => {
      generateTest(courseData, moduleDetails, 'Unit Test', moduleNumber);
    };
    nextBtn.onclick = () => {
			//console.log(moduleNumber);
      displayModule(courseData, moduleNumber + 1);
    };
  } else {
    generateTest(courseData, moduleDetails, 'Final Exam', moduleNumber);
		const nextBtn = document.querySelector('.next-btn');
		nextBtn.onclick = () => {
			console.log('next btn clicked from final test');
      window.location.href = "display-result.html";
    };
  }
}

// Generate the test for the module
function generateTest(courseData, moduleDetails, testType, moduleNumber) {
  let testHtml = `
    <div class="test-title-container">${testType}</div>
    <div class="question-container"></div>
  `;

  let questions = ''; // Initialize as an empty string

  const questionsArr = moduleDetails.questions;
  for (let i = 0; i < questionsArr.length; i++) {
    questions += `
      <div class="question">
        ${i + 1}. ${questionsArr[i].question}
      </div>
      <div class="options">
        <div class="option">
          <input type="radio" name="module${moduleNumber}_question${i + 1}" value="${questionsArr[i].option1}">
          <label>${escapeHtml(questionsArr[i].option1)}</label>
        </div>
        <div class="option">
          <input type="radio" name="module${moduleNumber}_question${i + 1}" value="${questionsArr[i].option2}">
          <label>${escapeHtml(questionsArr[i].option2)}</label>
        </div>
        <div class="option">
          <input type="radio" name="module${moduleNumber}_question${i + 1}" value="${questionsArr[i].option3}">
          <label>${escapeHtml(questionsArr[i].option3)}</label>
        </div>
				<div class="option">
          <input type="radio" name="module${moduleNumber}_question${i + 1}" value="${questionsArr[i].option3}">
          <label>${escapeHtml(questionsArr[i].option4)}</label>
        </div>
      </div>
    `;
  }

  document.querySelector('.content-container').innerHTML = testHtml;
  document.querySelector('.question-container').innerHTML = questions;

  const testBtn = document.querySelector('.test-btn');
  if (testBtn && testBtn.textContent === 'Take Test') {
    toggleTestbtn(testBtn, courseData, moduleDetails, moduleNumber);
  }
}

// Evaluate the test based on selected answers
function evaluateTest(courseData, moduleDetails, moduleNumber) {
  let marks = 0;
  const questionsArr = moduleDetails.questions;

  questionsArr.forEach((question, index) => {
    const selectedOption = document.querySelector(
      `input[name="module${moduleNumber}_question${index + 1}"]:checked`
    );
    if (selectedOption && selectedOption.value === question[`answer`]) {
      console.log(selectedOption.value);
      marks++;
    }
  });

  const totalQuestions = questionsArr.length;
  const percentage = (marks / totalQuestions) * 100;
  let result, resultImage, textColor;
  if (percentage >= 40) {
    result = "You pass";
    resultImage = "images/exam-result-images/pass-image.avif";
    textColor = "#15bb4c";
  } else {
    result = "You fail";
    resultImage = "images/exam-result-images/fail-image.jpg";
    textColor = "rgb(244, 95, 95)";
  }

  // Preparing data to send in the fetch request
  const payload = {
    courseid: courseData.courseId,
    userid: 100005,	
    module_number: moduleNumber,
    total_marks: totalQuestions,
    user_marks: marks,
    result: percentage >= 40 ? "pass" : "fail",
  };

  // Sending the request to the server
  fetch("updatetestresult", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.status === "success") {
        console.log("Server response: ", data.message);
      } else {
        console.warn("Server response: ", data.message);
      }
    })
    .catch((error) => {
      console.error("Error updating test result: ", error);
    });

  // Updating the UI with the test result
  document.querySelector(".content-container").innerHTML = `
    <div class="test-result" style="color: ${textColor};">
      ${result} : Your score - ${marks}/${totalQuestions}
    </div>
    <div class='result-image-container'><img class='result-image' src='${resultImage}'></div>
  `;

  const testBtn = document.querySelector(".test-btn");
  if (testBtn) {
    toggleTestbtn(testBtn, courseData, moduleDetails, moduleNumber);
  }
}


// Toggle the test button's text and functionality
function toggleTestbtn(testBtn, courseData, moduleDetails, moduleNumber) {
  if (testBtn.textContent === 'Submit Test') {
    testBtn.textContent = "Take Test";
    testBtn.onclick = () => {
      generateTest(courseData, moduleDetails, 'Unit Test', moduleNumber);
    };
  } else {
    testBtn.textContent = "Submit Test";
    testBtn.onclick = () => {
      evaluateTest(courseData, moduleDetails, moduleNumber);
    };
  }
}
