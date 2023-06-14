CREATE TABLE henvendelse
(
    id        VARCHAR DEFAULT uuid_generate_v4() PRIMARY KEY,
    fnr       VARCHAR                  NOT NULL,
    tema      VARCHAR                  NOT NULL,
    tidspunkt TIMESTAMP WITH TIME ZONE NOT NULL
);
