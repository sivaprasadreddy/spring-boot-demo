create sequence comment_id_seq start with 100 increment by 50;

create table comments
(
    id         bigint       not null default nextval('comment_id_seq'),
    post_id    bigint       not null references posts (id),
    name       varchar(150) not null,
    email      varchar(150),
    content    text         not null,
    created_at timestamp    not null,
    updated_at timestamp,
    primary key (id)
);