/**
 * pages/ComplianceDashboard.js
 */
(function () {
  const state = MainLayout.mount({ active: '/compliance' });
  if (!state) return;

  const params = new URLSearchParams(window.location.search);
  const contractId = params.get('id');
  const root = document.getElementById('compliance-detail-root');

  function loadRules() {
    return complianceService.getRules().catch(() => []);
  }

  function load() {
    root.innerHTML = `<div class="loading-row"><div class="spinner"></div></div>`;
    Promise.all([
      complianceService.getResults(contractId).catch(() => complianceService.checkCompliance(contractId)),
      loadRules()
    ])
      .then(([result, rules]) => render(result, rules))
      .catch(() => {
        root.innerHTML = `<div class="empty-state"><p>No compliance result available yet.</p></div>`;
      });
  }

  function render(result, rules) {
    const issues = (result.issuesFound || '').split(/\n|(?=MISSING:)/).map(s => s.trim()).filter(Boolean);
    root.innerHTML = `
      <div class="grid grid-2">
        <div class="card">
          <div class="card-head"><h3>Score</h3></div>
          <div style="display:flex; justify-content:center; padding:10px 0 6px;">
            ${RenderHelpers.seal(result.complianceScore ?? 0)}
          </div>
          <div style="text-align:center; font-size:12px; color:var(--text-muted); margin-top:6px;">
            Analyzed ${RenderHelpers.formatDateTime(result.analyzedAt)}
          </div>
          <button class="btn btn-gold btn-block" id="run-check-btn" style="margin-top:16px;">Run Check</button>
        </div>

        <div class="card">
          <div class="card-head"><h3>Issues Found</h3></div>
          ${issues.length ? `<ul style="margin:0; padding-left:18px; font-size:13px; color:var(--text-muted); line-height:1.8;">
            ${issues.map(i => `<li>${RenderHelpers.escapeHtml(i)}</li>`).join('')}
          </ul>` : `<p style="font-size:13px; color:var(--text-muted);">No outstanding issues recorded.</p>`}
        </div>
      </div>

      <div class="card">
        <div class="card-head"><h3>Compliance Rules</h3></div>
        <div class="table-wrap">
          <table class="ll-table">
            <thead><tr><th>Rule</th><th>Description</th><th>Severity</th></tr></thead>
            <tbody>
              ${rules.length ? rules.map(r => `
                <tr>
                  <td><strong>${RenderHelpers.escapeHtml(r.ruleName)}</strong></td>
                  <td style="color:var(--text-muted);">${RenderHelpers.escapeHtml(r.description)}</td>
                  <td>${RenderHelpers.riskTag(r.severityLevel)}</td>
                </tr>
              `).join('') : `<tr><td colspan="3" style="color:var(--text-muted);">No rules configured.</td></tr>`}
            </tbody>
          </table>
        </div>
      </div>
    `;

    document.getElementById('run-check-btn').addEventListener('click', () => {
      const btn = document.getElementById('run-check-btn');
      btn.disabled = true; btn.textContent = 'Running…';
      complianceService.checkCompliance(contractId)
        .then(() => { message.success('Compliance check complete'); load(); })
        .catch(() => message.error('Compliance check failed'))
        .finally(() => { btn.disabled = false; btn.textContent = 'Run Check'; });
    });
  }

  load();
})();
