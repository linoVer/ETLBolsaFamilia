-- Gerado por Oracle SQL Developer Data Modeler 18.4.0.339.1536
--   em:        2019-04-10 17:58:52 GMT-03:00
--   site:      Oracle Database 11g
--   tipo:      Oracle Database 11g



CREATE TABLE dm_local (
    id_local    NUMBER(8) NOT NULL,
    municipio   VARCHAR2(80) NOT NULL,
    uf          VARCHAR2(2) NOT NULL,
    regiao      VARCHAR2(80) NOT NULL
);

ALTER TABLE dm_local ADD CONSTRAINT dm_local_pk PRIMARY KEY ( id_local );

CREATE TABLE dm_tempo (
    id_tempo    NUMBER(7) NOT NULL,
    data        DATE NOT NULL,
    nu_ano      NUMBER(4) NOT NULL,
    nu_mes      NUMBER(2) NOT NULL,
    nu_dia      NUMBER(2) NOT NULL,
    nu_anomes   NUMBER NOT NULL,
    nm_mes      VARCHAR2(12) NOT NULL,
    sg_mes      VARCHAR2(3) NOT NULL,
    nm_mesano   VARCHAR2(8) NOT NULL,
    bimestre    NUMBER(1) NOT NULL,
    trimestre   NUMBER(1) NOT NULL,
    semestre    NUMBER(1) NOT NULL
);

ALTER TABLE dm_tempo ADD CONSTRAINT dm_tempo_pk PRIMARY KEY ( id_tempo );

CREATE TABLE ft_bolsa_familia (
    id_local          NUMBER(8) NOT NULL,
    id_tempo          NUMBER(7) NOT NULL,
    nu_beneficiados   NUMBER(10) NOT NULL,
    valor_total       NUMBER(12) NOT NULL
);

ALTER TABLE ft_bolsa_familia
    ADD CONSTRAINT table_4_dm_local_fk FOREIGN KEY ( id_local )
        REFERENCES dm_local ( id_local );

ALTER TABLE ft_bolsa_familia
    ADD CONSTRAINT table_4_dm_tempo_fk FOREIGN KEY ( id_tempo )
        REFERENCES dm_tempo ( id_tempo );



-- Relatório do Resumo do Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                             3
-- CREATE INDEX                             0
-- ALTER TABLE                              4
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
