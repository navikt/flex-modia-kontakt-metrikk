DROP TABLE henvendelse;

CREATE TABLE henvendelse
(
    id         VARCHAR DEFAULT uuid_generate_v4() PRIMARY KEY,
    fnr        VARCHAR                  NOT NULL,
    tema       VARCHAR                  NULL,
    temagruppe VARCHAR                  NOT NULL,
    thread_id  VARCHAR                  NOT NULL,
    tidspunkt  TIMESTAMP WITH TIME ZONE NOT NULL
);
