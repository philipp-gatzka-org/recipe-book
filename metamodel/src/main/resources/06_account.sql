create table account
(
    id                      serial       not null,
    creation_timestamp      timestamp    not null default current_timestamp,
    modification_timestamp  timestamp    not null default current_timestamp,
    firstname               varchar(50)  not null,
    lastname                varchar(50)  not null,
    email                   varchar(255) not null,
    password                varchar(255) not null,
    email_verification_code varchar(255) not null,
    password_reset_code     varchar(255) not null,
    language_id             int          not null,
    constraint pk_account__id primary key (id),
    constraint uk_account__email unique (email),
    constraint uk_account__email_verification_code unique (email_verification_code),
    constraint uk_account__password_reset_code unique (password_reset_code),
    constraint fk_account__language_id foreign key (language_id) references language
);