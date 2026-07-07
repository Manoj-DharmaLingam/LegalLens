/**
 * services/contractService.js
 */
const contractService = {
  getContracts() {
    return api.get('/contracts').then(res => res.data);
  },
  getContract(id) {
    return api.get(`/contracts/${id}`).then(res => res.data);
  },
  uploadContract(values, file) {
    const formData = new FormData();
    formData.append('file', file);
    Object.keys(values).forEach(key => {
      if (values[key] !== undefined && values[key] !== null) {
        formData.append(key, values[key]);
      }
    });
    return api.post('/contracts/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }).then(res => res.data);
  },
  updateStatus(id, status) {
    return api.put(`/contracts/${id}`, null, { params: { status } }).then(res => res.data);
  },
  deleteContract(id) {
    return api.delete(`/contracts/${id}`).then(res => res.data);
  }
};
