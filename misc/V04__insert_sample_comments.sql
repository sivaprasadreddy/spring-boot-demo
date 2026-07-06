insert into comments(id, post_id, email, name, content, created_at, updated_at) values
(1, 1, 'guest@gmail.com', 'Guest','sample comment 1', now(), null);

insert into comments(id, post_id, email, name, content, created_at, updated_at) values
(2, 24, 'test@gmail.com', 'Test','sample comment 2', now(), null);

insert into comments(id, post_id, email, name, content, created_at, updated_at) values
(3, 2, 'test@gmail.com', 'Test','sample comment 3', now(), now());