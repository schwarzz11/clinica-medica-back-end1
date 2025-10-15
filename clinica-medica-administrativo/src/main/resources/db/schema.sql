-- Este script cria as tabelas necessárias para o serviço administrativo.
-- O `ddl-auto=update` pode criar isso automaticamente, mas é uma boa prática tê-lo definido.

-- Cria a tabela de Perfis
CREATE TABLE IF NOT EXISTS perfis (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL UNIQUE,
  ativo BOOLEAN DEFAULT TRUE,
  cadastrar_funcionario BOOLEAN,
  ler_funcionario BOOLEAN,
  atualizar_funcionario BOOLEAN,
  deletar_funcionario BOOLEAN,
  listar_funcionario BOOLEAN,
  cadastrar_paciente BOOLEAN,
  ler_paciente BOOLEAN,
  atualizar_paciente BOOLEAN,
  deletar_paciente BOOLEAN,
  listar_paciente BOOLEAN,
  cadastrar_consulta BOOLEAN,
  ler_consulta BOOLEAN,
  atualizar_consulta BOOLEAN,
  deletar_consulta BOOLEAN,
  listar_consulta BOOLEAN,
  cadastrar_especialidade BOOLEAN,
  ler_especialidade BOOLEAN,
  atualizar_especialidade BOOLEAN,
  deletar_especialidade BOOLEAN,
  listar_especialidade BOOLEAN,
  cadastrar_convenio BOOLEAN,
  ler_convenio BOOLEAN,
  atualizar_convenio BOOLEAN,
  deletar_convenio BOOLEAN,
  listar_convenio BOOLEAN,
  cadastrar_prontuario BOOLEAN,
  ler_prontuario BOOLEAN,
  atualizar_prontuario BOOLEAN,
  deletar_prontuario BOOLEAN,
  listar_prontuario BOOLEAN
);

-- Cria a tabela de Funcionários
CREATE TABLE IF NOT EXISTS funcionarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  usuario VARCHAR(50) NOT NULL UNIQUE,
  senha VARCHAR(255) NOT NULL,
  sexo CHAR(1),
  data_nascimento DATE,
  rua VARCHAR(255),
  numero VARCHAR(20),
  complemento VARCHAR(100),
  bairro VARCHAR(100),
  cidade VARCHAR(100),
  estado VARCHAR(2),
  tipo_funcionario VARCHAR(50),
  perfil_id BIGINT,
  ativo BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (perfil_id) REFERENCES perfis(id)
);

-- Adicione aqui outras tabelas que este serviço gerencia (Pacientes, Convenios, Especialidades)