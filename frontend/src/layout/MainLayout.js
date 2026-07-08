/**
 * layout/MainLayout.js
 * Role-aware application shell (sidebar + header), vanilla JS equivalent of
 * the React MainLayout.js component. Every authenticated page calls
 * MainLayout.mount({ active, requireRole }) on load.
 */
const MainLayout = {
  /**
   * @param {Object} opts
   * @param {string} opts.active - nav key of the current page, e.g. '/upload'
   * @param {string} [opts.requireRole] - if set, only that role may view the page
   */
  mount(opts) {
    const { active, requireRole } = opts;
    const state = authStore.getState();

    // ---- auth guard ----
    if (!state.isAuthenticated) {
      window.location.href = '../pages/LoginPage.html';
      return null;
    }
    if (requireRole && state.user?.role !== requireRole) {
      message.error("You don't have permission to view that page.");
      window.location.href = './Dashboard.html';
      return null;
    }

    const roleLabel = (state.user?.role || '').replace('ROLE_', '');
    const isReviewerOrClient = state.user?.role === 'ROLE_LEGAL_REVIEWER' || state.user?.role === 'ROLE_CLIENT';
    const isAdmin = state.user?.role === 'ROLE_ADMIN';

    const navItems = [
      { key: '/', label: 'Dashboard', icon: '01', href: './Dashboard.html' },
      ...(isReviewerOrClient ? [{ key: '/upload', label: 'Upload Contract', icon: '02', href: './UploadPage.html' }] : []),
      { key: '/compliance', label: 'Compliance', icon: '03', href: './CompliancePage.html' },
      { key: '/reports', label: 'Reports', icon: '04', href: './ReportsPage.html' },
      ...(isAdmin ? [{ key: '/admin', label: 'Admin Panel', icon: '05', href: './AdminDashboard.html' }] : [])
    ];

    const sidebarSlot = document.getElementById('sidebar-slot');
    const topbarSlot = document.getElementById('topbar-slot');

    const collapsedPref = sessionStorage.getItem('sidebarCollapsed') === '1';

    sidebarSlot.innerHTML = `
      <aside class="sidebar${collapsedPref ? ' collapsed' : ''}" id="ll-sidebar">
        <div class="brand">
          <div class="brand-mark">LL</div>
          <div class="brand-name" id="ll-brand-name">${collapsedPref ? '' : 'LegalLens'}</div>
        </div>
        <nav class="sidebar-nav">
          ${navItems.map(item => `
            <a href="${item.href}" class="${item.key === active ? 'active' : ''}" data-key="${item.key}">
              <span class="nav-icon">${item.icon}</span>
              <span class="nav-label">${item.label}</span>
            </a>
          `).join('')}
        </nav>
      </aside>
    `;

    topbarSlot.innerHTML = `
      <header class="topbar">
        <button class="toggle-btn" id="ll-toggle" aria-label="Toggle sidebar">${collapsedPref ? '»' : '«'}</button>
        <div class="who">
          <span>Logged in as <b>${state.user?.username || ''}</b> &middot; ${roleLabel}</span>
          <button class="btn-logout" id="ll-logout">Logout</button>
        </div>
      </header>
    `;

    document.getElementById('ll-toggle').addEventListener('click', () => {
      const sb = document.getElementById('ll-sidebar');
      const collapsed = sb.classList.toggle('collapsed');
      document.getElementById('ll-brand-name').textContent = collapsed ? '' : 'LegalLens';
      document.getElementById('ll-toggle').textContent = collapsed ? '»' : '«';
      sessionStorage.setItem('sidebarCollapsed', collapsed ? '1' : '0');
    });

    document.getElementById('ll-logout').addEventListener('click', () => {
      authStore.logout();
      window.location.href = '../pages/LoginPage.html';
    });

    return state;
  }
};
