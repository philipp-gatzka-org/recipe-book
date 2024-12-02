create table recipe_book_member
(
    recipe_book_id int          not null,
    account_id     int          not null,
    access         access       not null,
    member_state   member_state not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    constraint pk_recipe_book_member__recipe_book_id___account_id primary key (recipe_book_id, account_id),
    constraint fk_recipe_book_member__recipe_book_id foreign key (recipe_book_id) references recipe_book,
    constraint fk_recipe_book_member__account_id foreign key (account_id) references account
);