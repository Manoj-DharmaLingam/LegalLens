/**
 * pages/Dashboard.js
 */
(function () {
  const state = MainLayout.mount({ active: '/' });
  if (!state) return;

  const role = state.user?.role;
  document.getElementById('dash-eyebrow').textContent = role ? role.replace('ROLE_', '') + ' overview' : 'Overview';

  function renderStatCards(contracts) {
    const wrap = document.getElementById('stat-cards');
    if (role === 'ROLE_ADMIN') {
      const total = contracts.length;
      const approved = contracts.filter(c => c.status === 'APPROVED').length;
      const rejected = contracts.filter(c => c.status === 'REJECTED').length;
      const scored = contracts.filter(c => typeof c.complianceScore === 'number');
      const avg = scored.length ? Math.round(scored.reduce((a, c) => a + c.complianceScore, 0) / scored.length) : 0;
      wrap.innerHTML = `
        ${statCard('Total Contracts', total, '')}
        ${statCard('Approved', approved, 'accent-sage')}
        ${statCard('Rejected', rejected, 'accent-red')}
        ${statCard('Avg Compliance', avg + '%', 'accent-gold')}
      `;
      wrap.className = 'grid grid-4';
    } else if (role === 'ROLE_LEGAL_REVIEWER') {
      const pending = contracts.filter(c => c.status === 'UNDER_REVIEW').length;
      const highRisk = contracts.filter(c => (c.complianceScore ?? 100) < 50).length;
      const recent = contracts.filter(c => {
        if (!c.uploadDate) return false;
        const days = (Date.now() - new Date(c.uploadDate).getTime()) / 86400000;
        return days <= 7;
      }).length;
      wrap.innerHTML = `
        ${statCard('Pending Review', pending, 'accent-gold')}
        ${statCard('High Risk', highRisk, 'accent-red')}
        ${statCard('Recent Uploads', recent, 'accent-sage')}
      `;
      wrap.className = 'grid grid-3';
    } else {
      const mine = contracts.length;
      const approved = contracts.filter(c => c.status === 'APPROVED').length;
      wrap.innerHTML = `
        ${statCard('My Contracts', mine, '')}
        ${statCard('Approved Docs', approved, 'accent-sage')}
      `;
      wrap.className = 'grid grid-2';
    }
  }

  function statCard(label, value, accentClass) {
    return `
      <div class="stat-card ${accentClass}">
        <div class="stat-label">${label}</div>
        <div class="stat-value">${value}</div>
      </div>
    `;
  }

  function renderTable(contracts) {
    const wrap = document.getElementById('dash-table-wrap');
    if (!contracts.length) {
      wrap.innerHTML = `
        <div class="empty-state">
          <div class="eyebrow">No filings yet</div>
          <p>Upload a contract to begin automated clause and compliance analysis.</p>
        </div>
      `;
      return;
    }
    wrap.innerHTML = `
      <div class="table-wrap">
        <table class="ll-table">
          <thead><tr>
            <th>Contract Name</th><th>Type</th><th>Status</th><th>Compliance</th><th>Action</th>
          </tr></thead>
          <tbody>
            ${contracts.map(c => `
              <tr>
                <td><strong>${RenderHelpers.escapeHtml(c.contractName)}</strong></td>
                <td>${RenderHelpers.typeTag(c.contractType)}</td>
                <td>${RenderHelpers.statusPill(c.status)}</td>
                <td style="min-width:130px;">${RenderHelpers.progressBar(c.complianceScore ?? 0)}</td>
                <td><button class="btn btn-outline btn-sm" data-id="${c.id}">View</button></td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    `;
    wrap.querySelectorAll('button[data-id]').forEach(btn => {
      btn.addEventListener('click', () => {
        window.location.href = `./ContractDetails.html?id=${btn.dataset.id}`;
      });
    });
  }

  contractService.getContracts()
    .then((contracts) => {
      renderStatCards(contracts);
      renderTable(contracts);
    })
    .catch((error) => {
      if (error?.response?.status === 401) {
        message.error('Session expired. Please login again.');
      } else {
        message.error('Failed to fetch contracts');
      }
      document.getElementById('dash-table-wrap').innerHTML = `
        <div class="empty-state">
          <div class="eyebrow">Couldn't load contracts</div>
          <p>Try refreshing the page.</p>
        </div>
      `;
      document.getElementById('stat-cards').innerHTML = '';
    });
})();
