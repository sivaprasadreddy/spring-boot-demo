create sequence user_id_seq start with 100 increment by 50;

create table users
(
    id         bigint       not null default nextval('user_id_seq'),
    email      varchar(255) not null,
    password   varchar(255) not null,
    name       varchar(255) not null,
    role       varchar(20)  not null,
    created_at timestamp    not null default current_timestamp,
    updated_at timestamp,
    primary key (id),
    constraint user_email_unique unique (email)
);

create sequence post_id_seq start with 100 increment by 50;

create table posts
(
    id         bigint       not null default nextval('post_id_seq'),
    title      varchar(250) not null,
    slug       varchar(300) not null,
    content    text         not null,
    created_by bigint       not null references users (id),
    created_at timestamp    not null default current_timestamp,
    updated_at timestamp,
    primary key (id),
    constraint posts_slug_unique unique (slug)
);

