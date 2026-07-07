/**
 * services/api.js
 * Axios instance configuration.
 */
const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// Request interceptor: inject Authorization header if a token is present
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
