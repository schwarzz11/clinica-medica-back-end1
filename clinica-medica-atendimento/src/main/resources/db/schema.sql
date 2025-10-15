-- Cria a tabela de Prontuários, se ela ainda não existir.
CREATE TABLE IF NOT EXISTS prontuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data DATE NOT NULL,
    historico TEXT,
    receituario TEXT,
    exames TEXT,
    observacoes TEXT
);