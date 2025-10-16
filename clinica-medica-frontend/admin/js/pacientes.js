let pacienteEmEdicao = null;
let conveniosDisponiveis = [];

$(async function() {
  togglePermissionControls(document.body);
  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-paciente');
      closeModal('modal-confirmacao');
    });
  });

  $('#btn-novo-paciente').on('click', () => {
    pacienteEmEdicao = null;
    document.getElementById('modal-paciente-titulo').textContent = 'Novo paciente';
    resetForm(document.getElementById('form-paciente'));
    atualizarCamposConvenio();
    openModal('modal-paciente');
  });

  $('#form-paciente').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const possuiConvenio = form.possuiConvenio.checked;
    const payload = {
      nome: form.nome.value.trim(),
      cpf: form.cpf.value.trim(),
      sexo: form.sexo.value,
      rg: form.rg.value.trim(),
      orgaoEmissor: form.orgaoEmissor.value.trim(),
      dataNascimento: form.dataNascimento.value || null,
      telefone: form.telefone.value.trim(),
      celular: form.celular.value.trim(),
      email: form.email.value.trim(),
      rua: form.rua.value.trim(),
      numero: form.numero.value.trim(),
      complemento: form.complemento.value.trim(),
      bairro: form.bairro.value.trim(),
      cidade: form.cidade.value.trim(),
      estado: form.estado.value.trim(),
      possuiConvenio,
      convenioId: possuiConvenio ? Number(form.convenioId.value) || null : null,
      numeroCarteirinha: possuiConvenio ? form.numeroCarteirinha.value.trim() : null,
      validadeCarteirinha: possuiConvenio ? form.validadeCarteirinha.value || null : null
    };

    try {
      if (pacienteEmEdicao) {
        await api.put(`/administrativo/pacientes/${pacienteEmEdicao}`, payload);
        showToast('Paciente atualizado com sucesso.');
      } else {
        await api.post('/administrativo/pacientes', payload);
        showToast('Paciente cadastrado com sucesso.');
      }
      closeModal('modal-paciente');
      await carregarPacientes();
    } catch (error) {
      console.error('Erro ao salvar paciente', error);
      showToast('Não foi possível salvar o paciente.', 'error');
    }
  });

  $('#btn-confirmar-exclusao').on('click', async () => {
    if (!pacienteEmEdicao) return;
    try {
      await api.delete(`/administrativo/pacientes/${pacienteEmEdicao}`);
      showToast('Paciente excluído.');
      closeModal('modal-confirmacao');
      await carregarPacientes();
    } catch (error) {
      console.error('Erro ao excluir paciente', error);
      showToast('Não foi possível excluir o paciente.', 'error');
    }
  });

  $('#possuiConvenio').on('change', atualizarCamposConvenio);

  await carregarConvenios();
  await carregarPacientes();
});

function atualizarCamposConvenio() {
  const checkbox = document.getElementById('possuiConvenio');
  const habilitado = checkbox.checked;
  document.querySelectorAll('[data-convenio-fields]').forEach((elemento) => {
    elemento.querySelectorAll('input, select').forEach((campo) => {
      campo.disabled = !habilitado;
      if (!habilitado) {
        campo.value = '';
      }
    });
    elemento.style.display = habilitado ? 'block' : 'none';
  });
}

async function carregarConvenios() {
  try {
    conveniosDisponiveis = await api.get('/administrativo/convenios');
    const select = document.getElementById('convenioId');
    select.innerHTML = '<option value="">Selecione</option>';
    conveniosDisponiveis.forEach((convenio) => {
      const option = document.createElement('option');
      option.value = convenio.id;
      option.textContent = convenio.nomeEmpresa;
      select.appendChild(option);
    });
  } catch (error) {
    console.error('Erro ao carregar convênios', error);
    showToast('Não foi possível carregar a lista de convênios.', 'error');
  }
}

async function carregarPacientes() {
  try {
    const pacientes = await api.get('/administrativo/pacientes');
    $('#pacientes-total').text(`${pacientes.length} pacientes cadastrados`);
    const corpo = $('#tabela-pacientes');
    corpo.empty();
    if (!pacientes.length) {
      corpo.append('<tr><td colspan="5" class="text-muted">Nenhum paciente cadastrado.</td></tr>');
      return;
    }
    pacientes.forEach((paciente) => {
      const botoes = [];
      if (hasPermission('atualizarPaciente')) {
        botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${paciente.id}">Editar</button>`);
      }
      if (hasPermission('deletarPaciente')) {
        botoes.push(`<button class="btn btn-danger" data-acao="excluir" data-id="${paciente.id}">Excluir</button>`);
      }
      corpo.append(`
        <tr data-id="${paciente.id}">
          <td>${paciente.nome}</td>
          <td>${paciente.cpf}</td>
          <td>${paciente.telefone || paciente.celular || '-'}</td>
          <td>${paciente.possuiConvenio ? paciente.nomeConvenio || 'Com convênio' : 'Particular'}</td>
          <td class="text-right"><div class="table-actions">${botoes.join(' ') || '<span class="text-muted">Sem permissões</span>'}</div></td>
        </tr>
      `);
    });

    $('#tabela-pacientes [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const paciente = pacientes.find((p) => String(p.id) === String(id));
      if (!paciente) return;
      pacienteEmEdicao = id;
      document.getElementById('modal-paciente-titulo').textContent = 'Editar paciente';
      fillForm(document.getElementById('form-paciente'), paciente);
      document.getElementById('possuiConvenio').checked = paciente.possuiConvenio;
      atualizarCamposConvenio();
      if (paciente.possuiConvenio && paciente.convenioId) {
        document.getElementById('convenioId').value = paciente.convenioId;
      }
      openModal('modal-paciente');
    });

    $('#tabela-pacientes [data-acao="excluir"]').on('click', (event) => {
      pacienteEmEdicao = event.currentTarget.dataset.id;
      openModal('modal-confirmacao');
    });
  } catch (error) {
    console.error('Erro ao carregar pacientes', error);
    showToast('Não foi possível carregar os pacientes.', 'error');
  }
}
