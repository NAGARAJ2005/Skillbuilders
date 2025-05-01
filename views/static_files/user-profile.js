
let userData = {}; 

const streamOptions = [
  "Web Development",
  "Java Development",
  "Data Science",
  "Artificial Intelligence",
  "Machine Learning",
  "Cybersecurity",
  "Cloud Computing",
  "Database Management",
  "Mobile App Development",
  "Game Development",
  "Blockchain Technology",
  "Internet of Things (IoT)",
  "UI/UX Design",
  "Data Structures and Algorithms",
  "Networking",
  "DevOps",
  "Software Testing",
  "Big Data",
  "Full Stack Development",
  "Frontend Development",
  "Backend Development",
  "Robotics",
  "Computer Vision",
  "Augmented Reality (AR)",
  "Virtual Reality (VR)",
  "Business Analytics",
  "Digital Marketing",
  "Project Management"
];

let userId = null;
function getSessionData() {
    fetch('getcurrentuser')  // Make a GET request to the /getSessionData servlet
        .then(response => response.json())  // Parse the JSON response
        .then(data => {
            if (data.userId) {
                console.log("User ID from session:", data.userId);
								userId = data.userId;
                // Now you can use the userId in your frontend logic
                // Example: Displaying the user ID in an element
            } else {
                console.log(data.error || "No user in session.");
            }
        })
        .catch(error => {
            console.error("Error fetching session data:", error);
        });
}


document.addEventListener("DOMContentLoaded", () => {
  // Fetch user profile data from the servlet
	//const contextPath = window.location.pathname.split('/')[1];
	getSessionData();
	
  fetch("fetchuserinformation" , {
		method: "GET"
	})
    .then((response) => response.json())
    .then((data) => {
      if (data.result === "failure") {
        alert(data.message); // Show an alert if the user is not logged in or an error occurs
        return;
      }
			
			console.log(data);
			
			userData = data;
			userData.userId = userId;
      // Populate fields with user data from the response
      document.querySelector(".profile-image").src = `images/user-profile-images/${data.profile}`;
      document.getElementById("name").textContent = data.name;
      document.getElementById("gender").textContent = data.gender;
      document.getElementById("professionalSummary").textContent = data.professionalSummary;
      document.getElementById("dob").textContent = data.dob;
      document.getElementById("phoneNumber").textContent = data.phoneNumber;
      document.getElementById("country").textContent = data.country;
      document.getElementById("city").textContent = data.city;
      document.getElementById("grade").textContent = data.grade;

      // Display current stream as a div with class 'stream'
      const currentStreamDiv = document.createElement("div");
      currentStreamDiv.className = "stream";
      currentStreamDiv.id = "currentstream-display";
      currentStreamDiv.textContent = data.stream;
      document.getElementById("stream").appendChild(currentStreamDiv);

      // Display interested streams
      const interestedStreamsContainer = document.getElementById("interestedStreams");
			const streams = Array.isArray(data.interestedStreams) ? data.interestedStreams : [];
      streams.forEach((stream, index) => {
        const streamDiv = document.createElement("div");
        streamDiv.className = "stream";
        streamDiv.id = `interestedstreams-stream-${index}`;
        streamDiv.textContent = stream;
        interestedStreamsContainer.appendChild(streamDiv);
      });

      // Add event listeners for each edit button
      document.getElementById("editname").addEventListener("click", () => editName("name"));
      document.getElementById("editgender").addEventListener("click", () => editGender("gender", ["Male", "Female", "Other"]));
      document.getElementById("editprofessionalSummary").addEventListener("click", () => editSummary("professionalSummary"));
      document.getElementById("editdob").addEventListener("click", () => editDOB("dob", "date"));
      document.getElementById("editphoneNumber").addEventListener("click", () => editPhoneNumber("phoneNumber"));
      document.getElementById("editcountry").addEventListener("click", () => editCountry("country"));
      document.getElementById("editcity").addEventListener("click", () => editCity("city"));
      document.getElementById("editgrade").addEventListener("click", () => editGrade("grade"));
      document.getElementById("editstream").addEventListener("click", () => editCurrentStream("stream", streamOptions));
      document.getElementById("editinterestedStreams").addEventListener("click", () => editInterestedStreams("interestedStreams", streamOptions));
			document.querySelector(".edit-profile-btn").addEventListener("click", () => editProfile("profile"));
    })
    .catch((error) => {
      console.error("Error fetching user profile:", error);
      alert("An error occurred while fetching user data.");
    });
});

// Function to handle fetch request to update field value
function updateFieldValue(userId, fieldId, newValue) {
    return fetch('updateuserinformation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `userid=${encodeURIComponent(userId)}&field=${encodeURIComponent(fieldId)}&value=${encodeURIComponent(newValue)}`
    })
    .then(response => response.json());
}

// Editing name
function editName(fieldId) {
    const field = document.getElementById(fieldId);
    const button = document.getElementById("edit" + fieldId);

    if (button.textContent === "Edit") {
        // Create an input element to edit the field value
        const input = document.createElement("input");
        input.type = "text";
        input.value = userData[fieldId];
        input.name = fieldId;
        input.id = `${fieldId}-input`;

        field.innerHTML = "";
        field.appendChild(input);
        button.textContent = "Save";
    } else {
        // Get the updated value
        const newValue = field.querySelector("input").value;

        // Call the separate function for updating the value
        updateFieldValue(userData.userId, fieldId, newValue)
            .then(data => {
                if (data.status === "success") {
                    // Update the UI with the new value
                    field.textContent = newValue;
                    userData[fieldId] = newValue;
                    //alert(data.message); 
										console.log(data.message);
                } else {
                    alert(data.message); // Show failure message
                }
            })
            .catch(error => {
                alert("An error occurred while updating the field: " + error.message);
                console.error("Error:", error);
            });

        button.textContent = "Edit";
    }
}


//Editing gender
function editGender(fieldId, options) {
    const field = document.getElementById(fieldId);
    const button = document.getElementById("edit" + fieldId);

    if (button.textContent === "Edit") {
        const input = document.createElement("select");
        input.name = fieldId;
        input.id = `${fieldId}-select`;
        options.forEach(option => {
            const optionElem = document.createElement("option");
            optionElem.value = option;
            optionElem.textContent = option;
            if (option === userData[fieldId]) optionElem.selected = true;
            input.appendChild(optionElem);
        });

        field.innerHTML = "";
        field.appendChild(input);
        button.textContent = "Save";
    } else {
        const newValue = field.querySelector("select").value;

        updateFieldValue(userData.userId, fieldId, newValue)
            .then(data => {
                if (data.status === "success") {
                    field.textContent = newValue;
                    userData[fieldId] = newValue;
										//alert(data.message); 
										console.log(data.message);
                } else {
                    alert(data.message); // Show failure message
                }
            })
            .catch(error => {
                alert("An error occurred while updating the field: " + error.message);
                console.error("Error:", error);
            });

        button.textContent = "Edit";
    }
}


//Editing proffessional summary
function editSummary(fieldId) {
    const field = document.getElementById(fieldId);
    const button = document.getElementById("edit" + fieldId);

    if (button.textContent === "Edit") {
        const input = document.createElement("textarea");
        input.value = userData[fieldId];
        input.name = fieldId;
        input.id = `${fieldId}-textarea`;

        field.innerHTML = "";
        field.appendChild(input);
        button.textContent = "Save";
    } else {
        const newValue = field.querySelector("textarea").value;

        updateFieldValue(userData.userId, fieldId, newValue)
            .then(data => {
                if (data.status === "success") {
                    field.textContent = newValue;
                    userData[fieldId] = newValue;
										//alert(data.message); 
										console.log(data.message);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                alert("An error occurred while updating the field: " + error.message);
                console.error("Error:", error);
            });

        button.textContent = "Edit";
    }
}


//Editing Date of Birth
function editDOB(fieldId) {
    const field = document.getElementById(fieldId);
    const button = document.getElementById("edit" + fieldId);

    if (button.textContent === "Edit") {
        const input = document.createElement("input");
        input.type = "date";
        input.value = userData[fieldId];
        input.name = fieldId;
        input.id = `${fieldId}-input`;

        field.innerHTML = "";
        field.appendChild(input);
        button.textContent = "Save";
    } else {
        const newValue = field.querySelector("input").value;

        updateFieldValue(userData.userId, fieldId, newValue)
            .then(data => {
                if (data.status === "success") {
                    field.textContent = newValue;
                    userData[fieldId] = newValue;
										//alert(data.message); 
										console.log(data.message);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                alert("An error occurred while updating the field: " + error.message);
                console.error("Error:", error);
            });

        button.textContent = "Edit";
    }
}


//Editing Phone Number
function editPhoneNumber(fieldId) {
    const field = document.getElementById(fieldId);
    const button = document.getElementById("edit" + fieldId);

    if (button.textContent === "Edit") {
        const input = document.createElement("input");
        input.type = "text";
        input.value = userData[fieldId];
        input.name = fieldId;
        input.id = `${fieldId}-input`;

        field.innerHTML = "";
        field.appendChild(input);
        button.textContent = "Save";
    } else {
        const newValue = field.querySelector("input").value;

				updateFieldValue(userData.userId, fieldId, newValue)
				    .then(data => {
				        if (data.status === "success") {
				            field.textContent = newValue;
				            userData[fieldId] = newValue;
										//alert(data.message); 
										console.log(data.message);
				        } else {
				            alert(data.message);
				        }
				    })
				    .catch(error => {
				        alert("An error occurred while updating the field: " + error.message);
				        console.error("Error:", error);
				    });

        button.textContent = "Edit";
    }
}


//Editing Country
function editCountry(fieldId) {
	const field = document.getElementById(fieldId);
	const button = document.getElementById("edit" + fieldId);

	if (button.textContent === "Edit") {
		const input = document.createElement("input");
		input.type = "text";
		input.value = userData[fieldId];
		input.name = fieldId;
		input.id = `${fieldId}-input`;

		field.innerHTML = "";
		field.appendChild(input);
		button.textContent = "Save";
	} else {
		let newValue = field.querySelector("input").value;

		updateFieldValue(userData.userId, fieldId, newValue)
		    .then(data => {
		        if (data.status === "success") {
		            field.textContent = newValue;
		            userData[fieldId] = newValue;
								//alert(data.message); 
								console.log(data.message);
		        } else {
		            alert(data.message);
		        }
		    })
		    .catch(error => {
		        alert("An error occurred while updating the field: " + error.message);
		        console.error("Error:", error);
		    });

		button.textContent = "Edit";
	}
}


//Editing city
function editCity(fieldId) {
	const field = document.getElementById(fieldId);
	const button = document.getElementById("edit" + fieldId);

	if (button.textContent === "Edit") {
		input = document.createElement("input");
		input.type = "text";
		input.value = userData[fieldId];
		input.name = fieldId;
		input.id = `${fieldId}-input`;

		field.innerHTML = "";
		field.appendChild(input);
		button.textContent = "Save";
	} else {
		let newValue = field.querySelector("input").value;
		field.textContent = newValue;
		

		updateFieldValue(userData.userId, fieldId, newValue)
		    .then(data => {
		        if (data.status === "success") {
		            field.textContent = newValue;
		            userData[fieldId] = newValue;
								//alert(data.message); 
								console.log(data.message);
		        } else {
		            alert(data.message);
		        }
		    })
		    .catch(error => {
		        alert("An error occurred while updating the field: " + error.message);
		        console.error("Error:", error);
		    });
			
		button.textContent = "Edit";
	}
}


//Editing Grade
function editGrade(fieldId) {
	const field = document.getElementById(fieldId);
	const button = document.getElementById("edit" + fieldId);

	if (button.textContent === "Edit") {
		input = document.createElement("input");
		input.type = "text";
		input.value = userData[fieldId];
		input.name = fieldId;
		input.id = `${fieldId}-input`;

		field.innerHTML = "";
		field.appendChild(input);
		button.textContent = "Save";
	} else {
		let newValue = field.querySelector("input").value;
		
		updateFieldValue(userData.userId, fieldId, newValue)
		    .then(data => {
		        if (data.status === "success") {
		            field.textContent = newValue;
		            userData[fieldId] = newValue;
								//alert(data.message); 
								console.log(data.message);
		        } else {
		            alert(data.message);
		        }
		    })
		    .catch(error => {
		        alert("An error occurred while updating the field: " + error.message);
		        console.error("Error:", error);
		    });
				
		button.textContent = "Edit";
	}
}

//Editing current stream
function editCurrentStream(fieldId, options) {
	const field = document.getElementById(fieldId);
	const button = document.getElementById("edit" + fieldId);

	if (button.textContent === "Edit") {
		input = document.createElement("select");
		input.name = fieldId;
		input.id = `${fieldId}-select`;
		options.forEach(option => {
			const optionElem = document.createElement("option");
			optionElem.value = option;
			optionElem.textContent = option;
			if (option === userData[fieldId]) optionElem.selected = true;
			input.appendChild(optionElem);
		});

		field.innerHTML = "";
		field.appendChild(input);
		button.textContent = "Save";
	} else {
		let newValue = field.querySelector("select").value;

		updateFieldValue(userData.userId, fieldId, newValue)
		    .then(data => {
		        if (data.status === "success") {
		            field.textContent = newValue;
		            userData[fieldId] = newValue;
								//alert(data.message); 
								console.log(data.message);
		        } else {
		            alert(data.message);
		        }
		    })
		    .catch(error => {
		        alert("An error occurred while updating the field: " + error.message);
		        console.error("Error:", error);
		    });
		
		button.textContent = "Edit";
	}
}


//Editing Interested streams
function editInterestedStreams(fieldId, options) {
    const field = document.getElementById(fieldId);
    const button = document.getElementById("edit" + fieldId);

    if (button.textContent === "Edit") {
        // Create a multi-select dropdown
        const input = document.createElement("select");
        input.name = fieldId;
        input.id = `${fieldId}-multiselect`;
        input.multiple = true;

        // Populate the dropdown with options
        options.forEach(option => {
            const optionElem = document.createElement("option");
            optionElem.value = option;
            optionElem.textContent = option;
            if (userData[fieldId].includes(option)) optionElem.selected = true;
            input.appendChild(optionElem);
        });

        // Replace current field content with the dropdown
        field.innerHTML = "";
        field.appendChild(input);
        button.textContent = "Save";
    } else {
        // Get the selected values from the dropdown
        const selectedOptions = Array.from(field.querySelector("select").selectedOptions);
        const newValue = selectedOptions.map(option => option.value);

        // Update the data on the server
        updateFieldValue(userData.userId, fieldId, newValue)
            .then(data => {
                if (data.status === "success") {
										//alert(data.message);
										console.log(data.message);
										
										field.innerHTML = "";
						        newValue.forEach((value, index) => {
						            const streamDiv = document.createElement("div");
						            streamDiv.className = "stream";
						            streamDiv.id = `${fieldId}-stream-${index}`;
						            streamDiv.textContent = value;
						            field.appendChild(streamDiv);
						        });
                } else {
                    alert(data.message);
                    console.error("Update failed:", data);
                }
            })
            .catch(error => {
                alert("An error occurred while updating the field: " + error.message);
                console.error("Error:", error);

                // Optionally: Revert the changes in the UI
                newValue.forEach((value, index) => {
                    const streamDiv = document.createElement("div");
                    streamDiv.className = "stream";
                    streamDiv.id = `${fieldId}-stream-${index}`;
                    streamDiv.textContent = value;
                    field.appendChild(streamDiv);
                });
            });

        button.textContent = "Edit";
    }
}

//Editing profile
function editProfile(fieldId) {
	const field = document.getElementById(fieldId);
	const editButton = document.querySelector(".edit-profile-btn");
	//const profileImage = document.querySelector(".profile-image");
	const inputContainer = document.querySelector(".input-container");
	const inputField = document.getElementById("profileImageInput");

	if (editButton.textContent === "Edit profile") {
		// Set the input field value to the current image source
		inputField.value = field.src; // Pre-fill with current image URL

		// Show the input field container
		inputContainer.style.display = "flex";

		// Change button text to "Save profile"
		editButton.textContent = "Save profile";
	} else {
		// Get the value from the input field
		const newImagePath = inputField.value;

		// Change the src of the profile image
		if (newImagePath) {
			
			updateFieldValue(userData.userId, fieldId, newImagePath)
			    .then(data => {
			        if (data.status === "success") {
			            field.src = newImagePath;
			            alert(data.message);
			        } else {
			            //alert(data.message);
									console.log(data.message);
			        }
			    })
			    .catch(error => {
			        alert("An error occurred while updating the field: " + error.message);
			        console.error("Error:", error);
			    });

			// Hide the input field after saving
			inputContainer.style.display = "none";
	
			// Change button text back to "Edit profile"
			editButton.textContent = "Edit profile";
		}
	}
}