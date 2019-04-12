drop table tempo_ibge cascade constraints;

create table tempo_ibge(
   cod_tempo   number(7)     not null check( cod_tempo  > 0 ),
   dat_base   date          not NULL,
   constraint tem_pk primary key( cod_tempo ) );
create index dat_dat_base_I on tempos_ibge( dat_base );