const TOKEN_KEY = 'authToken';
const USER_KEY = 'authUser';
const PERMISSIONS_KEY = 'userPermissions';

function saveSession({ token, user, permissions }) {
  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(USER_KEY, JSON.stringify(user));
  localStorage.setItem(PERMISSIONS_KEY, JSON.stringify(permissions || {}));
}

function clearSession() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
  localStorage.removeItem(PERMISSIONS_KEY);
}

function getSessionToken() {
  return localStorage.getItem(TOKEN_KEY);
}

function getCurrentUser() {
  const stored = localStorage.getItem(USER_KEY);
  return stored ? JSON.parse(stored) : null;
}

function getPermissions() {
  const stored = localStorage.getItem(PERMISSIONS_KEY);
  return stored ? JSON.parse(stored) : {};
}

function hasPermission(permissionName) {
  const permissions = getPermissions();
  return Boolean(permissions[permissionName]);
}

function decodeJwt(token) {
  if (!token) return null;
  const payload = token.split('.')[1];
  if (!payload) return null;
  try {
    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decodeURIComponent(escape(decoded)));
  } catch (error) {
    console.error('Falha ao decodificar JWT', error);
    return null;
  }
}

function checkAuth() {
  const token = getSessionToken();
  if (!token) {
    window.location.href = '/login.html';
    return null;
  }
  return token;
}

function renderUserBadge(selector = '#user-badge') {
  const user = getCurrentUser();
  if (!user) return;
  const container = document.querySelector(selector);
  if (!container) return;
  container.innerHTML = `
    <span class="badge neutral">${user.perfilNome || 'Perfil'}</span>
    <strong>${user.nome || user.usuario}</strong>
  `;
}

function setupLogout(buttonSelector = '.btn-logout') {
  document.querySelectorAll(buttonSelector).forEach((button) => {
    button.addEventListener('click', () => {
      clearSession();
      window.location.href = '/login.html';
    });
  });
}

function populatePermissionsSummary(selector) {
  const container = document.querySelector(selector);
  if (!container) return;
  const permissions = getPermissions();
  const fragments = Object.entries(permissions).map(([key, value]) => {
    const label = key.replace(/([A-Z])/g, ' $1').replace(/^./, (c) => c.toUpperCase());
    return `<span class="badge ${value ? '' : 'neutral'}">${label}</span>`;
  });
  container.innerHTML = fragments.join(' ');
}

$(function() {
  const loginForm = document.querySelector('#login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', async (event) => {
      event.preventDefault();
      const form = event.currentTarget;
      const usuario = form.usuario.value.trim();
      const senha = form.senha.value.trim();
      const feedback = document.querySelector('#login-feedback');
      if (feedback) {
        feedback.textContent = '';
        feedback.classList.remove('alert-danger');
      }

      if (!usuario || !senha) {
        if (feedback) {
          feedback.textContent = 'Informe usuário e senha.';
          feedback.classList.add('alert', 'alert-danger');
        }
        return;
      }

      try {
        const response = await apiRequest('/administrativo/auth/login', {
          method: 'POST',
          includeAuth: false,
          data: { usuario, senha }
        });
        saveSession(response);
        window.location.href = '/admin/dashboard.html';
      } catch (error) {
        console.error('Erro ao autenticar', error);
        if (feedback) {
          const message = (error.payload && error.payload.message) || 'Não foi possível autenticar. Verifique as credenciais.';
          feedback.textContent = message;
          feedback.classList.add('alert', 'alert-danger');
        }
      }
    });
  }
});
