-- Create tables
create table account
(
    id                      serial                              not null,
    creation_timestamp      timestamp default CURRENT_TIMESTAMP not null,
    modification_timestamp  timestamp default CURRENT_TIMESTAMP not null,
    firstname               varchar(50)                         not null,
    lastname                varchar(50)                         not null,
    email                   varchar(255)                        not null,
    password                varchar(255)                        not null,
    email_verification_code varchar(50),
    password_reset_code     varchar(50),
    constraint pk_account__id
        primary key (id),
    constraint uk_account__email
        unique (email),
    constraint uk_account__email_verification_code
        unique (email_verification_code),
    constraint uk_account__password_reset_code
        unique (password_reset_code)
);
