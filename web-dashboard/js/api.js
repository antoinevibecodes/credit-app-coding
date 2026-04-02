// Shared API client — handles both mock and real API calls

function apiHeaders() {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }
    return headers;
}

// ==================== MOCK DATA ====================

const MOCK_BUTTONS = [
    { Id: 1, Name: 'Track', EntitlementTypeId: 4, EntitlementTypeName: 'TinyTrack', CreditAmount: 5, IsForceOption: false, IsActive: true, SortOrder: 1, ColorHex: '#e3a21a' },
    { Id: 2, Name: 'Track Force', EntitlementTypeId: 4, EntitlementTypeName: 'TinyTrack', CreditAmount: 5, IsForceOption: true, IsActive: true, SortOrder: 2, ColorHex: '#00aba9' },
    { Id: 3, Name: 'Train', EntitlementTypeId: 5, EntitlementTypeName: 'Train', CreditAmount: 3, IsForceOption: false, IsActive: true, SortOrder: 3, ColorHex: '#00aba9' },
    { Id: 4, Name: 'Train Force', EntitlementTypeId: 5, EntitlementTypeName: 'Train', CreditAmount: 3, IsForceOption: true, IsActive: true, SortOrder: 4, ColorHex: '#e3a21a' },
    { Id: 5, Name: 'Arcade', EntitlementTypeId: 6, EntitlementTypeName: 'Arcade', CreditAmount: 2, IsForceOption: false, IsActive: true, SortOrder: 5, ColorHex: '#4a90d9' },
    { Id: 6, Name: 'Bumper Cars', EntitlementTypeId: 4, EntitlementTypeName: 'TinyTrack', CreditAmount: 4, IsForceOption: false, IsActive: true, SortOrder: 6, ColorHex: '#d94a4a' }
];

const MOCK_ENTITLEMENT_TYPES = [
    { Id: 1, Name: 'General' },
    { Id: 2, Name: 'Food' },
    { Id: 3, Name: 'TrafficTrack' },
    { Id: 4, Name: 'TinyTrack' },
    { Id: 5, Name: 'Train' },
    { Id: 6, Name: 'Arcade' },
    { Id: 7, Name: 'MemberAdvantages' },
    { Id: 8, Name: 'TransferCredits' }
];

let mockButtonNextId = 100;

function generateMockTransactions() {
    const buttons = ['Track', 'Train', 'Arcade', 'Bumper Cars', 'Track Force'];
    const types = ['TinyTrack', 'Train', 'Arcade', 'TinyTrack', 'TinyTrack'];
    const amounts = [5, 3, 2, 4, 5];
    const names = ['John Smith', 'Jane Doe', 'Mike Wilson', 'Sarah Lee', 'Anonymous'];
    const transactions = [];

    for (let i = 0; i < 20; i++) {
        const idx = i % 5;
        const date = new Date();
        date.setHours(date.getHours() - i);

        transactions.push({
            TransactionId: 5000 + i,
            DriverId: idx === 4 ? 0 : 10000 + idx,
            DriverName: names[idx],
            IsAnonymous: idx === 4,
            ButtonName: buttons[idx],
            EntitlementTypeName: types[idx],
            CreditAmount: -amounts[idx],
            Timestamp: date.toISOString().slice(0, 19),
            TransactionType: 'Charge'
        });
    }
    return transactions;
}

function generateMockDailyReport(date) {
    return {
        Date: date,
        TotalTransactions: 312,
        TotalCreditsUsed: 1547,
        Activities: [
            { ButtonName: 'Track', EntitlementTypeName: 'TinyTrack', UsageCount: 47, TotalCreditsUsed: 235 },
            { ButtonName: 'Train', EntitlementTypeName: 'Train', UsageCount: 33, TotalCreditsUsed: 99 },
            { ButtonName: 'Arcade', EntitlementTypeName: 'Arcade', UsageCount: 65, TotalCreditsUsed: 130 },
            { ButtonName: 'Bumper Cars', EntitlementTypeName: 'TinyTrack', UsageCount: 28, TotalCreditsUsed: 112 },
            { ButtonName: 'Track Force', EntitlementTypeName: 'TinyTrack', UsageCount: 15, TotalCreditsUsed: 75 }
        ]
    };
}

// ==================== API FUNCTIONS ====================

// ----- Charge Buttons -----

async function fetchChargeButtons() {
    if (CONFIG.USE_MOCK) {
        return { ok: true, data: JSON.parse(JSON.stringify(MOCK_BUTTONS)) };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/chargebuttons/GetAll?includeInactive=true', {
            headers: apiHeaders()
        });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function createChargeButton(button) {
    if (CONFIG.USE_MOCK) {
        button.Id = mockButtonNextId++;
        MOCK_BUTTONS.push(button);
        return { ok: true, data: { Id: button.Id, Success: true } };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/chargebuttons/Create', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify(button)
        });
        const data = await res.json();
        return { ok: data.Success, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function updateChargeButton(button) {
    if (CONFIG.USE_MOCK) {
        const idx = MOCK_BUTTONS.findIndex(b => b.Id === button.Id);
        if (idx >= 0) MOCK_BUTTONS[idx] = { ...MOCK_BUTTONS[idx], ...button };
        return { ok: true, data: { Success: true } };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/chargebuttons/Update', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify(button)
        });
        const data = await res.json();
        return { ok: data.Success, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function deleteChargeButton(id) {
    if (CONFIG.USE_MOCK) {
        const idx = MOCK_BUTTONS.findIndex(b => b.Id === id);
        if (idx >= 0) MOCK_BUTTONS.splice(idx, 1);
        return { ok: true };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/chargebuttons/Delete', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify({ Id: id })
        });
        const data = await res.json();
        return { ok: data.Success };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

// ----- Entitlement Types -----

async function fetchEntitlementTypes() {
    if (CONFIG.USE_MOCK) {
        return { ok: true, data: MOCK_ENTITLEMENT_TYPES };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/entitlementtypes/GetAll', {
            headers: apiHeaders()
        });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

// ----- Reports -----

async function fetchTransactionHistory(query) {
    if (CONFIG.USE_MOCK) {
        const txns = generateMockTransactions();
        return {
            ok: true,
            data: { TotalCount: txns.length, PageNumber: 1, PageSize: 50, Transactions: txns }
        };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/reports/GetTransactionHistory', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify(query)
        });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function fetchDailyReport(date) {
    if (CONFIG.USE_MOCK) {
        return { ok: true, data: generateMockDailyReport(date) };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/reports/GetDailyReport', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify({ Date: date })
        });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}
