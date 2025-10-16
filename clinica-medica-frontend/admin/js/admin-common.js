$(function() {
  const token = checkAuth();
  if (!token) {
    return;
  }

  renderUserBadge();
  setupLogout();

  const currentPath = window.location.pathname.split('/').pop();
  document.querySelectorAll('[data-nav]').forEach((link) => {
    const target = link.getAttribute('href');
    if (target && target.includes(currentPath)) {
      link.classList.add('active');
    }
  });
});

function openModal(id) {
  const overlay = document.getElementById(id);
  if (overlay) {
    overlay.classList.add('is-open');
  }
}

function closeModal(id) {
  const overlay = document.getElementById(id);
  if (overlay) {
    overlay.classList.remove('is-open');
  }
}

function fillForm(form, data) {
  if (!form || !data) return;
  Object.entries(data).forEach(([key, value]) => {
    const field = form.elements.namedItem(key);
    if (!field) return;
    if (field.type === 'checkbox') {
      field.checked = Boolean(value);
    } else if (field.type === 'date' && value) {
      field.value = value.split('T')[0];
    } else if (field.type === 'datetime-local' && value) {
      field.value = toDateTimeLocal(value);
    } else {
      field.value = value ?? '';
    }
  });
}

function resetForm(form) {
  if (!form) return;
  form.reset();
}

function togglePermissionControls(container) {
  if (!container) return;
  container.querySelectorAll('[data-permission]').forEach((element) => {
    const permission = element.dataset.permission;
    if (!hasPermission(permission)) {
      element.classList.add('is-hidden');
    } else {
      element.classList.remove('is-hidden');
    }
  });
}
