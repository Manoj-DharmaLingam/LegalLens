# LegalLens — Frontend (HTML5 / CSS3 / Vanilla JS)

A frontend-only build of the LegalLens contract compliance analyzer, built strictly
with HTML5, CSS3, and vanilla JavaScript (no React, no Redux, no build step).
Axios (via CDN) is the only external dependency, used exactly as the SRS specifies.

There is **no backend** here — every service call targets
`http://localhost:8080/api`, matching the Spring Boot API described in the SRS.
Run the LegalLens backend locally (or point `src/services/api.js`'s `baseURL`
at a different host) to see live data.

## Folder structure (mirrors the SRS)

```
frontend/
├── index.html                  entry point → redirects to Login or Dashboard
└── src/
    ├── css/
    │   └── main.css            design tokens + all component styles
    ├── js/
    │   ├── toast.js            message.success / message.error / message.info
    │   └── render-helpers.js   seal/badge/progress-bar/date rendering helpers
    ├── layout/
    │   └── MainLayout.js       sidebar + header shell, auth guard, role guard
    ├── store/
    │   └── authStore.js        vanilla replacement for the Redux authSlice
    │                           (same state shape: { user, token, isAuthenticated })
    ├── services/                one file per Axios service, matching the SRS 1:1
    │   ├── api.js               axios instance + Bearer token interceptor
    │   ├── authService.js
    │   ├── contractService.js
    │   ├── clauseService.js
    │   ├── commentService.js
    │   ├── complianceService.js
    │   ├── reportService.js
    │   └── adminService.js
    └── pages/                   one HTML + one JS file per page
        ├── LoginPage.html/.js
        ├── RegisterPage.html/.js
        ├── Dashboard.html/.js
        ├── UploadPage.html/.js
        ├── ContractDetails.html/.js
        ├── CompliancePage.html/.js
        ├── ComplianceDashboard.html/.js
        ├── ReportsPage.html/.js
        └── AdminDashboard.html/.js
```

## Running it

Just open `index.html` in a browser, or serve the folder statically:

```bash
npx serve frontend
# or
python3 -m http.server --directory frontend 5500
```

## Design notes

The visual language is a "case file" concept: an ink-blue sidebar/shell around
parchment-colored document cards, with a circular **compliance seal** — a
notarial-stamp progress ring — as the signature element wherever a compliance
score appears (dashboard table, contract details, compliance dashboard).
Risk levels read like a notary's marks: sage for low risk/approved, gold for
medium/caution, sealing-wax red for high risk/rejected.

## Auth & state

`authStore.js` reproduces the exact behavior specified for the Redux
`authSlice`: `loginSuccess({ user, token })` and `logout()` persist to and
clear `localStorage` (`token`, `user`), and every authenticated page calls
`MainLayout.mount()` on load, which redirects to the login page if no token
is present, and enforces `ROLE_ADMIN`-only pages (Admin Panel).
