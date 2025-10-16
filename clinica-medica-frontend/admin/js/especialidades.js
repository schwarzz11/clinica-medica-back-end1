let especialidadeEmEdicao = null;

$(function() {
  togglePermissionControls(document.body);
  carregarEspecialidades();

  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-especialidade');
      closeModal('modal-confirmacao');
    });
  });

  $('#btn-nova-especialidade').on('click', () => {
    especialidadeEmEdicao = null;
    document.getElementById('modal-especialidade-titulo').textContent = 'Nova especialidade';
    resetForm(document.getElementById('form-especialidade'));
    openModal('modal-especialidade');
  });

  $('#form-especialidade').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const payload = { nome: form.nome.value.trim() };
    try {
      if (especialidadeEmEdicao) {
        await api.put(`/administrativo/especialidades/${especialidadeEmEdicao}`, payload);
        showToast('Especialidade atualizada com sucesso.');
      } else {
        await api.post('/administrativo/especialidades', payload);
        showToast('Especialidade cadastrada com sucesso.');
      }
      closeModal('modal-especialidade');
      await carregarEspecialidades();
    } catch (error) {
      console.error('Erro ao salvar especialidade', error);
      showToast('Não foi possível salvar a especialidade.', 'error');
    }
  });

  $('#btn-confirmar-exclusao').on('click', async () => {
    if (!especialidadeEmEdicao) return;
    try {
      await api.delete(`/administrativo/especialidades/${especialidadeEmEdicao}`);
      showToast('Especialidade removida.');
      closeModal('modal-confirmacao');
      await carregarEspecialidades();
    } catch (error) {
      console.error('Erro ao remover especialidade', error);
      showToast('Não foi possível remover a especialidade.', 'error');
    }
  });
});

async function carregarEspecialidades() {
  try {
    const especialidades = await api.get('/administrativo/especialidades');
    const corpo = $('#tabela-especialidades');
    corpo.empty();
    if (!especialidades.length) {
      corpo.append('<tr><td colspan="2" class="text-muted">Nenhuma especialidade cadastrada.</td></tr>');
      return;
    }
    especialidades.forEach((especialidade) => {
      const botoes = [];
      if (hasPermission('atualizarEspecialidade')) {
        botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${especialidade.id}">Editar</button>`);
      }
      if (hasPermission('deletarEspecialidade')) {
        botoes.push(`<button class="btn btn-danger" data-acao="excluir" data-id="${especialidade.id}">Excluir</button>`);
      }
      corpo.append(`
        <tr data-id="${especialidade.id}">
          <td>${especialidade.nome}</td>
          <td class="text-right"><div class="table-actions">${botoes.join(' ') || '<span class="text-muted">Sem permissões</span>'}</div></td>
        </tr>
      `);
    });

    $('#tabela-especialidades [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const especialidade = especialidades.find((item) => String(item.id) === String(id));
      if (!especialidade) return;
      especialidadeEmEdicao = id;
      document.getElementById('modal-especialidade-titulo').textContent = 'Editar especialidade';
      fillForm(document.getElementById('form-especialidade'), especialidade);
      openModal('modal-especialidade');
    });

    $('#tabela-especialidades [data-acao="excluir"]').on('click', (event) => {
      especialidadeEmEdicao = event.currentTarget.dataset.id;
      openModal('modal-confirmacao');
    });
  } catch (error) {
    console.error('Erro ao carregar especialidades', error);
    showToast('Não foi possível carregar as especialidades.', 'error');
  }
}
