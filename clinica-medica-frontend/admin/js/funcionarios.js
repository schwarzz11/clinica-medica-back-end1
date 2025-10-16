let funcionarioEmEdicao = null;
let perfisDisponiveis = [];

$(async function() {
  togglePermissionControls(document.body);

  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-funcionario');
      closeModal('modal-confirmacao');
    });
  });

  $('#btn-novo-funcionario').on('click', () => {
    funcionarioEmEdicao = null;
    document.getElementById('modal-funcionario-titulo').textContent = 'Novo funcionário';
    const form = document.getElementById('form-funcionario');
    resetForm(form);
    form.senha.required = true;
    form.senha.placeholder = 'Defina uma senha temporária';
    openModal('modal-funcionario');
  });

  $('#form-funcionario').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const payload = {
      nome: form.nome.value.trim(),
      cpf: form.cpf.value.trim(),
      email: form.email.value.trim(),
      usuario: form.usuario.value.trim(),
      senha: form.senha.value.trim(),
      sexo: form.sexo.value,
      dataNascimento: form.dataNascimento.value || null,
      rua: form.rua.value.trim(),
      numero: form.numero.value.trim(),
      complemento: form.complemento.value.trim(),
      bairro: form.bairro.value.trim(),
      cidade: form.cidade.value.trim(),
      estado: form.estado.value.trim(),
      tipoFuncionario: form.tipoFuncionario.value,
      perfilId: Number(form.perfilId.value)
    };

    try {
      if (funcionarioEmEdicao) {
        await api.put(`/administrativo/funcionarios/${funcionarioEmEdicao}`, payload);
        showToast('Funcionário atualizado com sucesso.');
      } else {
        await api.post('/administrativo/funcionarios', payload);
        showToast('Funcionário cadastrado com sucesso.');
      }
      closeModal('modal-funcionario');
      await carregarFuncionarios();
    } catch (error) {
      console.error('Erro ao salvar funcionário', error);
      const mensagem = error.payload && error.payload.message ? error.payload.message : 'Não foi possível salvar o funcionário.';
      showToast(mensagem, 'error');
    }
  });

  $('#btn-confirmar-exclusao').on('click', async () => {
    if (!funcionarioEmEdicao) return;
    try {
      await api.delete(`/administrativo/funcionarios/${funcionarioEmEdicao}`);
      showToast('Funcionário removido.');
      closeModal('modal-confirmacao');
      await carregarFuncionarios();
    } catch (error) {
      console.error('Erro ao remover funcionário', error);
      showToast('Não foi possível remover o funcionário.', 'error');
    }
  });

  await carregarPerfis();
  await carregarFuncionarios();
});

async function carregarPerfis() {
  try {
    perfisDisponiveis = await api.get('/administrativo/perfis');
    const select = document.getElementById('perfilId');
    select.innerHTML = '';
    perfisDisponiveis.forEach((perfil) => {
      const option = document.createElement('option');
      option.value = perfil.id;
      option.textContent = perfil.nome;
      select.appendChild(option);
    });
  } catch (error) {
    console.error('Erro ao carregar perfis', error);
    showToast('Não foi possível carregar a lista de perfis.', 'error');
  }
}

async function carregarFuncionarios() {
  try {
    const funcionarios = await api.get('/administrativo/funcionarios');
    const corpo = $('#tabela-funcionarios');
    corpo.empty();
    if (!funcionarios.length) {
      corpo.append('<tr><td colspan="6" class="text-muted">Nenhum funcionário cadastrado.</td></tr>');
      return;
    }
    funcionarios.forEach((funcionario) => {
      const botoes = [];
      if (hasPermission('atualizarFuncionario')) {
        botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${funcionario.id}">Editar</button>`);
      }
      if (hasPermission('deletarFuncionario')) {
        botoes.push(`<button class="btn btn-danger" data-acao="excluir" data-id="${funcionario.id}">Excluir</button>`);
      }
      corpo.append(`
        <tr data-id="${funcionario.id}">
          <td>${funcionario.nome}</td>
          <td>${funcionario.usuario}</td>
          <td>${funcionario.email}</td>
          <td>${funcionario.tipoFuncionario || '-'}</td>
          <td>${funcionario.perfilNome || '-'}</td>
          <td class="text-right"><div class="table-actions">${botoes.join(' ') || '<span class="text-muted">Sem permissões</span>'}</div></td>
        </tr>
      `);
    });

    $('#tabela-funcionarios [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const funcionario = funcionarios.find((f) => String(f.id) === String(id));
      if (!funcionario) return;
      funcionarioEmEdicao = id;
      document.getElementById('modal-funcionario-titulo').textContent = 'Editar funcionário';
      const form = document.getElementById('form-funcionario');
      fillForm(form, funcionario);
      form.perfilId.value = funcionario.perfilId || '';
      form.senha.value = '';
      form.senha.placeholder = 'Defina uma nova senha';
      form.senha.required = true;
      openModal('modal-funcionario');
    });

    $('#tabela-funcionarios [data-acao="excluir"]').on('click', (event) => {
      funcionarioEmEdicao = event.currentTarget.dataset.id;
      openModal('modal-confirmacao');
    });
  } catch (error) {
    console.error('Erro ao carregar funcionários', error);
    showToast('Não foi possível carregar a lista de funcionários.', 'error');
  }
}
