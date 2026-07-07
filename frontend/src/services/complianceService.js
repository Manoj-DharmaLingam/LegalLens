/**
 * services/complianceService.js
 */
const complianceService = {
  checkCompliance(contractId) {
    return api.post('/compliance/check', null, { params: { contractId } }).then(res => res.data);
  },
  getResults(contractId) {
    return api.get(`/compliance/results/${contractId}`).then(res => res.data);
  },
  getRules() {
    return api.get('/compliance/rules').then(res => res.data);
  }
};
