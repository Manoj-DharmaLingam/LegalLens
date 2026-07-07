/**
 * services/reportService.js
 */
const reportService = {
  getContractReport() {
    return api.get('/reports/contracts').then(res => res.data);
  },
  getComplianceReport() {
    return api.get('/reports/compliance').then(res => res.data);
  },
  getRiskReport() {
    return api.get('/reports/risks').then(res => res.data);
  },
  getAllReports() {
    return api.get('/reports/all').then(res => res.data);
  }
};
