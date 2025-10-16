const API_BASE_URL = 'http://localhost:8088';

function getStoredToken() {
  return localStorage.getItem('authToken');
}

function buildHeaders(customHeaders = {}, includeAuth = true) {
  const headers = Object.assign({ 'Content-Type': 'application/json' }, customHeaders);
  if (includeAuth) {
    const token = getStoredToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
  }
  return headers;
}

async function apiRequest(path, { method = 'GET', data, headers = {}, includeAuth = true } = {}) {
  const config = {
    method,
    headers: buildHeaders(headers, includeAuth)
  };

  if (data !== undefined) {
    config.body = typeof data === 'string' ? data : JSON.stringify(data);
  }

  const response = await fetch(`${API_BASE_URL}${path}`, config);

  if (response.status === 401) {
    clearSession();
    window.location.href = '/login.html';
    return Promise.reject(new Error('Não autenticado.'));
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('content-type');
  const payload = contentType && contentType.includes('application/json') ? await response.json() : await response.text();

  if (!response.ok) {
    const error = new Error('Erro na requisição');
    error.payload = payload;
    error.status = response.status;
    throw error;
  }

  return payload;
}

const api = {
  get: (path, options = {}) => apiRequest(path, Object.assign({ method: 'GET' }, options)),
  post: (path, data, options = {}) => apiRequest(path, Object.assign({ method: 'POST', data }, options)),
  put: (path, data, options = {}) => apiRequest(path, Object.assign({ method: 'PUT', data }, options)),
  delete: (path, options = {}) => apiRequest(path, Object.assign({ method: 'DELETE' }, options))
};

function formatDate(value) {
  if (!value) return '';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleDateString('pt-BR');
}

function formatDateTime(value) {
  if (!value) return '';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString('pt-BR', { hour: '2-digit', minute: '2-digit' });
}

function toDateTimeLocal(value) {
  if (!value) return '';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return '';
  const pad = (num) => `${num}`.padStart(2, '0');
  const year = date.getFullYear();
  const month = pad(date.getMonth() + 1);
  const day = pad(date.getDate());
  const hours = pad(date.getHours());
  const minutes = pad(date.getMinutes());
  return `${year}-${month}-${day}T${hours}:${minutes}`;
}

function showToast(message, type = 'success') {
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;
  document.body.appendChild(toast);
  requestAnimationFrame(() => toast.classList.add('is-visible'));
  setTimeout(() => {
    toast.classList.remove('is-visible');
    setTimeout(() => toast.remove(), 200);
  }, 3200);
}
