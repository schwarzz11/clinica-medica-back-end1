$(async function() {
  try {
    const [pacientes, consultas, convenios, funcionarios] = await Promise.all([
      api.get('/administrativo/pacientes'),
      api.get('/agendamento/consultas'),
      api.get('/administrativo/convenios'),
      api.get('/administrativo/funcionarios')
    ]);

    $('#metric-pacientes').text(pacientes.length);
    $('#metric-consultas').text(consultas.length);
    $('#metric-convenios').text(convenios.length);
    const medicos = funcionarios.filter((f) => f.tipoFuncionario === 'MEDICO');
    $('#metric-medicos').text(medicos.length);

    renderConsultas(consultas);
    renderConvenios(convenios);
  } catch (error) {
    console.error('Erro ao carregar painel', error);
    showToast('Não foi possível carregar os dados do painel.', 'error');
  }
});

function renderConsultas(consultas) {
  const corpo = $('#tabela-consultas');
  corpo.empty();
  if (!consultas.length) {
    corpo.append('<tr><td colspan="4" class="text-muted">Nenhuma consulta agendada.</td></tr>');
    return;
  }
  consultas
    .sort((a, b) => new Date(a.dataHorario) - new Date(b.dataHorario))
    .slice(0, 5)
    .forEach((consulta) => {
      const tipo = consulta.eRetorno ? '<span class="badge secondary">Retorno</span>' : '<span class="badge">Consulta</span>';
      corpo.append(`
        <tr>
          <td>${formatDateTime(consulta.dataHorario)}</td>
          <td>${consulta.nomePaciente}</td>
          <td>${consulta.nomeMedico}</td>
          <td>${tipo}</td>
        </tr>
      `);
    });
}

function renderConvenios(convenios) {
  const lista = $('#lista-convenios');
  lista.html('');
  if (!convenios.length) {
    lista.append('<li class="text-muted">Nenhum convênio cadastrado.</li>');
    return;
  }
  convenios.slice(0, 5).forEach((convenio) => {
    lista.append(`
      <li class="highlight-card" style="padding: 1rem;">
        <strong>${convenio.nomeEmpresa}</strong>
        <p class="text-muted">CNPJ: ${convenio.cnpj}</p>
      </li>
    `);
  });
}
