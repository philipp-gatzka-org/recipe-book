create table ingredient
(
    id                     serial       not null,
    name                   varchar(255) not null,
    recipe_book_id         int          not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    default_unit           unit         not null,
    constraint pk_ingredient__id primary key (id),
    constraint uk_ingredient__name___recipe_book_id unique (name, recipe_book_id),
    constraint fk_ingredient__recipe_book_id foreign key (recipe_book_id) references recipe_book
);