function renderTransactionForm(type = 'debit') {
    const formContainer = document.getElementById('transaction-form');
    formContainer.innerHTML = ''; // Clear any existing form

    if (type === 'debit' || type === 'credit') {
        formContainer.innerHTML = `
            <h2>${type === 'debit' ? 'Debit Card' : 'Credit Card'} Payment</h2>
            <form id="payment-form">
                <label for="card-number">Card Number:</label>
                <input type="text" id="card-number" name="card-number" required>
                <br><br>
                <label for="expiry">Expiry Date:</label>
                <input type="month" id="expiry" name="expiry" required>
                <br><br>
                <label for="cvv">CVV:</label>
                <input type="password" id="cvv" name="cvv" required>
                <br><br>
                <button type="button" class="submit-button">Pay</button>
            </form>
        `;
    } else if (type === 'upi') {
        formContainer.innerHTML = `
            <h2>UPI Payment</h2>
            <form id="payment-form">
                <label for="upi-id">UPI ID:</label>
                <input type="text" id="upi-id" name="upi-id" required>
                <br><br>
                <button type="button" class="submit-button">Pay</button>
            </form>
        `;
    }

    const payButton = document.querySelector('.submit-button');
    payButton.addEventListener('click', handlePayment);
}

function handlePayment() {
    // Get course IDs from the current URL
    const urlParams = new URLSearchParams(window.location.search);
    const courseIds = urlParams.get('courseids');

    if (!courseIds) {
        alert('No course IDs found in the URL.');
        console.error('No course IDs found in the URL.');
        return;
    }

    // Prepare POST request payload
    const formData = new URLSearchParams();
    formData.append('courseids', courseIds);

    // Send POST request to the servlet
    fetch('http://localhost:7910/Skillbuilders-2/addtoenrolled', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData,
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server responded with status ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            alert(data.message);
            console.log(data.message);
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Failed to enroll courses. Please try again later.');
        });
}

// Initialize the default form as Debit Card
renderTransactionForm('debit');