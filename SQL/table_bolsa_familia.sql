drop table cidades_ibge cascade constraints;

create table cidades_ibge(
   cod_ibge   number(7)     not null check( cod_ibge  > 0 ),
   uf    	  varchar(2)       not null check( uf in ( 'PR','SC','RS','SP','RJ','MG','ES',
                                                         'MT','MS','GO','DF','BA','SE','AL',
                                                         'PE','RN','PB','CE','MA','PI','PA',
                                                         'TO','AM','AC','RR','RO', 'AP' ) ),
   cod_uf     number(2)     NOT NULL,
   cid_ibge   varchar2(80)       not null ,
   cod_cidade number(5)		NOT NULL,
   constraint grv_pk primary key( cod_ibge ) );
create index cid_cid_ibge_I on cidades_ibge( cid_ibge );