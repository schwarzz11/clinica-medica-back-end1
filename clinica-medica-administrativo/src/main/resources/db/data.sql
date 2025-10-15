-- Limpa os dados antigos para garantir um início limpo
DELETE FROM funcionarios;
DELETE FROM perfis;
ALTER TABLE perfis AUTO_INCREMENT = 1;
ALTER TABLE funcionarios AUTO_INCREMENT = 1;

-- 1. Insere o perfil de Administrador com todas as permissões ativadas.
INSERT INTO perfis (
    nome, ativo,
    cadastrar_funcionario, ler_funcionario, atualizar_funcionario, deletar_funcionario, listar_funcionario,
    cadastrar_paciente, ler_paciente, atualizar_paciente, deletar_paciente, listar_paciente,
    cadastrar_consulta, ler_consulta, atualizar_consulta, deletar_consulta, listar_consulta,
    cadastrar_especialidade, ler_especialidade, atualizar_especialidade, deletar_especialidade, listar_especialidade,
    cadastrar_convenio, ler_convenio, atualizar_convenio, deletar_convenio, listar_convenio,
    cadastrar_prontuario, ler_prontuario, atualizar_prontuario, deletar_prontuario, listar_prontuario
) VALUES (
    'ADMIN', true,
    true, true, true, true, true, -- Funcionário
    true, true, true, true, true, -- Paciente
    true, true, true, true, true, -- Consulta
    true, true, true, true, true, -- Especialidade
    true, true, true, true, true, -- Convênio
    true, true, true, true, true  -- Prontuário
);

-- 2. Insere o funcionário administrador com a senha 'admin' criptografada.
INSERT INTO funcionarios (
    nome, cpf, email, usuario, senha, sexo, data_nascimento, rua, numero, complemento, bairro, cidade, estado, tipo_funcionario, perfil_id, ativo
) VALUES (
    'Administrador do Sistema', '00000000000', 'admin@clinica.com', 'admin', '$2a$10$3g3dJ/8l5G3i3OKdIqBwB.Tj.gP/a9gL5i0fJg.2Uq3h8cZ1C4hBq', 'M', '2000-01-01', 'Avenida Principal', '123', 'Sala 101', 'Centro', 'Cidade Exemplo', 'MG', 'OUTROS', 1, true
);