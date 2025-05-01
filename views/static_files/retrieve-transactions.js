// Fetch data from the servlet when the page loads
window.addEventListener('load', () => {
    // Fetch data from the servlet (make sure the URL is correct)
    fetch('retrievetransactions')
        .then(response => {
            // Check if the response is successful
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json(); // Parse the JSON response
        })
        .then(data => {
            console.log(data); // Log the received data
            displayTransactions(data); 
        })
        .catch(error => {
            console.error('Error fetching transactions:', error);
        });
});

// Function to display the transactions in the console (or modify for your use case)

function displayTransactions(transactions) {
    // Check if there are any transactions
    if (!Array.isArray(transactions) || transactions.length === 0) {
        console.log("No transactions found.");
				document.querySelector('.transactions-container').innerHTML = `
				<div class='no-transaction'>
				  <h1>You haven't done any transactions yet!</h1>
				  <p>Explore courses using search bar and enroll into your favourite courses.</p>
				</div>
				`;
        return;
    }

    // Create a table and its header
    let table = `<table class="transaction-table">
                    <thead>
                        <tr>
                            <th>Transaction ID</th>
                            <th>Amount</th>
                            <th>Course ID</th>
														<th>Details</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>`;

    // Iterate through the transactions and generate table rows
    transactions.forEach(transaction => {
        table += `<tr>
                    <td>${transaction.transactionid}</td>
                    <td>${transaction.amount}</td>
                    <td>${transaction.courseid}</td>
										<td>${transaction.details}</td>
                    <td>${transaction.time_date}</td>
                  </tr>`;
    });

    // Close the table tag
    table += `</tbody></table>`;

    // Insert the table into the div with class 'transaction-container'
    document.querySelector('.transactions-container').innerHTML = table;
}
