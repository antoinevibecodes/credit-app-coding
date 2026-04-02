# API Specification — New Endpoints for .NET Backend

**For:** Backend developer  
**Base URL:** `http://ganottwebapi1.tinytowne.com/api/`  
**NOTE:** Existing endpoints (GetDriver, chargecard, getdriverentitlements, GetScaledDriverPicture, UploadDriverPicture) must remain unchanged.

---

## Database Changes Needed

### New Tables

**1. ChargeButtons**
| Column | Type | Notes |
|--------|------|-------|
| Id | INT PK IDENTITY | Auto-increment |
| Name | NVARCHAR(100) | Button display name |
| EntitlementTypeId | INT FK | References entitlement types |
| CreditAmount | INT | Credits to deduct per use |
| IsForceOption | BIT | Bypass credit check |
| IsActive | BIT | Show/hide button |
| SortOrder | INT | Display order |
| ColorHex | NVARCHAR(7) | Button color, e.g. #e3a21a |
| CreatedDate | DATETIME | Default GETDATE() |
| ModifiedDate | DATETIME | Updated on edit |

**2. KioskPurchaseOptions**
| Column | Type | Notes |
|--------|------|-------|
| Id | INT PK IDENTITY | Auto-increment |
| Name | NVARCHAR(100) | Package name |
| Description | NVARCHAR(500) | What's included |
| CreditAmount | INT | Credits added on purchase |
| PriceDisplay | NVARCHAR(20) | e.g. "$25.00" |
| EntitlementTypeId | INT FK | Which credit type |
| IsActive | BIT | Show/hide option |

**3. AuthPins**
| Column | Type | Notes |
|--------|------|-------|
| Id | INT PK IDENTITY | |
| PinHash | NVARCHAR(256) | Hashed PIN |
| Role | NVARCHAR(20) | "admin" or "staff" |

**4. Modify Existing Transactions Table**
- Add `ChargeButtonId` (INT, nullable) — references ChargeButtons.Id
- Add `ButtonName` (NVARCHAR(100)) — snapshot of button name at time of charge

---

## New API Endpoints

### 1. Charge Button Management

#### GET `/api/chargebuttons/GetAll`
Returns all charge buttons. Add `?includeInactive=true` for admin panel.

**Response:**
```json
[
  {
    "Id": 1,
    "Name": "Track",
    "EntitlementTypeId": 4,
    "EntitlementTypeName": "TinyTrack",
    "CreditAmount": 5,
    "IsForceOption": false,
    "IsActive": true,
    "SortOrder": 1,
    "ColorHex": "#e3a21a"
  }
]
```

#### POST `/api/chargebuttons/Create`
**Request:**
```json
{
  "Name": "Bumper Cars",
  "EntitlementTypeId": 4,
  "CreditAmount": 3,
  "IsForceOption": false,
  "IsActive": true,
  "SortOrder": 5,
  "ColorHex": "#00aba9"
}
```
**Response:** `{ "Id": 9, "Success": true, "Message": "" }`

#### POST `/api/chargebuttons/Update`
Same as Create, with `Id` field included.  
**Response:** `{ "Success": true, "Message": "" }`

#### POST `/api/chargebuttons/Delete`
**Request:** `{ "Id": 9 }`  
**Response:** `{ "Success": true, "Message": "" }`

---

### 2. Entitlement Types

#### GET `/api/entitlementtypes/GetAll`
**Response:**
```json
[
  { "Id": 1, "Name": "General" },
  { "Id": 2, "Name": "Food" },
  { "Id": 3, "Name": "TrafficTrack" },
  { "Id": 4, "Name": "TinyTrack" },
  { "Id": 5, "Name": "Train" },
  { "Id": 6, "Name": "Arcade" },
  { "Id": 7, "Name": "MemberAdvantages" },
  { "Id": 8, "Name": "TransferCredits" }
]
```

---

### 3. Reporting

#### POST `/api/reports/GetTransactionHistory`
**Request:**
```json
{
  "HasDriverId": true,
  "DriverId": 12345,
  "HasRfidUidL": false,
  "RfidUidL": 0,
  "StartDate": "2026-01-01T00:00:00",
  "EndDate": "2026-04-02T23:59:59",
  "EntitlementTypeId": null,
  "PageNumber": 1,
  "PageSize": 50
}
```
**Response:**
```json
{
  "TotalCount": 142,
  "PageNumber": 1,
  "PageSize": 50,
  "Transactions": [
    {
      "TransactionId": 5001,
      "DriverId": 12345,
      "DriverName": "John Smith",
      "IsAnonymous": false,
      "ButtonName": "Track",
      "EntitlementTypeName": "TinyTrack",
      "CreditAmount": -5,
      "Timestamp": "2026-04-01T14:30:00",
      "TransactionType": "Charge"
    }
  ]
}
```

#### POST `/api/reports/GetDailyReport`
**Request:**
```json
{ "Date": "2026-04-02" }
```
**Response:**
```json
{
  "Date": "2026-04-02",
  "TotalTransactions": 312,
  "TotalCreditsUsed": 1547,
  "Activities": [
    {
      "ButtonName": "Track",
      "EntitlementTypeName": "TinyTrack",
      "UsageCount": 47,
      "TotalCreditsUsed": 235
    }
  ]
}
```

---

### 4. Kiosk

#### POST `/api/kiosk/GetPurchaseOptions`
**Request:** `{}` (empty body)  
**Response:**
```json
[
  {
    "Id": 1,
    "Name": "10 Track Rides",
    "Description": "Good for 10 rides on the traffic track",
    "CreditAmount": 50,
    "PriceDisplay": "$25.00",
    "EntitlementTypeId": 4
  }
]
```

#### POST `/api/kiosk/ProcessPurchase`
**Request:**
```json
{
  "RfidUidL": 12345,
  "PurchaseOptionId": 1,
  "PaymentMethod": "card"
}
```
**Response:** `{ "Success": true, "Message": "50 credits added", "NewBalance": 200 }`

---

### 5. Authentication

#### POST `/api/auth/ValidatePin`
**Request:** `{ "Pin": "1234", "Role": "admin" }`  
**Response:** `{ "Valid": true, "Token": "abc123" }`

The token is a server-generated GUID. Subsequent admin requests include: `Authorization: Bearer abc123`

---

## CORS Configuration (for Web Dashboard)

If the web dashboard is hosted on a different domain, add to Web.config or via code:
```
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
```

---

## Existing Endpoints (DO NOT MODIFY)

1. `POST /api/Entitlements/GetDriver`
2. `POST /api/entitlements/chargecard`
3. `POST /api/entitlements/getdriverentitlements`
4. `GET /api/transactions/GetScaledDriverPicture?DriverId=[id]`
5. `POST /api/admission/UploadDriverPicture`

---

## Mock Mode

The Android app includes a built-in mock server for testing. In **debug builds**, `USE_MOCK_SERVER` is `true` and the app uses fake data. In **release builds**, it connects to the real API.

To test with real endpoints:
1. Set `USE_MOCK_SERVER` to `false` in `app/build.gradle` defaultConfig
2. Build and deploy
