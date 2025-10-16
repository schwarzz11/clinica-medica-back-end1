let convenioEmEdicao = null;

$(function() {
  togglePermissionControls(document.body);
  carregarConvenios();

  $('#btn-novo-convenio').on('click', () => {
    convenioEmEdicao = null;
    document.getElementById('modal-convenio-titulo').textContent = 'Novo convênio';
    resetForm(document.getElementById('form-convenio'));
    openModal('modal-convenio');
  });

  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-convenio');
      closeModal('modal-confirmacao');
    });
  });

  $('#form-convenio').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const dados = {
      nomeEmpresa: form.nomeEmpresa.value.trim(),
      cnpj: form.cnpj.value.trim(),
      nomeContato: form.nomeContato.value.trim(),
      telefone: form.telefone.value.trim()
    };

    try {
      if (convenioEmEdicao) {
        await api.put(`/administrativo/convenios/${convenioEmEdicao}`, dados);
        showToast('Convênio atualizado com sucesso.');
      } else {
        await api.post('/administrativo/convenios', dados);
        showToast('Convênio cadastrado com sucesso.');
      }
      closeModal('modal-convenio');
      carregarConvenios();
    } catch (error) {
      console.error('Erro ao salvar convênio', error);
      showToast('Não foi possível salvar o convênio.', 'error');
    }
  });

  $('#btn-confirmar-exclusao').on('click', async () => {
    if (!convenioEmEdicao) return;
    try {
      await api.delete(`/administrativo/convenios/${convenioEmEdicao}`);
      showToast('Convênio excluído.');
      closeModal('modal-confirmacao');
      carregarConvenios();
    } catch (error) {
      console.error('Erro ao excluir convênio', error);
      showToast('Não foi possível excluir o convênio.', 'error');
    }
  });
});

async function carregarConvenios() {
  try {
    const convenios = await api.get('/administrativo/convenios');
    const corpo = $('#tabela-convenios');
    corpo.empty();
    if (!convenios.length) {
      corpo.append('<tr><td colspan="5" class="text-muted">Nenhum convênio cadastrado.</td></tr>');
      return;
    }
    convenios.forEach((convenio) => {
      const botoes = [];
      if (hasPermission('atualizarConvenio')) {
        botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${convenio.id}">Editar</button>`);
      }
      if (hasPermission('deletarConvenio')) {
        botoes.push(`<button class="btn btn-danger" data-acao="excluir" data-id="${convenio.id}">Excluir</button>`);
      }
      corpo.append(`
        <tr data-id="${convenio.id}">
          <td>${convenio.nomeEmpresa}</td>
          <td>${convenio.cnpj}</td>
          <td>${convenio.nomeContato || '-'}</td>
          <td>${convenio.telefone || '-'}</td>
          <td class="text-right">
            <div class="table-actions">${botoes.join(' ') || '<span class="text-muted">Sem permissões</span>'}</div>
          </td>
        </tr>
      `);
    });

    $('#tabela-convenios [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const convenio = convenios.find((item) => String(item.id) === String(id));
      if (!convenio) return;
      convenioEmEdicao = id;
      document.getElementById('modal-convenio-titulo').textContent = 'Editar convênio';
      fillForm(document.getElementById('form-convenio'), convenio);
      openModal('modal-convenio');
    });

    $('#tabela-convenios [data-acao="excluir"]').on('click', (event) => {
      convenioEmEdicao = event.currentTarget.dataset.id;
      openModal('modal-confirmacao');
    });
  } catch (error) {
    console.error('Erro ao carregar convênios', error);
    showToast('Não foi possível carregar os convênios.', 'error');
  }
}
