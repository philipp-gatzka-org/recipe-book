create table recipe_ingredient
(
    recipe_id     int  not null,
    ingredient_id int  not null,
    unit          unit not null,
    amount        int  not null,
    constraint pk_recipe_ingredient__recipe_id___ingredient_id primary key (recipe_id, ingredient_id),
    constraint fk_recipe_ingredient__recipe_id foreign key (recipe_id) references recipe,
    constraint fk_recipe_ingredient__ingredient_id foreign key (ingredient_id) references ingredient
);