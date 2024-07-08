document.getElementById('loginSwitch').addEventListener('click', function () {
    document.getElementById('loginFormContainer').style.display = 'block';
    document.getElementById('registerFormContainer').style.display = 'none';
    this.classList.add('active');
    document.getElementById('registerSwitch').classList.remove('active');
});

document.getElementById('registerSwitch').addEventListener('click', function () {
    document.getElementById('loginFormContainer').style.display = 'none';
    document.getElementById('registerFormContainer').style.display = 'block';
    this.classList.add('active');
    document.getElementById('loginSwitch').classList.remove('active');
});

document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    fetch('http://localhost:8080/user/login', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa(username + ':' + password)
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.username) {
                localStorage.setItem('username', username);
                localStorage.setItem('password', password); // Store password
                localStorage.setItem('authorities', JSON.stringify(data.authorities));
                window.location.href = '../game page/game.html'; // Redirect to the next page
            } else {
                document.getElementById('loginMessage').textContent = 'Login failed. Please check your credentials.';
            }
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const username = document.getElementById('registerUsername').value;
    const password = document.getElementById('registerPassword').value;

    fetch('http://localhost:8080/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username: username, password: password })
    })
        .then(response => {
            if (response.status === 409) {
                throw new Error('Username already taken');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('registerMessage').textContent = 'Registration successful. Please login with your new credentials.';
        })
        .catch(error => {
            if (error.message === 'Username already taken') {
                document.getElementById('registerMessage').textContent = 'Registration failed. Username already taken.';
            } else {
                console.error('Error:', error);
            }
        });
});
