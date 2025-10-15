-- Cria a tabela de Consultas, se ela ainda não existir.
CREATE TABLE IF NOT EXISTS consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_horario DATETIME NOT NULL,
    motivo_cancelamento VARCHAR(255),
    status VARCHAR(50)
);