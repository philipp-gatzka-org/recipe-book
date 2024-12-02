create table recipe_book
(
    id                     serial      not null,
    creation_timestamp     timestamp   not null default current_timestamp,
    modification_timestamp timestamp   not null default current_timestamp,
    name                   varchar(50) not null,
    visibility             visibility  not null,
    constraint pk_recipe_book__id primary key (id)
);