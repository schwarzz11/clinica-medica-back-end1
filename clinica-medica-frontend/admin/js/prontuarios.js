let prontuarioEmEdicao = null;
let consultasDisponiveis = [];

$(async function() {
  togglePermissionControls(document.body);

  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-prontuario');
      closeModal('modal-confirmacao');
    });
  });

  $('#btn-novo-prontuario').on('click', () => {
    prontuarioEmEdicao = null;
    document.getElementById('modal-prontuario-titulo').textContent = 'Novo prontuário';
    resetForm(document.getElementById('form-prontuario'));
    preencherConsultas();
    openModal('modal-prontuario');
  });

  $('#form-prontuario').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const payload = {
      consultaId: Number(form.consultaId.value),
      observacoes: form.observacoes.value.trim(),
      receituario: form.receituario.value.trim(),
      exames: form.exames.value.trim()
    };

    try {
      if (prontuarioEmEdicao) {
        await api.put(`/atendimento/prontuarios/${prontuarioEmEdicao}`, payload);
        showToast('Prontuário atualizado com sucesso.');
      } else {
        await api.post('/atendimento/prontuarios', payload);
        showToast('Prontuário registrado com sucesso.');
      }
      closeModal('modal-prontuario');
      await carregarProntuarios();
    } catch (error) {
      console.error('Erro ao salvar prontuário', error);
      showToast('Não foi possível salvar o prontuário.', 'error');
    }
  });

  $('#btn-confirmar-exclusao').on('click', async () => {
    if (!prontuarioEmEdicao) return;
    try {
      await api.delete(`/atendimento/prontuarios/${prontuarioEmEdicao}`);
      showToast('Prontuário excluído.');
      closeModal('modal-confirmacao');
      await carregarProntuarios();
    } catch (error) {
      console.error('Erro ao excluir prontuário', error);
      showToast('Não foi possível excluir o prontuário.', 'error');
    }
  });

  await carregarConsultas();
  await carregarProntuarios();
});

async function carregarConsultas() {
  try {
    consultasDisponiveis = await api.get('/agendamento/consultas');
    preencherConsultas();
  } catch (error) {
    console.error('Erro ao carregar consultas', error);
    showToast('Não foi possível carregar as consultas para vincular.', 'error');
  }
}

function preencherConsultas() {
  const select = document.getElementById('consultaId');
  if (!select) return;
  select.innerHTML = '<option value="">Selecione</option>';
  consultasDisponiveis
    .filter((consulta) => consulta.estaAtiva)
    .forEach((consulta) => {
      const option = document.createElement('option');
      option.value = consulta.id;
      option.textContent = `${formatDateTime(consulta.dataHorario)} - ${consulta.nomePaciente}`;
      select.appendChild(option);
    });
}

async function carregarProntuarios() {
  try {
    const prontuarios = await api.get('/atendimento/prontuarios');
    const corpo = $('#tabela-prontuarios');
    corpo.empty();
    if (!prontuarios.length) {
      corpo.append('<tr><td colspan="6" class="text-muted">Nenhum prontuário cadastrado.</td></tr>');
      return;
    }
    prontuarios.forEach((prontuario) => {
      const botoes = [];
      if (hasPermission('atualizarProntuario')) {
        botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${prontuario.id}">Editar</button>`);
      }
      if (hasPermission('deletarProntuario')) {
        botoes.push(`<button class="btn btn-danger" data-acao="excluir" data-id="${prontuario.id}">Excluir</button>`);
      }
      corpo.append(`
        <tr data-id="${prontuario.id}">
          <td>${formatDateTime(prontuario.dataConsulta)}</td>
          <td>${prontuario.nomePaciente}</td>
          <td>${prontuario.nomeMedico}</td>
          <td>${prontuario.receituario ? prontuario.receituario : '-'}</td>
          <td>${prontuario.exames ? prontuario.exames : '-'}</td>
          <td class="text-right"><div class="table-actions">${botoes.join(' ') || '<span class="text-muted">Sem ações</span>'}</div></td>
        </tr>
      `);
    });

    $('#tabela-prontuarios [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const prontuario = prontuarios.find((item) => String(item.id) === String(id));
      if (!prontuario) return;
      prontuarioEmEdicao = id;
      document.getElementById('modal-prontuario-titulo').textContent = 'Editar prontuário';
      const form = document.getElementById('form-prontuario');
      preencherConsultas();
      form.consultaId.value = prontuario.consultaId || '';
      form.observacoes.value = prontuario.observacoes || '';
      form.receituario.value = prontuario.receituario || '';
      form.exames.value = prontuario.exames || '';
      openModal('modal-prontuario');
    });

    $('#tabela-prontuarios [data-acao="excluir"]').on('click', (event) => {
      prontuarioEmEdicao = event.currentTarget.dataset.id;
      openModal('modal-confirmacao');
    });
  } catch (error) {
    console.error('Erro ao carregar prontuários', error);
    showToast('Não foi possível carregar os prontuários.', 'error');
  }
}
