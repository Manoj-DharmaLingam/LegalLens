/**
 * js/toast.js
 * Stand-in for antd's `message` API: message.success(), message.error(), message.info()
 */
const message = {
  _root() {
    let root = document.getElementById('toast-root');
    if (!root) {
      root = document.createElement('div');
      root.id = 'toast-root';
      document.body.appendChild(root);
    }
    return root;
  },
  _show(text, type) {
    const root = this._root();
    const el = document.createElement('div');
    el.className = `toast ${type}`;
    el.textContent = text;
    root.appendChild(el);
    setTimeout(() => el.remove(), 3000);
  },
  success(text) { this._show(text, 'success'); },
  error(text) { this._show(text, 'error'); },
  info(text) { this._show(text, 'info'); }
};
