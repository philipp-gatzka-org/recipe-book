-- Create Users

DO
$$
    BEGIN
        -- Check if recipe_book_owner exists
        IF NOT EXISTS (SELECT
                       FROM pg_catalog.pg_roles
                       WHERE rolname = 'recipe_book_owner') THEN
            CREATE USER recipe_book_owner WITH PASSWORD 'initialPassword';
        END IF;

        -- Check if recipe_book_server exists
        IF NOT EXISTS (SELECT
                       FROM pg_catalog.pg_roles
                       WHERE rolname = 'recipe_book_server') THEN
            CREATE USER recipe_book_server WITH PASSWORD 'initialPassword';
        END IF;
    END
$$;

-- Create Sequences

create sequence s_account_id as integer;

-- Create Types

-- Create Tables

create table account
(
    id                     integer      not null default nextval('recipe_book.s_account_id'::regclass) not null,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp,
    firstname              varchar(50)  not null,
    lastname               varchar(50)  not null,
    email                  varchar(255) not null,
    email_code             varchar(255),
    password               varchar(255) not null,
    password_code          varchar(255),
    constraint pk_account__id primary key (id),
    constraint uk_account__email unique (email),
    constraint uk_account__email_code unique (email_code)
);

-- Create Views

-- Set table ownership

alter table account
    owner to recipe_book_owner;

-- Grant privileges

grant usage on schema recipe_book to recipe_book_server;

grant select, usage on all sequences in schema recipe_book to recipe_book_server;

grant select, insert, update, delete on all tables in schema recipe_book to recipe_book_server;


