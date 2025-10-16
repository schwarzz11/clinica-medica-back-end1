let perfilEmEdicao = null;
const PERMISSOES = [
  'cadastrarFuncionario','lerFuncionario','atualizarFuncionario','deletarFuncionario','listarFuncionario',
  'cadastrarPaciente','lerPaciente','atualizarPaciente','deletarPaciente','listarPaciente',
  'cadastrarConsulta','lerConsulta','atualizarConsulta','deletarConsulta','listarConsulta',
  'cadastrarEspecialidade','lerEspecialidade','atualizarEspecialidade','deletarEspecialidade','listarEspecialidade',
  'cadastrarConvenio','lerConvenio','atualizarConvenio','deletarConvenio','listarConvenio',
  'cadastrarProntuario','lerProntuario','atualizarProntuario','deletarProntuario','listarProntuario'
];

$(function() {
  togglePermissionControls(document.body);
  carregarPerfis();

  document.querySelectorAll('[data-close]').forEach((element) => {
    element.addEventListener('click', () => {
      closeModal('modal-perfil');
      closeModal('modal-confirmacao');
    });
  });

  $('#btn-novo-perfil').on('click', () => {
    perfilEmEdicao = null;
    document.getElementById('modal-perfil-titulo').textContent = 'Novo perfil';
    const form = document.getElementById('form-perfil');
    resetForm(form);
    openModal('modal-perfil');
  });

  $('#form-perfil').on('submit', async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const payload = { nome: form.nome.value.trim() };
    PERMISSOES.forEach((permissao) => {
      payload[permissao] = form[permissao].checked;
    });

    try {
      if (perfilEmEdicao) {
        await api.put(`/administrativo/perfis/${perfilEmEdicao}`, payload);
        showToast('Perfil atualizado com sucesso.');
      } else {
        await api.post('/administrativo/perfis', payload);
        showToast('Perfil criado com sucesso.');
      }
      closeModal('modal-perfil');
      await carregarPerfis();
    } catch (error) {
      console.error('Erro ao salvar perfil', error);
      const mensagem = error.payload && error.payload.message ? error.payload.message : 'Não foi possível salvar o perfil.';
      showToast(mensagem, 'error');
    }
  });

  $('#btn-confirmar-exclusao').on('click', async () => {
    if (!perfilEmEdicao) return;
    try {
      await api.delete(`/administrativo/perfis/${perfilEmEdicao}`);
      showToast('Perfil excluído.');
      closeModal('modal-confirmacao');
      await carregarPerfis();
    } catch (error) {
      console.error('Erro ao excluir perfil', error);
      showToast('Não foi possível excluir o perfil.', 'error');
    }
  });
});

async function carregarPerfis() {
  try {
    const perfis = await api.get('/administrativo/perfis');
    const corpo = $('#tabela-perfis');
    corpo.empty();
    if (!perfis.length) {
      corpo.append('<tr><td colspan="7" class="text-muted">Nenhum perfil cadastrado.</td></tr>');
      return;
    }
    perfis.forEach((perfil) => {
      const botoes = [];
      botoes.push(`<button class="btn btn-outline" data-acao="editar" data-id="${perfil.id}">Editar</button>`);
      botoes.push(`<button class="btn btn-danger" data-acao="excluir" data-id="${perfil.id}">Excluir</button>`);
      corpo.append(`
        <tr data-id="${perfil.id}">
          <td>${perfil.nome}</td>
          <td>${resumoModulo(perfil, 'Funcionario')}</td>
          <td>${resumoModulo(perfil, 'Paciente')}</td>
          <td>${resumoModulo(perfil, 'Consulta')}</td>
          <td>${resumoModulo(perfil, 'Especialidade')}</td>
          <td>${resumoModulo(perfil, 'Convenio')}</td>
          <td>${resumoModulo(perfil, 'Prontuario')}</td>
          <td class="text-right"><div class="table-actions">${botoes.join(' ')}</div></td>
        </tr>
      `);
    });

    $('#tabela-perfis [data-acao="editar"]').on('click', (event) => {
      const id = event.currentTarget.dataset.id;
      const perfil = perfis.find((item) => String(item.id) === String(id));
      if (!perfil) return;
      perfilEmEdicao = id;
      document.getElementById('modal-perfil-titulo').textContent = 'Editar perfil';
      const form = document.getElementById('form-perfil');
      form.nome.value = perfil.nome;
      PERMISSOES.forEach((permissao) => {
        if (form[permissao]) {
          form[permissao].checked = Boolean(perfil[permissao]);
        }
      });
      openModal('modal-perfil');
    });

    $('#tabela-perfis [data-acao="excluir"]').on('click', (event) => {
      perfilEmEdicao = event.currentTarget.dataset.id;
      openModal('modal-confirmacao');
    });
  } catch (error) {
    console.error('Erro ao carregar perfis', error);
    showToast('Não foi possível carregar os perfis.', 'error');
  }
}

function resumoModulo(perfil, modulo) {
  const chaves = [
    { chave: `cadastrar${modulo}`, rotulo: 'C' },
    { chave: `ler${modulo}`, rotulo: 'L' },
    { chave: `atualizar${modulo}`, rotulo: 'A' },
    { chave: `deletar${modulo}`, rotulo: 'D' },
    { chave: `listar${modulo}`, rotulo: 'Lst' }
  ];
  return chaves
    .map((item) => `<span class="badge ${perfil[item.chave] ? '' : 'neutral'}">${item.rotulo}</span>`)
    .join(' ');
}
