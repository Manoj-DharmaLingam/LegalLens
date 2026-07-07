/**
 * store/authStore.js
 * A minimal vanilla-JS stand-in for Redux Toolkit's authSlice.
 * Same state shape and persistence rules as the spec:
 *   { user, token, isAuthenticated }
 */
const AUTH_EVENT = 'authStore:change';

const authStore = {
  getState() {
    const token = localStorage.getItem('token') || null;
    let user = null;
    try { user = JSON.parse(localStorage.getItem('user')) || null; } catch (e) { user = null; }
    return {
      user,
      token,
      isAuthenticated: !!token
    };
  },

  /** action: loginSuccess({ user, token }) */
  loginSuccess(payload) {
    localStorage.setItem('token', payload.token);
    localStorage.setItem('user', JSON.stringify(payload.user));
    window.dispatchEvent(new CustomEvent(AUTH_EVENT, { detail: this.getState() }));
  },

  /** action: logout() */
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.dispatchEvent(new CustomEvent(AUTH_EVENT, { detail: this.getState() }));
  },

  subscribe(fn) {
    window.addEventListener(AUTH_EVENT, (e) => fn(e.detail));
  }
};
