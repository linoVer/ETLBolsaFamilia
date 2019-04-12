create tablespace dados_dw_bolsa_familia
  logging
  datafile 'c:\oraclexe\dados\dados_dw_bolsa_familia.dbf' 
  size 500m 
  autoextend on 
  next 100m maxsize 3072m
  extent management local;
  
create tablespace indices_dw_bolsa_familia
  logging
  datafile 'c:\oraclexe\dados\indices_dw_bolsa_familia.dbf' 
  size 200m 
  autoextend on 
  next 10m maxsize 1024m
  extent management local;  

create temporary tablespace temp_dw_bolsa_familia
  tempfile 'c:\oraclexe\dados\temp_dw_bolsa_familia.dbf' 
  size 50m 
  autoextend on 
  next 10m maxsize 200m
  extent management local;

create user dw_bolsa_familia
  identified by dw_bolsa_familia
  default tablespace DADOS_DW_BOLSA_FAMILIA
  temporary tablespace TEMP_DW_BOLSA_FAMILIA
  profile DEFAULT;

grant connect to DW_BOLSA_FAMILIA;
grant resource to DW_BOLSA_FAMILIA;
grant create view to DW_BOLSA_FAMILIA;


ALTER SESSION SET CURRENT_SCHEMA = DW_BOLSA_FAMILIA;