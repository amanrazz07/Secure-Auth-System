// Utility to switch between tabs
function switchTab(tab) {
    // Update buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    // Update forms
    document.querySelectorAll('.auth-form').forEach(form => {
        form.classList.remove('active-form');
    });
    document.getElementById(`${tab}-form`).classList.add('active-form');
    
    // Clear notifications
    closeNotification();
}

// Show notification
function showNotification(message, type) {
    const notif = document.getElementById('notification');
    const notifMsg = document.getElementById('notif-message');
    
    notif.className = `notification ${type}`;
    notifMsg.textContent = message;
    
    // Auto hide after 5 seconds
    setTimeout(closeNotification, 5000);
}

// Close notification
function closeNotification() {
    const notif = document.getElementById('notification');
    notif.classList.add('hidden');
}

// Toggle password visibility
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const btn = input.nextElementSibling;
    
    if (input.type === 'password') {
        input.type = 'text';
        btn.textContent = 'Hide';
    } else {
        input.type = 'password';
        btn.textContent = 'Show';
    }
}

// Handle Login Form Submission
async function handleLogin(e) {
    e.preventDefault();
    
    if (window.location.protocol === 'file:') {
        showNotification('Error: Please open http://localhost:8080 in your browser, do not open the file directly.', 'error');
        return;
    }
    
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    const btn = document.getElementById('login-btn');
    const btnText = btn.querySelector('.btn-text');
    const loader = btn.querySelector('.loader');
    
    // UI state: loading
    btnText.classList.add('hidden');
    loader.classList.remove('hidden');
    btn.disabled = true;
    closeNotification();
    
    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            // Show dashboard
            document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active-form'));
            document.querySelector('.tabs').classList.add('hidden');
            
            const dashboard = document.getElementById('dashboard');
            dashboard.classList.add('active-form');
            document.getElementById('welcome-message').textContent = `Welcome back, ${username}!`;
            
            showNotification('Login successful!', 'success');
            
            // Clear form
            document.getElementById('login-form').reset();
        } else {
            showNotification(data.message || 'Invalid credentials.', 'error');
        }
    } catch (error) {
        showNotification('Server connection failed.', 'error');
    } finally {
        // Reset UI state
        btnText.classList.remove('hidden');
        loader.classList.add('hidden');
        btn.disabled = false;
    }
}

// Handle Registration Form Submission
async function handleRegister(e) {
    e.preventDefault();
    
    if (window.location.protocol === 'file:') {
        showNotification('Error: Please open http://localhost:8080 in your browser, do not open the file directly.', 'error');
        return;
    }
    
    const username = document.getElementById('reg-username').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    
    const btn = document.getElementById('reg-btn');
    const btnText = btn.querySelector('.btn-text');
    const loader = btn.querySelector('.loader');
    
    // UI state: loading
    btnText.classList.add('hidden');
    loader.classList.remove('hidden');
    btn.disabled = true;
    closeNotification();
    
    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            showNotification('Registration successful! Please log in.', 'success');
            
            // Switch to login tab and prefill username
            document.getElementById('register-form').reset();
            switchTab('login');
            document.querySelector('.tab-btn:first-child').classList.add('active');
            document.getElementById('login-username').value = username;
        } else {
            showNotification(data.message || 'Registration failed.', 'error');
        }
    } catch (error) {
        showNotification('Server connection failed.', 'error');
    } finally {
        // Reset UI state
        btnText.classList.remove('hidden');
        loader.classList.add('hidden');
        btn.disabled = false;
    }
}

// Handle Logout
function logout() {
    // Hide dashboard, show login
    document.getElementById('dashboard').classList.remove('active-form');
    document.querySelector('.tabs').classList.remove('hidden');
    
    // Switch to login tab
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelector('.tab-btn:first-child').classList.add('active');
    
    document.getElementById('login-form').classList.add('active-form');
    
    showNotification('You have been logged out.', 'success');
}
