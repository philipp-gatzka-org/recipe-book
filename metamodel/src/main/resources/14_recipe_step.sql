create table recipe_step
(
    id        serial not null,
    recipe_id int    not null,
    text      text   not null,
    constraint pk_recipe_step__id primary key (id),
    constraint fk_recipe_step__recipe_id foreign key (recipe_id) references recipe
);