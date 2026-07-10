/**
 * js/render-helpers.js
 * Small, dependency-free rendering helpers reused across pages.
 */
const RenderHelpers = {
  riskClass(score) {
    if (score >= 80) return 'low';
    if (score >= 50) return 'medium';
    return 'high';
  },

  /** The signature "compliance seal" — a notary-style stamp for a score. */
  seal(score, opts = {}) {
    const s = Number(score) || 0;
    const risk = this.riskClass(s);
    const size = opts.small ? 'seal-sm' : '';
    const r = opts.small ? 24 : 46;
    const c = 2 * Math.PI * r;
    const offset = c - (c * s) / 100;
    const dim = opts.small ? 56 : 108;
    return `
      <div class="seal risk-${risk} ${size}">
        <svg viewBox="0 0 ${dim} ${dim}">
          <circle cx="${dim/2}" cy="${dim/2}" r="${r}" fill="none" stroke="currentColor" stroke-opacity=".18" stroke-width="${opts.small?4:6}"></circle>
          <circle cx="${dim/2}" cy="${dim/2}" r="${r}" fill="none" stroke="currentColor" stroke-width="${opts.small?4:6}"
            stroke-dasharray="${c}" stroke-dashoffset="${offset}" stroke-linecap="round"
            transform="rotate(-90 ${dim/2} ${dim/2})"></circle>
        </svg>
        <div style="text-align:center;">
          <div class="seal-score">${s}</div>
          ${opts.small ? '' : '<div class="seal-pct">SCORE</div>'}
        </div>
      </div>
    `;
  },

  progressBar(score) {
    const s = Number(score) || 0;
    const cls = s >= 80 ? '' : (s >= 50 ? 'mid' : 'low');
    return `<div class="progress ${cls}"><span style="width:${s}%"></span></div>`;
  },

  statusPill(status) {
    return `<span class="status-pill status-${status}">${(status || '').replace('_', ' ')}</span>`;
  },

  riskTag(level) {
    const cls = level === 'HIGH' ? 'tag-red' : (level === 'MEDIUM' ? 'tag-gold' : (level === 'PENDING' ? 'tag-slate' : 'tag-sage'));
    return `<span class="tag ${cls}">${level}</span>`;
  },

  typeTag(text) {
    return `<span class="tag tag-slate">${text || '—'}</span>`;
  },

  formatDate(value) {
    if (!value) return '—';
    const d = new Date(value);
    if (isNaN(d.getTime())) return value;
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
  },

  formatDateTime(value) {
    if (!value) return '—';
    const d = new Date(value);
    if (isNaN(d.getTime())) return value;
    return d.toLocaleString('en-US', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
  },

  escapeHtml(str) {
    if (str === undefined || str === null) return '';
    return String(str)
      .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;').replace(/'/g, '&#039;');
  },

  errorMessage(err, fallback) {
    return err?.response?.data?.error || err?.response?.data?.message || fallback;
  }
};
