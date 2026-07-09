/**
 * pages/ContractDetails.js
 */
(function () {
  const state = MainLayout.mount({ active: '/' });
  if (!state) return;

  const params = new URLSearchParams(window.location.search);
  const contractId = params.get('id');
  const canReview = state.user?.role === 'ROLE_LEGAL_REVIEWER' || state.user?.role === 'ROLE_ADMIN';
  const root = document.getElementById('details-root');

  let contract = null;
  let clauses = [];
  let comments = [];

  function load() {
    root.innerHTML = `<div class="loading-row"><div class="spinner"></div></div>`;
    Promise.all([
      contractService.getContract(contractId),
      clauseService.getClauses(contractId),
      commentService.getComments(contractId),
      complianceService.getResults(contractId)
    ])
      .then(([c, cl, cm, cr]) => {
        contract = c;
        // attach latest compliance score (if available) so the UI can render the seal
        if (cr && typeof cr.complianceScore !== 'undefined' && cr.complianceScore !== null) {
          contract.complianceScore = cr.complianceScore;
        }
        clauses = cl; comments = cm;
        render();
      })
      .catch(() => {
        message.error('Failed to fetch details');
        root.innerHTML = `<div class="empty-state"><div class="eyebrow">Not found</div><p>This contract could not be loaded.</p></div>`;
      });
  }

  function render() {
    root.innerHTML = `
      <div class="grid grid-2">
        <div class="card">
          <div class="card-head">
            <h3>${RenderHelpers.escapeHtml(contract.contractName)}</h3>
            ${RenderHelpers.statusPill(contract.status)}
          </div>
          <div style="display:flex; gap:20px; align-items:center; margin-bottom:16px;">
            ${RenderHelpers.seal(contract.complianceScore ?? 0)}
            <div style="font-size:13px; color:var(--text-muted); line-height:1.7;">
              <div><strong>Type:</strong> ${RenderHelpers.escapeHtml(contract.contractType || '—')}</div>
              <div><strong>Jurisdiction:</strong> ${RenderHelpers.escapeHtml(contract.jurisdiction || '—')}</div>
              <div><strong>Value:</strong> ${contract.contractValue != null ? '$' + Number(contract.contractValue).toLocaleString() : '—'}</div>
              <div><strong>Uploaded:</strong> ${RenderHelpers.formatDateTime(contract.uploadDate)}</div>
            </div>
          </div>
          <hr class="divider-dashed">
          <div style="font-size:13px; color:var(--text-muted);">
            <div><strong>First Party:</strong> ${RenderHelpers.escapeHtml(contract.firstParty || '—')}</div>
            <div><strong>Second Party:</strong> ${RenderHelpers.escapeHtml(contract.secondParty || '—')}</div>
            <div><strong>Start:</strong> ${RenderHelpers.formatDate(contract.startDate)} &nbsp;&middot;&nbsp; <strong>End:</strong> ${RenderHelpers.formatDate(contract.endDate)}</div>
            ${contract.description ? `<div style="margin-top:8px;">${RenderHelpers.escapeHtml(contract.description)}</div>` : ''}
          </div>

          ${canReview ? `
            <hr class="divider-dashed">
            <div class="form-row" style="margin-bottom:0;">
              <label>Update Status</label>
              <div style="display:flex; gap:8px;">
                <select class="form-control" id="status-select">
                  ${['UPLOADED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED'].map(s => `<option ${s === contract.status ? 'selected' : ''}>${s}</option>`).join('')}
                </select>
                <button class="btn btn-outline" id="status-save">Save</button>
              </div>
            </div>
          ` : ''}
        </div>

        <div class="card">
          <div class="card-head">
            <h3>Detected Clauses</h3>
            <button class="btn btn-gold btn-sm" id="analyze-btn">Analyze</button>
          </div>
          <div id="clauses-list">
            ${clauses.length ? clauses.map(clauseRow).join('') : `<div class="empty-state"><p>No clauses detected yet — run analysis.</p></div>`}
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-head"><h3>Comments</h3></div>
        <div id="comments-list">
          ${comments.length ? comments.map(commentRow).join('') : `<p style="color:var(--text-muted); font-size:13px;">No comments yet.</p>`}
        </div>
        <hr class="divider-dashed">
        <div class="form-row" style="margin-bottom:0;">
          <textarea class="form-control" id="new-comment" placeholder="Add a comment for the reviewer team…"></textarea>
        </div>
        <button class="btn btn-primary" id="comment-submit" style="margin-top:10px;">Post Comment</button>
      </div>
    `;

    document.getElementById('analyze-btn').addEventListener('click', onAnalyze);
    document.querySelectorAll('.clause-edit-btn').forEach(btn => {
      btn.addEventListener('click', () => openClauseModal(btn.dataset.id));
    });
    document.getElementById('comment-submit').addEventListener('click', onAddComment);
    const statusSave = document.getElementById('status-save');
    if (statusSave) statusSave.addEventListener('click', onStatusUpdate);
  }

  function clauseRow(cl) {
    return `
      <div class="clause-item risk-${cl.riskLevel}">
        <div class="clause-head">
          <span class="clause-type">${RenderHelpers.escapeHtml(cl.clauseType)}</span>
          <div style="display:flex; gap:8px; align-items:center;">
            ${RenderHelpers.riskTag(cl.riskLevel)}
            <button class="btn btn-outline btn-sm clause-edit-btn" data-id="${cl.id}">Edit</button>
          </div>
        </div>
        <div class="clause-content">${RenderHelpers.escapeHtml(cl.content)}</div>
      </div>
    `;
  }

  function commentRow(cm) {
    return `
      <div class="comment-item">
        <div class="meta">${RenderHelpers.escapeHtml(cm.reviewer?.username || 'user')} &middot; ${RenderHelpers.formatDateTime(cm.createdAt)}</div>
        <div>${RenderHelpers.escapeHtml(cm.content)}</div>
      </div>
    `;
  }

  function onAnalyze() {
    const btn = document.getElementById('analyze-btn');
    btn.disabled = true; btn.textContent = 'Analyzing…';
    clauseService.analyzeContract(contractId)
      .then(() => {
        message.success('Analysis complete!');
        load();
      })
      .catch(() => message.error('Analysis failed'))
      .finally(() => { btn.disabled = false; btn.textContent = 'Analyze'; });
  }

  function onStatusUpdate() {
    const status = document.getElementById('status-select').value;
    contractService.updateStatus(contractId, status)
      .then(() => { message.success('Status updated'); load(); })
      .catch(() => message.error('Failed to update status'));
  }

  function onAddComment() {
    const el = document.getElementById('new-comment');
    const content = el.value.trim();
    if (!content) return;
    commentService.addComment(contractId, content)
      .then(() => { el.value = ''; load(); })
      .catch(() => message.error('Failed to add comment'));
  }

  // ---- clause edit modal ----
  let editingClauseId = null;
  const modalBackdrop = document.getElementById('clause-modal-backdrop');

  function openClauseModal(id) {
    const cl = clauses.find(c => String(c.id) === String(id));
    if (!cl) return;
    editingClauseId = id;
    document.getElementById('clauseType').value = cl.clauseType;
    document.getElementById('clauseRisk').value = cl.riskLevel;
    document.getElementById('clauseContent').value = cl.content;
    modalBackdrop.classList.add('open');
  }

  document.getElementById('clause-cancel').addEventListener('click', () => modalBackdrop.classList.remove('open'));

  document.getElementById('clause-form').addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      clauseType: document.getElementById('clauseType').value,
      riskLevel: document.getElementById('clauseRisk').value,
      content: document.getElementById('clauseContent').value
    };
    clauseService.updateClause(editingClauseId, payload)
      .then(() => {
        modalBackdrop.classList.remove('open');
        message.success('Clause updated');
        load();
      })
      .catch(() => message.error('Failed to update clause'));
  });

  load();
})();
