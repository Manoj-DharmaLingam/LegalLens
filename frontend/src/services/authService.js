/**
 * services/authService.js
 */
const authService = {
  login(credentials) {
    return api.post('/auth/login', credentials).then(res => res.data);
  },
  register(userData) {
    return api.post('/auth/register', userData).then(res => res.data);
  }
};
