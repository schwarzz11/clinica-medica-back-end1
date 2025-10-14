-- Inserção dos perfis com a estrutura correta.
INSERT INTO perfis (
    id, nome,
    cadastrar_funcionario, ler_funcionario, atualizar_funcionario, deletar_funcionario, listar_funcionario,
    cadastrar_paciente, ler_paciente, atualizar_paciente, deletar_paciente, listar_paciente,
    cadastrar_consulta, ler_consulta, atualizar_consulta, deletar_consulta, listar_consulta,
    cadastrar_especialidade, ler_especialidade, atualizar_especialidade, deletar_especialidade, listar_especialidade,
    cadastrar_convenio, ler_convenio, atualizar_convenio, deletar_convenio, listar_convenio,
    cadastrar_prontuario, ler_prontuario, atualizar_prontuario, deletar_prontuario, listar_prontuario,
    ativo
) VALUES
(1, 'ADMINISTRADOR', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
(2, 'MEDICO', 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1),
(3, 'ATENDENTE', 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1);

-- Inserção dos funcionários com a SENHA CRIPTOGRAFADA CORRETA para '123'.
INSERT INTO funcionarios (nome, cpf, email, usuario, senha, sexo, data_nascimento, rua, numero, complemento, bairro, cidade, estado, tipo_funcionario, perfil_id, ativo) VALUES
('Admin User', '11122233344', 'admin@clinicamedica.com', 'admin', '$2a$10$8.Id9q9sF/2nB3B.g92tZ.z2u5v.C.5x/jJk1c.3g5f.o9w.q3Z.K', 'M', '1990-01-01', 'Rua Principal', '123', 'Sala 1', 'Centro', 'Araguari', 'MG', 'OUTROS', 1, 1),
('Dr. Carlos Silva', '55566677788', 'carlos.silva@clinicamedica.com', 'carlos.med', '$2a$10$8.Id9q9sF/2nB3B.g92tZ.z2u5v.C.5x/jJk1c.3g5f.o9w.q3Z.K', 'M', '1985-05-15', 'Avenida dos Médicos', '456', 'Apto 101', 'Jardim Botânico', 'Araguari', 'MG', 'MEDICO', 2, 1),
('Ana Paula', '99988877766', 'ana.paula@clinicamedica.com', 'ana.atende', '$2a$10$8.Id9q9sF/2nB3B.g92tZ.z2u5v.C.5x/jJk1c.3g5f.o9w.q3Z.K', 'F', '1998-10-20', 'Rua das Flores', '789', '', 'Brasil', 'Araguari', 'MG', 'ATENDENTE', 3, 1);