/**
 * services/adminService.js
 */
const adminService = {
  getUsers() {
    return api.get('/admin/users').then(res => res.data);
  },
  deleteUser(id) {
    return api.delete(`/admin/users/${id}`).then(res => res.data);
  },
  getTemplates() {
    return api.get('/admin/templates').then(res => res.data);
  },
  saveTemplate(template) {
    if (template.id) {
      return api.put(`/admin/templates/${template.id}`, template).then(res => res.data);
    }
    return api.post('/admin/templates', template).then(res => res.data);
  },
  deleteTemplate(id) {
    return api.delete(`/admin/templates/${id}`).then(res => res.data);
  },
  getRules() {
    return api.get('/admin/rules').then(res => res.data);
  },
  saveRule(rule) {
    if (rule.id) {
      return api.put(`/admin/rules/${rule.id}`, rule).then(res => res.data);
    }
    return api.post('/admin/rules', rule).then(res => res.data);
  },
  deleteRule(id) {
    return api.delete(`/admin/rules/${id}`).then(res => res.data);
  },
  getActivityLogs() {
    return api.get('/admin/activity').then(res => res.data);
  }
};
