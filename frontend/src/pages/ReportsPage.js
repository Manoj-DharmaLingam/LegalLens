/**
 * pages/ReportsPage.js
 */
(function () {
  const state = MainLayout.mount({ active: '/reports' });
  if (!state) return;

  document.getElementById('print-btn').addEventListener('click', () => window.print());

  contractService.getContracts()
    .then((contracts) => {
      const scored = contracts.filter(c => typeof c.complianceScore === 'number');
      const avg = scored.length ? Math.round(scored.reduce((a, c) => a + c.complianceScore, 0) / scored.length) : 0;
      const highRisk = contracts.filter(c => RenderHelpers.riskClass(c.complianceScore ?? 0) === 'high').length;

      document.getElementById('report-stats').innerHTML = `
        <div class="stat-card accent-red">
          <div class="stat-label">High Risk Alerts</div>
          <div class="stat-value">${highRisk}</div>
        </div>
        <div class="stat-card accent-gold">
          <div class="stat-label">Total Analysis</div>
          <div class="stat-value">${contracts.length}</div>
        </div>
      `;

      const wrap = document.getElementById('report-table-wrap');
      if (!contracts.length) {
        wrap.innerHTML = `<div class="empty-state"><p>No contracts to report on yet.</p></div>`;
        return;
      }
      wrap.innerHTML = `
        <div class="table-wrap">
          <table class="ll-table">
            <thead><tr><th>Contract Name</th><th>Type</th><th>Compliance Score</th><th>Risk Level</th><th></th></tr></thead>
            <tbody>
              ${contracts.map(c => {
                const risk = RenderHelpers.riskClass(c.complianceScore ?? 0).toUpperCase();
                return `
                <tr>
                  <td><strong>${RenderHelpers.escapeHtml(c.contractName)}</strong></td>
                  <td>${RenderHelpers.typeTag(c.contractType)}</td>
                  <td style="min-width:130px;">${RenderHelpers.progressBar(c.complianceScore ?? 0)}</td>
                  <td>${RenderHelpers.riskTag(risk)}</td>
                  <td><a class="btn btn-outline btn-sm" href="./ContractDetails.html?id=${c.id}">Details</a></td>
                </tr>
              `;}).join('')}
            </tbody>
          </table>
        </div>
      `;
    })
    .catch(() => {
      message.error('Failed to fetch report data');
      document.getElementById('report-table-wrap').innerHTML = `<div class="empty-state"><p>Couldn't load reports.</p></div>`;
    });
})();
