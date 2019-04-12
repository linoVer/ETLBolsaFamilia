-- Gerado por Oracle SQL Developer Data Modeler 18.4.0.339.1536
--   em:        2019-04-11 21:29:38 GMT-03:00
--   site:      Oracle Database 11g
--   tipo:      Oracle Database 11g



CREATE TABLE dados_aux (
    id                        NUMBER(10) NOT NULL,
    data_referencia           DATE NOT NULL,
    cod_ibge                  NUMBER(10) NOT NULL,
    nome_ibge                 VARCHAR2(50) NOT NULL,
    valor                     NUMBER(12, 2) NOT NULL,
    quantidade_beneficiados   NUMBER(10) NOT NULL
);

ALTER TABLE dados_aux ADD CONSTRAINT dados_aux_pk PRIMARY KEY ( id );



-- Relatório do Resumo do Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                             1
-- CREATE INDEX                             0
-- ALTER TABLE                              1
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
