/**
 * pages/LoginPage.js
 */
(function () {
  // If already logged in, skip straight to the dashboard
  if (authStore.getState().isAuthenticated) {
    window.location.href = './Dashboard.html';
    return;
  }

  const form = document.getElementById('login-form');
  const submitBtn = document.getElementById('login-submit');

  function setInvalid(rowId, invalid) {
    document.getElementById(rowId).classList.toggle('invalid', invalid);
  }

  function validate() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    let ok = true;
    if (!username) { setInvalid('row-username', true); ok = false; } else setInvalid('row-username', false);
    if (!password) { setInvalid('row-password', true); ok = false; } else setInvalid('row-password', false);
    return ok;
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (!validate()) return;

    const values = {
      username: document.getElementById('username').value.trim(),
      password: document.getElementById('password').value
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'Logging in…';

    authService.login(values)
      .then((data) => {
        authStore.loginSuccess({
          user: { username: data.username, role: data.role },
          token: data.accessToken
        });
        message.success('Login successful!');
        window.location.href = './Dashboard.html';
      })
      .catch((error) => {
        message.error(RenderHelpersFallback(error));
        document.getElementById('password').value = '';
      })
      .finally(() => {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Log in';
      });
  });

  function RenderHelpersFallback(error) {
    return error?.response?.data?.error || 'Login failed';
  }
})();
