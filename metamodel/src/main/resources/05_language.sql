create table language
(
    id                     serial       not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    code                   varchar(3)   not null,
    name                   varchar(255) not null,
    constraint pk_language__id primary key (id),
    constraint uk_language__code unique (code),
    constraint uk_language__name unique (name)
);