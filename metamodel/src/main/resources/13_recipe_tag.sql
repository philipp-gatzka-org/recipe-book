create table recipe_tag
(
    recipe_id int not null,
    tag_id    int not null,
    constraint pk_recipe_tag__recipe_id___tag_id primary key (recipe_id, tag_id),
    constraint fk_recipe_tag__recipe_id foreign key (recipe_id) references recipe,
    constraint fk_recipe_tag__tag_id foreign key (tag_id) references tag
);