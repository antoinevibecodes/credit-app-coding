// Authentication helper

function isLoggedIn() {
    return sessionStorage.getItem(CONFIG.AUTH_TOKEN_KEY) !== null;
}

function getToken() {
    return sessionStorage.getItem(CONFIG.AUTH_TOKEN_KEY);
}

function logout() {
    sessionStorage.removeItem(CONFIG.AUTH_TOKEN_KEY);
    sessionStorage.removeItem(CONFIG.AUTH_ROLE_KEY);
    window.location.href = 'index.html';
}

function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = 'index.html';
    }
}

async function login(pin) {
    if (CONFIG.USE_MOCK) {
        // Mock: accept "1234"
        if (pin === '1234') {
            sessionStorage.setItem(CONFIG.AUTH_TOKEN_KEY, 'mock-token-abc123');
            sessionStorage.setItem(CONFIG.AUTH_ROLE_KEY, 'admin');
            return { success: true };
        }
        return { success: false, message: 'Invalid PIN' };
    }

    try {
        const response = await fetch(CONFIG.API_BASE_URL + '/auth/ValidatePin', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ Pin: pin, Role: 'admin' })
        });
        const data = await response.json();
        if (data.Valid) {
            sessionStorage.setItem(CONFIG.AUTH_TOKEN_KEY, data.Token);
            sessionStorage.setItem(CONFIG.AUTH_ROLE_KEY, 'admin');
            return { success: true };
        }
        return { success: false, message: 'Invalid PIN' };
    } catch (err) {
        return { success: false, message: 'Connection error: ' + err.message };
    }
}
