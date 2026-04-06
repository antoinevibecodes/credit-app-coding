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

// ==================== MOCK: DRIVERS & CARDS ====================

const MOCK_DRIVERS = [
    {
        DriverId: 10000, FirstName: 'John', LastName: 'Smith', DateOfBirth: '2015-06-15',
        PhotoUrl: null, IsAnonymous: false, RfidUidHex: 'a1b2c3d4',
        Entitlements: [
            { EntitlementTypeId: 1, EntitlementTypeName: 'General', Credits: 45 },
            { EntitlementTypeId: 4, EntitlementTypeName: 'TinyTrack', Credits: 20 },
            { EntitlementTypeId: 5, EntitlementTypeName: 'Train', Credits: 12 },
            { EntitlementTypeId: 6, EntitlementTypeName: 'Arcade', Credits: 8 }
        ]
    },
    {
        DriverId: 10001, FirstName: 'Jane', LastName: 'Doe', DateOfBirth: '2016-03-22',
        PhotoUrl: null, IsAnonymous: false, RfidUidHex: 'b2c3d4e5',
        Entitlements: [
            { EntitlementTypeId: 1, EntitlementTypeName: 'General', Credits: 30 },
            { EntitlementTypeId: 4, EntitlementTypeName: 'TinyTrack', Credits: 10 },
            { EntitlementTypeId: 5, EntitlementTypeName: 'Train', Credits: 6 }
        ]
    },
    {
        DriverId: 10002, FirstName: 'Mike', LastName: 'Wilson', DateOfBirth: '2014-11-08',
        PhotoUrl: null, IsAnonymous: false, RfidUidHex: 'c3d4e5f6',
        Entitlements: [
            { EntitlementTypeId: 1, EntitlementTypeName: 'General', Credits: 60 },
            { EntitlementTypeId: 4, EntitlementTypeName: 'TinyTrack', Credits: 35 },
            { EntitlementTypeId: 6, EntitlementTypeName: 'Arcade', Credits: 15 }
        ]
    },
    {
        DriverId: 10003, FirstName: 'Sarah', LastName: 'Lee', DateOfBirth: '2017-01-30',
        PhotoUrl: null, IsAnonymous: false, RfidUidHex: 'd4e5f6a7',
        Entitlements: [
            { EntitlementTypeId: 1, EntitlementTypeName: 'General', Credits: 25 },
            { EntitlementTypeId: 5, EntitlementTypeName: 'Train', Credits: 9 }
        ]
    }
];

const MOCK_STANDARD_CARDS = [
    { Id: 1, RfidUidHex: 'ff000001', Label: 'Birthday Card #1', CardType: 'birthday', DefaultCredits: 20, EntitlementTypeId: 1, EntitlementTypeName: 'General', IsActive: true },
    { Id: 2, RfidUidHex: 'ff000002', Label: 'Birthday Card #2', CardType: 'birthday', DefaultCredits: 20, EntitlementTypeId: 1, EntitlementTypeName: 'General', IsActive: true },
    { Id: 3, RfidUidHex: 'ff000003', Label: 'Guest Pass #1', CardType: 'guest', DefaultCredits: 10, EntitlementTypeId: 1, EntitlementTypeName: 'General', IsActive: true },
    { Id: 4, RfidUidHex: 'ff000004', Label: 'Event Card #1', CardType: 'event', DefaultCredits: 30, EntitlementTypeId: 1, EntitlementTypeName: 'General', IsActive: false }
];

let mockStandardCardNextId = 100;

const MOCK_KIOSK_OPTIONS = [
    { Id: 1, Name: '10 Track Rides', Description: 'Good for 10 rides on the traffic track', CreditAmount: 50, PriceDisplay: '$25.00', EntitlementTypeId: 4, IsActive: true },
    { Id: 2, Name: '5 Train Rides', Description: '5 rides on the Tiny Towne train', CreditAmount: 15, PriceDisplay: '$12.00', EntitlementTypeId: 5, IsActive: true },
    { Id: 3, Name: '20 Arcade Tokens', Description: '20 tokens for all arcade games', CreditAmount: 20, PriceDisplay: '$15.00', EntitlementTypeId: 6, IsActive: true },
    { Id: 4, Name: 'Unlimited Day Pass', Description: 'Unlimited rides on all attractions for one day', CreditAmount: 999, PriceDisplay: '$45.00', EntitlementTypeId: 1, IsActive: true },
    { Id: 5, Name: 'General 50 Credits', Description: '50 general credits usable anywhere', CreditAmount: 50, PriceDisplay: '$30.00', EntitlementTypeId: 1, IsActive: true }
];

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

// ----- Driver Info -----

async function fetchDriverInfo(query) {
    if (CONFIG.USE_MOCK) {
        let driver = null;
        if (query.HasDriverId) {
            driver = MOCK_DRIVERS.find(d => d.DriverId === query.DriverId);
        } else if (query.HasRfidUidHex) {
            driver = MOCK_DRIVERS.find(d => d.RfidUidHex === query.RfidUidHex.toLowerCase());
        }
        if (driver) {
            return { ok: true, data: JSON.parse(JSON.stringify(driver)) };
        }
        return { ok: false, message: 'Driver not found' };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/Entitlements/GetDriver', {
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

async function fetchDriverEntitlements(driverId) {
    if (CONFIG.USE_MOCK) {
        const driver = MOCK_DRIVERS.find(d => d.DriverId === driverId);
        if (driver) {
            return { ok: true, data: driver.Entitlements };
        }
        return { ok: false, message: 'Driver not found' };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/entitlements/getdriverentitlements', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify({ DriverId: driverId })
        });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

// ----- Charge Card (web) -----

async function chargeCard(driverId, buttonId, buttonName, creditAmount, entitlementTypeId) {
    if (CONFIG.USE_MOCK) {
        const driver = MOCK_DRIVERS.find(d => d.DriverId === driverId);
        if (!driver) return { ok: false, message: 'Driver not found' };

        const ent = driver.Entitlements.find(e => e.EntitlementTypeId === entitlementTypeId);
        if (ent) {
            ent.Credits = Math.max(0, ent.Credits - creditAmount);
        }
        return { ok: true, data: { Success: true, Message: creditAmount + ' credits deducted', RemainingCredits: ent ? ent.Credits : 0 } };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/entitlements/chargecard', {
            method: 'POST',
            headers: apiHeaders(),
            body: JSON.stringify({ DriverId: driverId, ChargeButtonId: buttonId, ButtonName: buttonName, CreditAmount: creditAmount, EntitlementTypeId: entitlementTypeId })
        });
        const data = await res.json();
        return { ok: data.Success !== false, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

// ----- Standard Cards -----

async function fetchStandardCards() {
    if (CONFIG.USE_MOCK) {
        return { ok: true, data: JSON.parse(JSON.stringify(MOCK_STANDARD_CARDS)) };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/standardcards/GetAll', { headers: apiHeaders() });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function createStandardCard(card) {
    if (CONFIG.USE_MOCK) {
        card.Id = mockStandardCardNextId++;
        MOCK_STANDARD_CARDS.push(card);
        return { ok: true, data: { Id: card.Id, Success: true } };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/standardcards/Create', {
            method: 'POST', headers: apiHeaders(), body: JSON.stringify(card)
        });
        const data = await res.json();
        return { ok: data.Success, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function updateStandardCard(card) {
    if (CONFIG.USE_MOCK) {
        const idx = MOCK_STANDARD_CARDS.findIndex(c => c.Id === card.Id);
        if (idx >= 0) MOCK_STANDARD_CARDS[idx] = { ...MOCK_STANDARD_CARDS[idx], ...card };
        return { ok: true, data: { Success: true } };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/standardcards/Update', {
            method: 'POST', headers: apiHeaders(), body: JSON.stringify(card)
        });
        const data = await res.json();
        return { ok: data.Success, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function deleteStandardCard(id) {
    if (CONFIG.USE_MOCK) {
        const idx = MOCK_STANDARD_CARDS.findIndex(c => c.Id === id);
        if (idx >= 0) MOCK_STANDARD_CARDS.splice(idx, 1);
        return { ok: true };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/standardcards/Delete', {
            method: 'POST', headers: apiHeaders(), body: JSON.stringify({ Id: id })
        });
        const data = await res.json();
        return { ok: data.Success };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

// ----- Kiosk -----

async function fetchKioskOptions() {
    if (CONFIG.USE_MOCK) {
        return { ok: true, data: MOCK_KIOSK_OPTIONS.filter(o => o.IsActive) };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/kiosk/GetPurchaseOptions', {
            method: 'POST', headers: apiHeaders(), body: '{}'
        });
        const data = await res.json();
        return { ok: true, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}

async function processKioskPurchase(rfidUidL, purchaseOptionId, paymentMethod) {
    if (CONFIG.USE_MOCK) {
        const option = MOCK_KIOSK_OPTIONS.find(o => o.Id === purchaseOptionId);
        if (!option) return { ok: false, message: 'Invalid option' };
        return { ok: true, data: { Success: true, Message: option.CreditAmount + ' credits added', NewBalance: 999 } };
    }
    try {
        const res = await fetch(CONFIG.API_BASE_URL + '/kiosk/ProcessPurchase', {
            method: 'POST', headers: apiHeaders(),
            body: JSON.stringify({ RfidUidL: rfidUidL, PurchaseOptionId: purchaseOptionId, PaymentMethod: paymentMethod })
        });
        const data = await res.json();
        return { ok: data.Success, data: data };
    } catch (err) {
        return { ok: false, message: err.message };
    }
}
