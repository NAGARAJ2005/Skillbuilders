// Select elements for course details and contents
const courseDetailsBtn = document.querySelector('.course-details-btn');
const courseContentsBtn = document.querySelector('.course-contents-btn');
const courseDetailsContainer = document.querySelector('.course-details-container');
const courseContentsContainer = document.querySelector('.course-contents-container');
const courseContentsDescription = document.querySelector('.course-content-description');

// Event listeners for buttons to toggle between course details and contents
courseDetailsBtn.addEventListener('click', function() {
    courseDetailsBtn.classList.add('active-button');
    courseContentsBtn.classList.remove('active-button');
    courseDetailsContainer.classList.add('active');
    courseContentsContainer.classList.remove('active');
});

courseContentsBtn.addEventListener('click', function() {
    courseContentsBtn.classList.add('active-button');
    courseDetailsBtn.classList.remove('active-button');
    courseContentsContainer.classList.add('active');
    courseDetailsContainer.classList.remove('active');
});

// Add event listeners for module management
document.getElementById('add-module-button').addEventListener('click', askModuleType);
document.getElementById('remove-module-button').addEventListener('click', removeLastModule);

let moduleCount = 0;

function askModuleType() {
    moduleCount++;
    renderCourseContentDescription();

    // Create a container for each module with buttons to select type
    const moduleDiv = document.createElement('div');
    moduleDiv.className = 'module-container';
    document.querySelector('.modules-container').appendChild(moduleDiv);

    moduleDiv.innerHTML = `
        <button class='lecture-button'>Lecture</button>
        <button class='final-test-button'>Final Test</button>
    `;

    // Lecture button functionality
    moduleDiv.querySelector('.lecture-button').addEventListener('click', () => {
        moduleDiv.innerHTML = `
            <div class='module-number-type'>Module ${moduleCount}: Lecture</div>
            <label>Module Name:</label><br>
            <input type='text' class='module-name' placeholder='Enter module name'><br><br>
            <label>Video URL/Link:</label><br>
            <input type='text' class='lecture-video' placeholder='Lecture video URL'><br><br>
            <div class='unit-test-container'>
                <div class='test-title'>Unit Test</div>
                <div class='questions-container'></div>
                <button class='add-question' type='button'>+ Question</button>
                <button class='remove-question' type='button'>- Question</button>
            </div>
        `;
        addQuestionFunctionality(moduleDiv);
    });

    // Final Test button functionality
    moduleDiv.querySelector('.final-test-button').addEventListener('click', () => {
        moduleDiv.innerHTML = `
            <div class='module-number-type'>Module ${moduleCount}: Final Test</div>
            <label>Module Name:</label>
            <input type='text' class='module-name' placeholder='Enter module name'><br><br>
            <div class='unit-test-container'>
                <div class='test-title'>Final Test</div>
                <div class='questions-container'></div>
                <button class='add-question' type='button'>+ Question</button>
                <button class='remove-question' type='button'>- Question</button>
            </div>
        `;
        addQuestionFunctionality(moduleDiv);
    });
}

function removeLastModule() {
    const lastModule = document.querySelector('.modules-container .module-container:last-child');
    if (lastModule) {
        lastModule.remove();
        moduleCount--; // Decrement module count when a module is removed
    }
    renderCourseContentDescription();
}

function renderCourseContentDescription() {
    if (moduleCount === 0) {
        courseContentsDescription.classList.add('active-description');
    } else {
        courseContentsDescription.classList.remove('active-description');
    }
}

function addQuestionFunctionality(moduleDiv) {
    const addQuestionBtn = moduleDiv.querySelector('.add-question');
    const removeQuestionBtn = moduleDiv.querySelector('.remove-question');
    const questionsContainer = moduleDiv.querySelector('.questions-container');

    addQuestionBtn.addEventListener('click', () => {
        const questionDiv = document.createElement('div');
        questionDiv.className = 'question';
        questionDiv.innerHTML = `
            <label>Question:</label><br>
            <input type="text" class="questionText" placeholder="Enter Question here"/><br><br>
            <span>Options</span><br>
            <label>1 :</label>
            <input type="text" class="option1 option" placeholder="Option 1"/>
            <label>2 :</label>
            <input type="text" class="option2 option" placeholder="Option 2"/>
            <label>3 :</label>
            <input type="text" class="option3 option" placeholder="Option 3"/>
            <label>4 :</label>
            <input type="text" class="option4 option" placeholder="Option 4"/>
            <input type="text" class="correctOption option" placeholder="Answer"/>
        `;
        questionsContainer.appendChild(questionDiv);
    });

    // Logic to remove the last question
    removeQuestionBtn.addEventListener('click', () => {
        const lastQuestion = questionsContainer.querySelector('.question:last-child');
        if (lastQuestion) {
            questionsContainer.removeChild(lastQuestion);
        }
    });
}

document.getElementById('submit-button').addEventListener('click', submitAllModules);

function submitAllModules() {
	console.log(document.getElementById('course-price').value);
    const courseDetails = {
        title: document.getElementById('course-title').value,
        description: document.getElementById('course-description').value,
        duration: document.getElementById('course-duration').value,
        modulesCount: document.getElementById('modules-count').value,
        thumbnail: document.getElementById('thumbnail').value,
        price: document.getElementById('course-price').value,
        streams: Array.from(document.getElementById('streams').selectedOptions).map(option => option.value),
        prerequisites: document.getElementById("course-prerequisites")?.value.split(",").map(item => item.trim()).filter(item => item) || []
    };

    const modules = [];
    document.querySelectorAll('.module-container').forEach(module => {
        const moduleType = module.querySelector('.module-number-type').textContent.includes('Final Test') ? 'Final Test' : 'Lecture';
        const moduleData = {
            type: moduleType,
            moduleName: module.querySelector('.module-name') ? module.querySelector('.module-name').value : null,
            lectureLink: moduleType === 'Lecture' ? module.querySelector('.lecture-video').value : null,
            questions: []
        };

        module.querySelectorAll('.question').forEach(question => {
            const questionData = {
                questionText: question.querySelector('.questionText').value,
                options: [
                    question.querySelector('.option1').value,
                    question.querySelector('.option2').value,
                    question.querySelector('.option3').value,
                    question.querySelector('.option4').value
                ],
                correctOption: question.querySelector('.correctOption').value
            };
            moduleData.questions.push(questionData);
        });

        modules.push(moduleData);
    });
/*
    let formData = {
        courseDetails,
        modules
    };
*/

let mernStackCourseData = {
    courseDetails: {
        title: 'MERN Stack Development',
        description: 'Master the MERN stack (MongoDB, Express.js, React, and Node.js) to build full-stack web applications.',
        duration: 50, // duration in hours
        modulesCount: 7, // total number of modules
        thumbnail: 'mern-stack-course.png',
        price: 700, // course price in currency
        streams: ['Web Development', 'Full Stack Development'],
        prerequisites: ['Basic knowledge of JavaScript', 'Understanding of HTML and CSS'],
    },
    modules: [
        {
            type: 'Lecture',
            moduleName: 'Introduction to MERN Stack',
            lectureLink: 'www.mern-intro.com',
            questions: [
                {
                    questionText: 'What does MERN stand for?',
                    options: [
                        'MongoDB, Express.js, React, Node.js',
                        'MySQL, Express.js, React, Node.js',
                        'MongoDB, Express.js, Redux, Node.js',
                        'MongoDB, Ember.js, React, Node.js'
                    ],
                    correctOption: 'MongoDB, Express.js, React, Node.js',
                },
                {
                    questionText: 'Which component of the MERN stack handles the database?',
                    options: ['MongoDB', 'Express.js', 'React', 'Node.js'],
                    correctOption: 'MongoDB',
                },
                {
                    questionText: 'Which part of the MERN stack is used for building the front end?',
                    options: ['Node.js', 'React', 'Express.js', 'MongoDB'],
                    correctOption: 'React',
                },
                {
                    questionText: 'What is the primary language used in the MERN stack?',
                    options: ['Python', 'JavaScript', 'Ruby', 'PHP'],
                    correctOption: 'JavaScript',
                },
                {
                    questionText: 'Which of the following is a NoSQL database?',
                    options: ['MongoDB', 'MySQL', 'PostgreSQL', 'Oracle'],
                    correctOption: 'MongoDB',
                }
            ]
        },
        {
            type: 'Lecture',
            moduleName: 'MongoDB Basics',
            lectureLink: 'www.mongodb-basics.com',
            questions: [
                {
                    questionText: 'What type of database is MongoDB?',
                    options: [
                        'Relational Database',
                        'Document-oriented NoSQL Database',
                        'Key-Value Store',
                        'Graph Database'
                    ],
                    correctOption: 'Document-oriented NoSQL Database',
                },
                {
                    questionText: 'Which command is used to create a database in MongoDB?',
                    options: ['use <database_name>', 'create <database_name>', 'newdb <database_name>', 'makedb <database_name>'],
                    correctOption: 'use <database_name>',
                },
                {
                    questionText: 'What is the default data format used in MongoDB?',
                    options: ['JSON', 'XML', 'CSV', 'YAML'],
                    correctOption: 'JSON',
                },
                {
                    questionText: 'What does CRUD stand for?',
                    options: ['Create, Read, Update, Delete', 'Connect, Run, Update, Delete', 'Copy, Replace, Update, Delete', 'Create, Replace, Use, Drop'],
                    correctOption: 'Create, Read, Update, Delete',
                },
                {
                    questionText: 'How do you query all documents in a MongoDB collection?',
                    options: [
                        'db.collection.find()',
                        'db.collection.queryAll()',
                        'db.collection.select()',
                        'db.collection.retrieve()'
                    ],
                    correctOption: 'db.collection.find()',
                }
            ]
        },
        {
            type: 'Lecture',
            moduleName: 'Express.js Basics',
            lectureLink: 'www.express-basics.com',
            questions: [
                {
                    questionText: 'What is Express.js primarily used for?',
                    options: [
                        'Building APIs and handling server-side logic',
                        'Creating front-end designs',
                        'Managing databases',
                        'None of the above'
                    ],
                    correctOption: 'Building APIs and handling server-side logic',
                },
                {
                    questionText: 'Which command is used to install Express.js?',
                    options: ['npm install express', 'npm install expressjs', 'node install express', 'install express'],
                    correctOption: 'npm install express',
                },
                {
                    questionText: 'Which method is used to define a route in Express?',
                    options: ['app.get()', 'app.route()', 'app.post()', 'All of the above'],
                    correctOption: 'All of the above',
                },
                {
                    questionText: 'What is middleware in Express.js?',
                    options: [
                        'Functions executed after request handling',
                        'Functions executed during request processing',
                        'Functions executed before request handling',
                        'Functions executed outside the server'
                    ],
                    correctOption: 'Functions executed during request processing',
                },
                {
                    questionText: 'Which method is used to start an Express server?',
                    options: ['app.listen()', 'app.run()', 'server.start()', 'express.listen()'],
                    correctOption: 'app.listen()',
                }
            ]
        },
        {
            type: 'Lecture',
            moduleName: 'React Basics',
            lectureLink: 'www.react-basics.com',
            questions: [
                {
                    questionText: 'What is React primarily used for?',
                    options: [
                        'Building server-side APIs',
                        'Creating interactive user interfaces',
                        'Managing databases',
                        'None of the above'
                    ],
                    correctOption: 'Creating interactive user interfaces',
                },
                {
                    questionText: 'What does JSX stand for?',
                    options: [
                        'JavaScript XML',
                        'Java XML Script',
                        'JavaScript eXtension',
                        'None of the above'
                    ],
                    correctOption: 'JavaScript XML',
                },
                {
                    questionText: 'What is a React component?',
                    options: [
                        'A reusable piece of UI',
                        'A database record',
                        'A Node.js module',
                        'None of the above'
                    ],
                    correctOption: 'A reusable piece of UI',
                },
                {
                    questionText: 'Which method is used to render components in React?',
                    options: ['ReactDOM.render()', 'React.render()', 'DOM.render()', 'Component.render()'],
                    correctOption: 'ReactDOM.render()',
                },
                {
                    questionText: 'Which React hook is used to manage state?',
                    options: ['useState', 'useEffect', 'useContext', 'useReducer'],
                    correctOption: 'useState',
                }
            ]
        },
        {
            type: 'Lecture',
            moduleName: 'Node.js Basics',
            lectureLink: 'www.nodejs-basics.com',
            questions: [
                {
                    questionText: 'What is Node.js used for?',
                    options: [
                        'Running JavaScript on the server side',
                        'Creating front-end designs',
                        'Managing databases',
                        'None of the above'
                    ],
                    correctOption: 'Running JavaScript on the server side',
                },
                {
                    questionText: 'Which command is used to initialize a Node.js project?',
                    options: ['npm init', 'node init', 'npm start', 'node start'],
                    correctOption: 'npm init',
                },
                {
                    questionText: 'What is npm?',
                    options: [
                        'Node Package Manager',
                        'Node Programming Module',
                        'Network Programming Manager',
                        'None of the above'
                    ],
                    correctOption: 'Node Package Manager',
                },
                {
                    questionText: 'Which module is used to handle HTTP requests in Node.js?',
                    options: ['http', 'fs', 'url', 'express'],
                    correctOption: 'http',
                },
                {
                    questionText: 'Which file is typically used to define dependencies in a Node.js project?',
                    options: ['package.json', 'node_modules', 'config.json', 'dependencies.json'],
                    correctOption: 'package.json',
                }
            ]
        },
        {
            type: 'Lecture',
            moduleName: 'Full-Stack Integration',
            lectureLink: 'www.fullstack-integration.com',
            questions: [
                {
                    questionText: 'What is the role of the backend in a full-stack application?',
                    options: [
                        'Handle client-side interactions',
                        'Manage server-side logic and database operations',
                        'Render UI components',
                        'None of the above'
                    ],
                    correctOption: 'Manage server-side logic and database operations',
                },
                {
                    questionText: 'Which tool can be used to test APIs?',
                    options: ['Postman', 'React', 'npm', 'MongoDB'],
                    correctOption: 'Postman',
                },
                {
                    questionText: 'How does React communicate with the backend?',
                    options: ['Using HTTP requests', 'Direct database access', 'Using npm', 'None of the above'],
                    correctOption: 'Using HTTP requests',
                },
                {
                    questionText: 'What is CORS?',
                    options: [
                        'A security feature that restricts resource sharing',
                        'A database management tool',
                        'A frontend library',
                        'None of the above'
                    ],
                    correctOption: 'A security feature that restricts resource sharing',
                },
                {
                    questionText: 'Which protocol is used for data communication in a MERN app?',
                    options: ['HTTP/HTTPS', 'FTP', 'SMTP', 'SSH'],
                    correctOption: 'HTTP/HTTPS',
                }
            ]
        },
        {
            type: 'Final Test',
            moduleName: 'Final Exam: MERN Stack Development',
            lectureLink: null,
            questions: [
                {
                    questionText: 'Which library is used to create the frontend in the MERN stack?',
                    options: ['React', 'Vue.js', 'Angular', 'jQuery'],
                    correctOption: 'React',
                },
                {
                    questionText: 'What is the default port for a Node.js server?',
                    options: ['3000', '8080', '5000', '80'],
                    correctOption: '3000',
                },
                {
                    questionText: 'How do you start a React development server?',
                    options: ['npm start', 'npm run dev', 'react start', 'node start'],
                    correctOption: 'npm start',
                },
                {
                    questionText: 'Which MongoDB command is used to view all databases?',
                    options: ['show dbs', 'list dbs', 'find dbs', 'show databases'],
                    correctOption: 'show dbs',
                },
                {
                    questionText: 'What is the role of Express.js in the MERN stack?',
                    options: [
                        'Manage server-side routing and middleware',
                        'Render frontend UI components',
                        'Perform database queries',
                        'All of the above'
                    ],
                    correctOption: 'Manage server-side routing and middleware',
                }
            ]
        }
    ]
};

	let formData = mernStackCourseData;
	

	// Send JSON data to servlet using fetch API
	fetch('addcourse', {
	    method: "POST",
	    headers: {
	        "Content-Type": "application/json"
	    },
	    body: JSON.stringify(formData) // Convert the formData object to a JSON string
	})
	.then(response => {
	    if (response.ok) {
	        return response.json(); // If your servlet returns a JSON response
	    } else {
	        throw new Error("Network response was not ok");
	    }
	})
	.then(data => {
			alert(data.message);
	    console.log("Response from servlet:", data);
		
	})
	.catch(error => {
			alert(error);
	    console.error("There was a problem with the fetch operation:", error);
	});
	
	return true;
}