DROP TABLE henvendelse;

CREATE TABLE henvendelse
(
    id         VARCHAR DEFAULT uuid_generate_v4() PRIMARY KEY,
    fnr        VARCHAR                  NOT NULL,
    tema       VARCHAR                  NULL,
    temagruppe VARCHAR                  NOT NULL,
    traad_id   VARCHAR                  NOT NULL,
    tidspunkt  TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (fnr, tema, temagruppe, traad_id, tidspunkt)
);
