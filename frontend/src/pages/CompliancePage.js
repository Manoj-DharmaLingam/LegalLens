/**
 * pages/CompliancePage.js
 */
(function () {
  const state = MainLayout.mount({ active: '/compliance' });
  if (!state) return;

  contractService.getContracts()
    .then((contracts) => {
      const wrap = document.getElementById('compliance-table-wrap');
      if (!contracts.length) {
        wrap.innerHTML = `<div class="empty-state"><div class="eyebrow">Nothing to review</div><p>Upload a contract to see its compliance standing here.</p></div>`;
        return;
      }
      wrap.innerHTML = `
        <div class="table-wrap">
          <table class="ll-table">
            <thead><tr><th>Contract Name</th><th>Type</th><th>Compliance</th><th>Status</th><th></th></tr></thead>
            <tbody>
              ${contracts.map(c => `
                <tr>
                  <td><strong>${RenderHelpers.escapeHtml(c.contractName)}</strong></td>
                  <td>${RenderHelpers.typeTag(c.contractType)}</td>
                  <td style="min-width:130px;">${RenderHelpers.progressBar(c.complianceScore ?? 0)}</td>
                  <td>${RenderHelpers.statusPill(c.status)}</td>
                  <td><a class="btn btn-outline btn-sm" href="./ComplianceDashboard.html?id=${c.id}">Details</a></td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </div>
      `;
    })
    .catch(() => {
      message.error('Failed to fetch contracts');
      document.getElementById('compliance-table-wrap').innerHTML = `<div class="empty-state"><p>Couldn't load the portfolio.</p></div>`;
    });
})();
