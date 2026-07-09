/**
 * pages/AdminDashboard.js
 */
(function () {
  const state = MainLayout.mount({ active: '/admin', requireRole: 'ROLE_ADMIN' });
  if (!state) return;

  const loaded = { users: false, templates: false, rules: false, activity: false };

  // ---- tab switching ----
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
      document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
      btn.classList.add('active');
      document.getElementById('tab-' + btn.dataset.tab).classList.add('active');
      loadTabData(btn.dataset.tab);
    });
  });

  function loadTabData(tab) {
    if (loaded[tab]) return;
    loaded[tab] = true;
    if (tab === 'users') loadUsers();
    if (tab === 'templates') loadTemplates();
    if (tab === 'rules') loadRules();
    if (tab === 'activity') loadActivity();
  }

  // ---- Users ----
  function loadUsers() {
    adminService.getUsers()
      .then(users => {
        document.getElementById('tab-users').innerHTML = `
          <div class="table-wrap">
            <table class="ll-table">
              <thead><tr><th>Username</th><th>Full Name</th><th>Email</th><th>Role</th><th>Action</th></tr></thead>
              <tbody>
                ${users.map(u => `
                  <tr>
                    <td><strong>${RenderHelpers.escapeHtml(u.username)}</strong></td>
                    <td>${RenderHelpers.escapeHtml(u.fullName)}</td>
                    <td>${RenderHelpers.escapeHtml(u.email)}</td>
                    <td>${RenderHelpers.typeTag((u.role || '').replace('ROLE_', ''))}</td>
                    <td><button class="btn btn-danger btn-sm" data-id="${u.id}">Delete</button></td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>
        `;
        document.querySelectorAll('#tab-users button[data-id]').forEach(btn => {
          btn.addEventListener('click', () => {
            const id = Number(btn.dataset.id);
            adminService.deleteUser(id)
              .then(() => { loaded.users = false; loadUsers(); message.success('User deleted'); })
              .catch((err) => {
                console.error('Delete user failed:', err);
                message.error(RenderHelpers.errorMessage(err, 'Failed to delete user'));
              });
          });
        });
      })
      .catch(() => message.error('Failed to load users'));
  }

  // ---- Templates ----
  let templates = [];
  function loadTemplates() {
    adminService.getTemplates()
      .then(data => { templates = data; renderTemplates(); })
      .catch(() => message.error('Failed to load templates'));
  }
  function renderTemplates() {
    const el = document.getElementById('templates-body');
    if (!templates.length) { el.innerHTML = `<div class="empty-state"><p>No clause templates yet.</p></div>`; return; }
    el.innerHTML = `
      <div class="table-wrap">
        <table class="ll-table">
          <thead><tr><th>Name</th><th>Category</th><th>Risk</th><th>Action</th></tr></thead>
          <tbody>
            ${templates.map(t => `
              <tr>
                <td><strong>${RenderHelpers.escapeHtml(t.name)}</strong></td>
                <td>${RenderHelpers.typeTag(t.category)}</td>
                <td>${RenderHelpers.riskTag(t.recommendedRiskLevel)}</td>
                <td>
                  <button class="btn btn-outline btn-sm" data-edit="${t.id}">Edit</button>
                  <button class="btn btn-danger btn-sm" data-del="${t.id}">Delete</button>
                </td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    `;
    el.querySelectorAll('[data-edit]').forEach(b => b.addEventListener('click', () => openTemplateModal(b.dataset.edit)));
    el.querySelectorAll('[data-del]').forEach(b => b.addEventListener('click', () => {
      adminService.deleteTemplate(b.dataset.del).then(() => { loadTemplates(); message.success('Template deleted'); }).catch(() => message.error('Failed to delete template'));
    }));
  }
  document.getElementById('add-template-btn').addEventListener('click', () => openTemplateModal(null));
  let editingTemplateId = null;
  function openTemplateModal(id) {
    editingTemplateId = id;
    const tpl = id ? templates.find(t => String(t.id) === String(id)) : null;
    document.getElementById('template-modal-title').textContent = id ? 'Edit Clause Template' : 'Add Clause Template';
    document.getElementById('tplName').value = tpl?.name || '';
    document.getElementById('tplCategory').value = tpl?.category || '';
    document.getElementById('tplContent').value = tpl?.defaultContent || '';
    document.getElementById('tplRisk').value = tpl?.recommendedRiskLevel || 'LOW';
    document.getElementById('template-modal-backdrop').classList.add('open');
  }
  document.getElementById('template-cancel').addEventListener('click', () => document.getElementById('template-modal-backdrop').classList.remove('open'));
  document.getElementById('template-form').addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      id: editingTemplateId || undefined,
      name: document.getElementById('tplName').value.trim(),
      category: document.getElementById('tplCategory').value.trim(),
      defaultContent: document.getElementById('tplContent').value.trim(),
      recommendedRiskLevel: document.getElementById('tplRisk').value
    };
    adminService.saveTemplate(payload)
      .then(() => { document.getElementById('template-modal-backdrop').classList.remove('open'); message.success('Template saved'); loadTemplates(); })
      .catch(() => message.error('Failed to save template'));
  });

  // ---- Rules ----
  let rules = [];
  function loadRules() {
    adminService.getRules()
      .then(data => { rules = data; renderRules(); })
      .catch(() => message.error('Failed to load rules'));
  }
  function renderRules() {
    const el = document.getElementById('rules-body');
    if (!rules.length) { el.innerHTML = `<div class="empty-state"><p>No compliance rules yet.</p></div>`; return; }
    el.innerHTML = `
      <div class="table-wrap">
        <table class="ll-table">
          <thead><tr><th>Rule Name</th><th>Description</th><th>Severity</th><th>Action</th></tr></thead>
          <tbody>
            ${rules.map(r => `
              <tr>
                <td><strong>${RenderHelpers.escapeHtml(r.ruleName)}</strong></td>
                <td style="color:var(--text-muted);">${RenderHelpers.escapeHtml(r.description)}</td>
                <td>${RenderHelpers.riskTag(r.severityLevel)}</td>
                <td>
                  <button class="btn btn-outline btn-sm" data-edit="${r.id}">Edit</button>
                  <button class="btn btn-danger btn-sm" data-del="${r.id}">Delete</button>
                </td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    `;
    el.querySelectorAll('[data-edit]').forEach(b => b.addEventListener('click', () => openRuleModal(b.dataset.edit)));
    el.querySelectorAll('[data-del]').forEach(b => b.addEventListener('click', () => {
      adminService.deleteRule(b.dataset.del).then(() => { loadRules(); message.success('Rule deleted'); }).catch(() => message.error('Failed to delete rule'));
    }));
  }
  document.getElementById('add-rule-btn').addEventListener('click', () => openRuleModal(null));
  let editingRuleId = null;
  function openRuleModal(id) {
    editingRuleId = id;
    const rule = id ? rules.find(r => String(r.id) === String(id)) : null;
    document.getElementById('rule-modal-title').textContent = id ? 'Edit Compliance Rule' : 'Add Compliance Rule';
    document.getElementById('ruleName').value = rule?.ruleName || '';
    document.getElementById('ruleDescription').value = rule?.description || '';
    document.getElementById('ruleSeverity').value = rule?.severityLevel || 'LOW';
    document.getElementById('rule-modal-backdrop').classList.add('open');
  }
  document.getElementById('rule-cancel').addEventListener('click', () => document.getElementById('rule-modal-backdrop').classList.remove('open'));
  document.getElementById('rule-form').addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      id: editingRuleId || undefined,
      ruleName: document.getElementById('ruleName').value.trim(),
      description: document.getElementById('ruleDescription').value.trim(),
      severityLevel: document.getElementById('ruleSeverity').value
    };
    adminService.saveRule(payload)
      .then(() => { document.getElementById('rule-modal-backdrop').classList.remove('open'); message.success('Rule saved'); loadRules(); })
      .catch(() => message.error('Failed to save rule'));
  });

  // ---- Activity ----
  function loadActivity() {
    adminService.getActivityLogs()
      .then(logs => {
        document.getElementById('tab-activity').innerHTML = logs.length ? `
          <div class="table-wrap">
            <table class="ll-table">
              <thead><tr><th>Action</th><th>Username</th><th>Details</th><th>Timestamp</th></tr></thead>
              <tbody>
                ${logs.map(l => `
                  <tr>
                    <td>${RenderHelpers.typeTag(l.action)}</td>
                    <td>${RenderHelpers.escapeHtml(l.username)}</td>
                    <td style="color:var(--text-muted);">${RenderHelpers.escapeHtml(l.details)}</td>
                    <td>${RenderHelpers.formatDateTime(l.logTimestamp)}</td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>
        ` : `<div class="empty-state"><p>No activity recorded yet.</p></div>`;
      })
      .catch(() => message.error('Failed to load activity log'));
  }

  loadTabData('users');
})();
