/**
 * services/clauseService.js
 */
const clauseService = {
  analyzeContract(contractId) {
    return api.post('/clauses/analyze', null, { params: { contractId } }).then(res => res.data);
  },
  getClauses(contractId) {
    return api.get('/clauses', { params: { contractId } }).then(res => res.data);
  },
  updateClause(id, clause) {
    return api.put(`/clauses/${id}`, clause).then(res => res.data);
  },
  deleteClause(id) {
    return api.delete(`/clauses/${id}`).then(res => res.data);
  }
};
