/**
 * pages/RegisterPage.js
 */
(function () {
  if (authStore.getState().isAuthenticated) {
    window.location.href = './Dashboard.html';
    return;
  }

  const form = document.getElementById('register-form');
  const submitBtn = document.getElementById('register-submit');

  function setInvalid(rowId, invalid) {
    document.getElementById(rowId).classList.toggle('invalid', invalid);
  }

  function isEmail(v) { return /\S+@\S+\.\S+/.test(v); }

  function validate() {
    const fullName = document.getElementById('fullName').value.trim();
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    let ok = true;

    setInvalid('row-fullName', !fullName); if (!fullName) ok = false;
    setInvalid('row-username', !username); if (!username) ok = false;
    setInvalid('row-email', !email || !isEmail(email)); if (!email || !isEmail(email)) ok = false;
    setInvalid('row-password', !password); if (!password) ok = false;

    return ok;
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (!validate()) return;

    const values = {
      fullName: document.getElementById('fullName').value.trim(),
      username: document.getElementById('username').value.trim(),
      email: document.getElementById('email').value.trim(),
      password: document.getElementById('password').value,
      role: document.getElementById('role').value
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'Creating account…';

    authService.register(values)
      .then(() => {
        message.success('Registration successful! Please login.');
        window.location.href = './LoginPage.html';
      })
      .catch((error) => {
        message.error(error?.response?.data?.error || 'Registration failed');
      })
      .finally(() => {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Register';
      });
  });
})();
