create tablespace dados_bolsa_familia
  logging
  datafile 'c:\oraclexe\dados\dados_bolsa_familia.dbf' 
  size 500m 
  autoextend on 
  next 100m maxsize 3072m
  extent management local;
  
create tablespace indices_bolsa_familia
  logging
  datafile 'c:\oraclexe\dados\indices_bolsa_familia.dbf' 
  size 200m 
  autoextend on 
  next 10m maxsize 1024m
  extent management local;  

create temporary tablespace temp_bolsa_familia
  tempfile 'c:\oraclexe\dados\temp_bolsa_familia.dbf' 
  size 50m 
  autoextend on 
  next 10m maxsize 200m
  extent management local;

create user bolsa_familia
  identified by bolsa_familia
  default tablespace DADOS_BOLSA_FAMILIA
  temporary tablespace TEMP_BOLSA_FAMILIA
  profile DEFAULT;

grant connect to BOLSA_FAMILIA;
grant resource to BOLSA_FAMILIA;
grant create view to BOLSA_FAMILIA;


ALTER SESSION SET CURRENT_SCHEMA = BOLSA_FAMILIA;