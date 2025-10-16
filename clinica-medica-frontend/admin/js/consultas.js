let consultaEmEdicao = null;
let pacientesDisponiveis = [];
let medicosDisponiveis = [];

$(async function() {
  togglePermissionControls(document.body);

  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-consulta');
      closeModal('modal-cancelar');
    });
  });

  $('#btn-nova-consulta').on('click', () => {
    consultaEmEdicao = null;
    document.getElementById('modal-consulta-titulo').textContent = 'Nova consulta';
    const form = document.getElementById('form-consulta');
    resetForm(form);
    preencherSelects();
    openModal('modal-consulta');
  });

  $('#form-consulta').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const payload = {
      pacienteId: Number(form.pacienteId.value),
      medicoId: Number(form.medicoId.value),
      dataHorario: form.dataHorario.value,
      sintomas: form.sintomas.value.trim(),
      eRetorno: form.eRetorno.checked
    };

    try {
      if (consultaEmEdicao) {
        await api.put(`/agendamento/consultas/${consultaEmEdicao}`, payload);
        showToast('Consulta atualizada com sucesso.');
      } else {
        await api.post('/agendamento/consultas', payload);
        showToast('Consulta cadastrada com sucesso.');
      }
      closeModal('modal-consulta');
      await carregarConsultas();
    } catch (error) {
      console.error('Erro ao salvar consulta', error);
      showToast('Não foi possível salvar a consulta.', 'error');
    }
  });

  $('#btn-confirmar-cancelamento').on('click', async () => {
    if (!consultaComCancelamento) return;
    try {
      await api.delete(`/agendamento/consultas/${consultaComCancelamento}`);
      showToast('Consulta cancelada.');
      closeModal('modal-cancelar');
      await carregarConsultas();
    } catch (error) {
      console.error('Erro ao cancelar consulta', error);
      showToast('Não foi possível cancelar a consulta.', 'error');
    }
  });

  await carregarPacientesEMedicos();
  await carregarConsultas();
});

let consultaComCancelamento = null;

async function carregarPacientesEMedicos() {
  try {
    pacientesDisponiveis = await api.get('/administrativo/pacientes');
    const funcionarios = await api.get('/administrativo/funcionarios');
    medicosDisponiveis = funcionarios.filter((item) => item.tipoFuncionario === 'MEDICO');
    preencherSelects();
  } catch (error) {
    console.error('Erro ao carregar pacientes e médicos', error);
    showToast('Não foi possível carregar pacientes ou médicos.', 'error');
  }
}

function preencherSelects() {
  const selectPacientes = document.getElementById('pacienteId');
  const selectMedicos = document.getElementById('medicoId');
  if (!selectPacientes || !selectMedicos) return;

  selectPacientes.innerHTML = '<option value="">Selecione</option>';
  pacientesDisponiveis.forEach((paciente) => {
    const option = document.createElement('option');
    option.value = paciente.id;
    option.textContent = paciente.nome;
    selectPacientes.appendChild(option);
  });

  selectMedicos.innerHTML = '<option value="">Selecione</option>';
  medicosDisponiveis.forEach((medico) => {
    const option = document.createElement('option');
    option.value = medico.id;
    option.textContent = medico.nome;
    selectMedicos.appendChild(option);
  });
}

async function carregarConsultas() {
  try {
    const consultas = await api.get('/agendamento/consultas');
    const corpo = $('#tabela-consultas');
    corpo.empty();
    if (!consultas.length) {
      corpo.append('<tr><td colspan="7" class="text-muted">Nenhuma consulta cadastrada.</td></tr>');
      return;
    }
    consultas
      .sort((a, b) => new Date(a.dataHorario) - new Date(b.dataHorario))
      .forEach((consulta) => {
        const tipo = consulta.eRetorno ? '<span class="badge secondary">Retorno</span>' : '<span class="badge">Consulta</span>';
        const status = consulta.estaAtiva ? '<span class="status-badge success">Ativa</span>' : '<span class="status-badge warning">Cancelada</span>';
        const botoes = [];
        if (hasPermission('atualizarConsulta') && consulta.estaAtiva) {
          botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${consulta.id}">Editar</button>`);
        }
        if (hasPermission('deletarConsulta') && consulta.estaAtiva) {
          botoes.push(`<button class="btn btn-danger" data-acao="cancelar" data-id="${consulta.id}">Cancelar</button>`);
        }
        corpo.append(`
          <tr data-id="${consulta.id}">
            <td>${formatDateTime(consulta.dataHorario)}</td>
            <td>${consulta.nomePaciente}</td>
            <td>${consulta.nomeMedico}</td>
            <td>${tipo}</td>
            <td>${status}</td>
            <td>${consulta.sintomas ? consulta.sintomas : '-'}</td>
            <td class="text-right"><div class="table-actions">${botoes.join(' ') || '<span class="text-muted">Sem ações</span>'}</div></td>
          </tr>
        `);
      });

    $('#tabela-consultas [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const consulta = consultas.find((item) => String(item.id) === String(id));
      if (!consulta) return;
      consultaEmEdicao = id;
      document.getElementById('modal-consulta-titulo').textContent = 'Editar consulta';
      const form = document.getElementById('form-consulta');
      preencherSelects();
      form.pacienteId.value = consulta.pacienteId || '';
      form.medicoId.value = consulta.medicoId || '';
      form.dataHorario.value = toDateTimeLocal(consulta.dataHorario);
      form.sintomas.value = consulta.sintomas || '';
      form.eRetorno.checked = Boolean(consulta.eRetorno);
      openModal('modal-consulta');
    });

    $('#tabela-consultas [data-acao="cancelar"]').on('click', (event) => {
      consultaComCancelamento = event.currentTarget.dataset.id;
      openModal('modal-cancelar');
    });
  } catch (error) {
    console.error('Erro ao carregar consultas', error);
    showToast('Não foi possível carregar as consultas.', 'error');
  }
}
