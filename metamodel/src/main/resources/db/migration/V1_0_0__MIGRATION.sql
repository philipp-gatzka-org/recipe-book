-- 00 Create Schema
create schema if not exists recipe_book;

-- 01 Create sequences

create sequence s_language_id as integer;

create sequence s_account_id as integer;

create sequence s_recipe_book_id as integer;

create sequence s_ingredient_id as integer;

create sequence s_tag_id as integer;

create sequence s_recipe_id as integer;

create sequence s_recipe_step as integer;

-- 02 Create types

create type access as enum ('OWNER', 'MEMBER');

create type member_state as enum ('INVITED', 'JOINED');

create type unit as enum ('LITER', 'DECILITER', 'CENTILITER', 'MILLILITER', 'KILOGRAM','GRAM', 'TABLESPOON', 'TEASPOON', 'PINCH');

create type visibility as enum ('PUBLIC', 'PRIVATE');

-- 03 Create tables

create table language
(
    id                     int          not null default nextval('recipe_book.s_language_id'::regclass),
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    code                   varchar(3)   not null,
    name                   varchar(255) not null,
    constraint pk_language__id primary key (id),
    constraint uk_language__code unique (code),
    constraint uk_language__name unique (name)
);

create table account
(
    id                      int          not null default nextval('recipe_book.s_account_id'::regclass),
    creation_timestamp      timestamp    not null default current_timestamp,
    modification_timestamp  timestamp    not null default current_timestamp,
    firstname               varchar(50)  not null,
    lastname                varchar(50)  not null,
    email                   varchar(255) not null,
    password                varchar(255) not null,
    email_verification_code varchar(255),
    password_reset_code     varchar(255),
    language_id             int          not null,
    constraint pk_account__id primary key (id),
    constraint uk_account__email unique (email),
    constraint uk_account__email_verification_code unique (email_verification_code),
    constraint uk_account__password_reset_code unique (password_reset_code),
    constraint fk_account__language_id foreign key (language_id) references language
);

create table recipe_book
(
    id                     int         not null default nextval('recipe_book.s_recipe_book_id'::regclass),
    creation_timestamp     timestamp   not null default current_timestamp,
    modification_timestamp timestamp   not null default current_timestamp,
    name                   varchar(50) not null,
    visibility             visibility  not null,
    constraint pk_recipe_book__id primary key (id)
);

create table ingredient
(
    id                     int          not null default nextval('recipe_book.s_ingredient_id'::regclass),
    name                   varchar(255) not null,
    recipe_book_id         int          not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    default_unit           unit         not null,
    constraint pk_ingredient__id primary key (id),
    constraint uk_ingredient__name___recipe_book_id unique (name, recipe_book_id),
    constraint fk_ingredient__recipe_book_id foreign key (recipe_book_id) references recipe_book
);

create table tag
(
    id                     int          not null default nextval('recipe_book.s_tag_id'::regclass),
    name                   varchar(255) not null,
    recipe_book_id         int          not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    constraint pk_tag__id primary key (id),
    constraint uk_tag__name___recipe_book_id unique (name, recipe_book_id),
    constraint fk_tag__recipe_book_id foreign key (recipe_book_id) references recipe_book
);

create table recipe_book_member
(
    recipe_book_id         int          not null,
    account_id             int          not null,
    access                 access       not null,
    member_state           member_state not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    constraint pk_recipe_book_member__recipe_book_id___account_id primary key (recipe_book_id, account_id),
    constraint fk_recipe_book_member__recipe_book_id foreign key (recipe_book_id) references recipe_book,
    constraint fk_recipe_book_member__account_id foreign key (account_id) references account
);

create table recipe
(
    id                     int          not null default nextval('recipe_book.s_recipe_id'::regclass),
    name                   varchar(255) not null,
    recipe_book_id         int          not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    constraint pk_recipe__id primary key (id),
    constraint uk_recipe__name___recipe_book_id unique (name, recipe_book_id),
    constraint fk_recipe__recipe_book_id foreign key (recipe_book_id) references recipe_book
);

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

create table recipe_tag
(
    recipe_id int not null,
    tag_id    int not null,
    constraint pk_recipe_tag__recipe_id___tag_id primary key (recipe_id, tag_id),
    constraint fk_recipe_tag__recipe_id foreign key (recipe_id) references recipe,
    constraint fk_recipe_tag__tag_id foreign key (tag_id) references tag
);

create table recipe_step
(
    recipe_id int  not null,
    position  int  not null,
    text      text not null,
    constraint pk_recipe_step__id primary key (recipe_id, position),
    constraint fk_recipe_step__recipe_id foreign key (recipe_id) references recipe
);

-- 04 Create roles
create role recipe_book_server with password 'initialPassword';

-- 04 Set Privileges

grant usage on schema recipe_book to recipe_book_server;

grant select, USAGE on all sequences in schema recipe_book to recipe_book_server;

grant insert, select, update, delete on all tables in schema recipe_book to recipe_book_server;



