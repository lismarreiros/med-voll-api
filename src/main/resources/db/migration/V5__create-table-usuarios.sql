create table usuarios(
    id bigint generated always as identity primary key,
    login varchar(100) not null,
    senha varchar(255) not null
);