CREATE SCHEMA IF NOT EXISTS beat;

CREATE TABLE beat.administrador (
    id     BIGSERIAL PRIMARY KEY,
    nome   VARCHAR(255) NOT NULL,
    email  VARCHAR(255) NOT NULL UNIQUE,
    senha  VARCHAR(255) NOT NULL
);

CREATE TABLE beat.evento (
    id                BIGSERIAL PRIMARY KEY,
    nome              VARCHAR(255) NOT NULL,
    data              TIMESTAMP NOT NULL,
    local             VARCHAR(255) NOT NULL,
    descricao         TEXT NOT NULL,
    imagem_url        VARCHAR(255) NOT NULL,
    administrador_id  BIGINT NOT NULL,
    CONSTRAINT fk_evento_administrador
        FOREIGN KEY (administrador_id)
        REFERENCES beat.administrador(id)
);